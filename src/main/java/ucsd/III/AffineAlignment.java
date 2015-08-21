package ucsd.III;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AffineAlignment {
    private static final int NEGATIVE_INFINITY = -100000;

    private enum Action {
        INSERTION, DELETION, MATCH, CLOSE_UP, CLOSE_DOWN, EXTEND_INSERTION, EXTEND_DELETION
    }

    public static List<Integer> getScores(Map<Map.Entry, Integer> scoringMatrix,
                                          int openGapPenalty, int extendGapPenalty,
                                          String v, String w) {
        int[][][] lengthMatrix = new int[v.length() + 1][w.length() + 1][3];

        //initialize edges
        for (int i = 1; i <= v.length(); i++) {
            lengthMatrix[i][0][0] = -openGapPenalty - extendGapPenalty*(i - 1);
            lengthMatrix[i][0][1] = -openGapPenalty - extendGapPenalty*(i - 1);
            lengthMatrix[i][0][2] = NEGATIVE_INFINITY;
        }
        for (int j = 1; j <= w.length(); j++) {
            lengthMatrix[0][j][0] = NEGATIVE_INFINITY;
            lengthMatrix[0][j][1] = -openGapPenalty - extendGapPenalty*(j - 1);
            lengthMatrix[0][j][2] = -openGapPenalty - extendGapPenalty*(j - 1);
        }
        lengthMatrix[0][0][0] = NEGATIVE_INFINITY;
        lengthMatrix[0][0][1] = 0;
        lengthMatrix[0][0][2] = NEGATIVE_INFINITY;

        for (int i = 1; i <= v.length(); i++) {
            for (int j = 1; j <= w.length(); j++) {
                lengthMatrix[i][j][0] = Math.max(
                        lengthMatrix[i - 1][j][0] - extendGapPenalty,
                        lengthMatrix[i - 1][j][1] - openGapPenalty);

                lengthMatrix[i][j][2] = Math.max(
                        lengthMatrix[i][j - 1][1] - openGapPenalty,
                        lengthMatrix[i][j - 1][2] - extendGapPenalty);

                lengthMatrix[i][j][1] = Collections.max(Arrays.asList(
                        lengthMatrix[i][j][0],
                        lengthMatrix[i - 1][j - 1][1] +
                                scoringMatrix.get(new AbstractMap.SimpleEntry(v.charAt(i - 1), w.charAt(j - 1))),
                        lengthMatrix[i][j][2]));
            }
        }
        return Arrays.stream(lengthMatrix[v.length()][w.length()]).boxed().collect(Collectors.toList());
    }

    public static Action[][][] getBacktrackMatrix(Map<Map.Entry, Integer> scoringMatrix,
                                                  int openGapPenalty, int extendGapPenalty,
                                                  String v, String w) {
        Action[][][] backtrackMatrix = new Action[v.length() + 1][w.length() + 1][3];
        int[][][] lengthMatrix = new int[v.length() + 1][w.length() + 1][3];

        //initialize edges
        for (int i = 1; i <= v.length(); i++) {
            lengthMatrix[i][0][0] = -openGapPenalty - extendGapPenalty*(i - 1);
            lengthMatrix[i][0][1] = -openGapPenalty - extendGapPenalty*(i - 1);
            lengthMatrix[i][0][2] = NEGATIVE_INFINITY;
        }
        for (int j = 1; j <= w.length(); j++) {
            lengthMatrix[0][j][0] = NEGATIVE_INFINITY;
            lengthMatrix[0][j][1] = -openGapPenalty - extendGapPenalty*(j - 1);
            lengthMatrix[0][j][2] = -openGapPenalty - extendGapPenalty*(j - 1);
        }
        lengthMatrix[0][0][0] = NEGATIVE_INFINITY;
        lengthMatrix[0][0][1] = 0;
        lengthMatrix[0][0][2] = NEGATIVE_INFINITY;

        for (int i = 1; i <= v.length(); i++) {
            for (int j = 1; j <= w.length(); j++) {
                List<Integer> newLengthsLower = Arrays.asList(
                        lengthMatrix[i - 1][j][0] - extendGapPenalty,
                        lengthMatrix[i - 1][j][1] - openGapPenalty);
                int indexOfLongestLower = IntStream.range(0, newLengthsLower.size()).boxed()
                        .max(Comparator.comparing(n -> newLengthsLower.get(n))).get();
                lengthMatrix[i][j][0] = newLengthsLower.get(indexOfLongestLower);
                switch (indexOfLongestLower) {
                    case 0: backtrackMatrix[i][j][0] = Action.EXTEND_DELETION;
                        break;
                    case 1: backtrackMatrix[i][j][0] = Action.DELETION;
                        break;
                }

                List<Integer> newLengthsUpper = Arrays.asList(
                        lengthMatrix[i][j - 1][1] - openGapPenalty,
                        lengthMatrix[i][j - 1][2] - extendGapPenalty);
                int indexOfLongestUpper = IntStream.range(0, newLengthsUpper.size()).boxed()
                        .max(Comparator.comparing(n -> newLengthsUpper.get(n))).get();
                lengthMatrix[i][j][2] = newLengthsUpper.get(indexOfLongestUpper);
                switch (indexOfLongestUpper) {
                    case 0: backtrackMatrix[i][j][2] = Action.INSERTION;
                        break;
                    case 1: backtrackMatrix[i][j][2] = Action.EXTEND_INSERTION;
                        break;
                }

                List<Integer> newLengths = Arrays.asList(
                        lengthMatrix[i][j][0],
                        lengthMatrix[i - 1][j - 1][1] +
                                scoringMatrix.get(new AbstractMap.SimpleEntry(v.charAt(i - 1), w.charAt(j - 1))),
                        lengthMatrix[i][j][2]);
                int indexOfLongest = IntStream.range(0, newLengths.size()).boxed()
                        .max(Comparator.comparing(n -> newLengths.get(n))).get();
                lengthMatrix[i][j][1] = newLengths.get(indexOfLongest);
                switch (indexOfLongest) {
                    case 0: backtrackMatrix[i][j][1] = Action.CLOSE_UP;
                        break;
                    case 1: backtrackMatrix[i][j][1] = Action.MATCH;
                        break;
                    case 2: backtrackMatrix[i][j][1] = Action.CLOSE_DOWN;
                        break;
                }
            }
        }
        return backtrackMatrix;
    }

    public static String getActionsFromBacktrack(Action[][][] backtrackMatrix, int i, int j, int level) {
        if (i == 0 && j != 0) {
            return getActionsFromBacktrack(backtrackMatrix, i, j - 1, level) + " INS ";
        }
        if (i != 0 && j == 0) {
            return getActionsFromBacktrack(backtrackMatrix, i - 1, j, level) + " DEL ";
        }
        if (i != 0 && j != 0) {
            Action action = backtrackMatrix[i][j][level];
            switch (action) {
                case CLOSE_UP:
                    return getActionsFromBacktrack(backtrackMatrix, i, j, 0) + " CL ";
                case CLOSE_DOWN:
                    return getActionsFromBacktrack(backtrackMatrix, i, j, 2) + " CL ";
                case MATCH:
                    return getActionsFromBacktrack(backtrackMatrix, i - 1, j - 1, 1) + " MAT ";
                case INSERTION:
                    return getActionsFromBacktrack(backtrackMatrix, i, j - 1, 1) + " INS ";
                case DELETION:
                    return getActionsFromBacktrack(backtrackMatrix, i - 1, j, 1) + " DEL ";
                case EXTEND_DELETION:
                    return getActionsFromBacktrack(backtrackMatrix, i - 1, j, 0) + " XDEL ";
                case EXTEND_INSERTION:
                    return getActionsFromBacktrack(backtrackMatrix, i, j - 1, 2) + " XINS ";
            }
        }
        return "";
    }

    public static List<String> getAlignmentFromPath(String path, String v, String w) {
        String vAlign, wAlign;
        vAlign = wAlign = "";
        List<String> actions = Arrays.asList(path.split("\\s+"));
        for (String action : actions) {
            switch (action) {
                case "MAT":
                    vAlign += v.charAt(0);
                    wAlign += w.charAt(0);
                    v = v.substring(1);
                    w = w.substring(1);
                    break;
                case "INS":
                    vAlign += "-";
                    wAlign += w.charAt(0);
                    w = w.substring(1);
                    break;
                case "DEL":
                    vAlign += v.charAt(0);
                    wAlign += "-";
                    v = v.substring(1);
                    break;
                case "XINS":
                    vAlign += "-";
                    wAlign += w.charAt(0);
                    w = w.substring(1);
                    break;
                case "XDEL":
                    vAlign += v.charAt(0);
                    wAlign += "-";
                    v = v.substring(1);
                    break;
            }
        }
        return Arrays.asList(vAlign, wAlign);
    }

    public static int getScoreFromBacktrack(Action[][] backtrackMatrix, Map<Map.Entry, Integer> scoringMatrix,
                                            int indelPenalty, String v, String w, int i, int j) {
        if (i == 0 && j == 0) {
            return 0;
        }
        if (i == 0) {
            return -indelPenalty*j;
        }
        if (j == 0) {
            return -indelPenalty*i;
        }
        Action action = backtrackMatrix[i][j];
        switch (action) {
            case DELETION:
                return getScoreFromBacktrack(backtrackMatrix, scoringMatrix, indelPenalty, v, w, i - 1, j)
                        - indelPenalty;
            case INSERTION:
                return getScoreFromBacktrack(backtrackMatrix, scoringMatrix, indelPenalty, v, w, i, j - 1)
                        - indelPenalty;
            case MATCH:
                return getScoreFromBacktrack(backtrackMatrix, scoringMatrix, indelPenalty, v, w, i - 1, j - 1)
                        + scoringMatrix.get(new AbstractMap.SimpleEntry(v.charAt(i - 1), w.charAt(j - 1)));
            default: return 0;
        }
    }

    /**
     * Prints a backtrack matrix.
     * @param matrix    matrix to print
     */
    public static void printMatrix(Action[][][] matrix) {
        int numMats = matrix[0][0].length;
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        for (int k = 0; k < numMats; k++) {
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    System.out.print(matrix[i][j][k] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String v = br.readLine();
            String w = br.readLine();

            Map<Map.Entry, Integer> scoringMatrix = DataTableUtils
                    .getScoringMatrix("/home/slee/working/ucsd-bioinformatics/src/test/resources/BLOSUM62.txt");

            int openGapPenalty = 11;
            int extendGapPenalty = 1;

            List<Integer> scores = getScores(scoringMatrix, openGapPenalty, extendGapPenalty, v, w);
//            System.out.println(scores);
            int score = Collections.max(scores);

            Action[][][] backtrackMatrix = getBacktrackMatrix(scoringMatrix, openGapPenalty, extendGapPenalty, v, w);

//            printMatrix(backtrackMatrix);
//            System.out.println(getActionsFromBacktrack(backtrackMatrix, v.length(), w.length()));

            String path = getActionsFromBacktrack(backtrackMatrix, v.length(), w.length(), 1);
//            System.out.println(path);

//            int score = getScoreFromBacktrack(backtrackMatrix, scoringMatrix, indelPenalty,
//                    v, w, v.length(), w.length());
            List<String> alignment = getAlignmentFromPath(path, v, w);

            return ConsoleCapturer.toString(score + "\n" + alignment.stream().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_249_8.txt");
        System.out.println(result);
    }
}
