package ucsd.III;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LocalAlignment {
    private enum Action {
        INSERTION, DELETION, MATCH, FREE
    }

    private static int maxLength, iMax, jMax;

    private static Action[][] getBacktrackMatrix(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                                String v, String w) {
        Action[][] backtrackMatrix = new Action[v.length() + 1][w.length() + 1];
        int[][] lengthMatrix = new int[v.length() + 1][w.length() + 1];

        //initialize edges
        for (int i = 0; i <= v.length(); i++) {
            lengthMatrix[i][0] = 0;
        }
        for (int j = 0; j <= w.length(); j++) {
            lengthMatrix[0][j] = 0;
        }
        for (int i = 1; i <= v.length(); i++) {
            for (int j = 1; j <= w.length(); j++) {
                List<Integer> scores = Arrays.asList(
                        0,
                        lengthMatrix[i - 1][j] - indelPenalty,
                        lengthMatrix[i][j - 1] - indelPenalty,
                        lengthMatrix[i - 1][j - 1]
                                + scoringMatrix.get(new AbstractMap.SimpleEntry(v.charAt(i - 1), w.charAt(j - 1))));
                lengthMatrix[i][j] = Collections.max(scores);
                if (lengthMatrix[i][j] >= maxLength) {
                    maxLength = lengthMatrix[i][j];
                    iMax = i;
                    jMax = j;
                }
                if (lengthMatrix[i][j] == scores.get(0)) {
                    backtrackMatrix[i][j] = Action.FREE;
                } else if (lengthMatrix[i][j] ==  scores.get(1)) {
                    backtrackMatrix[i][j] = Action.DELETION;
                } else if (lengthMatrix[i][j] == scores.get(2)) {
                    backtrackMatrix[i][j] = Action.INSERTION;
                } else if (lengthMatrix[i][j] == scores.get(3)) {
                    backtrackMatrix[i][j] = Action.MATCH;
                }
            }
        }
        return backtrackMatrix;
    }

    public static String getActionsFromBacktrack(Action[][] backtrackMatrix, int i, int j) {
        if (i != 0 && j != 0) {
            Action action = backtrackMatrix[i][j];
            switch (action) {
                case DELETION:
                    return getActionsFromBacktrack(backtrackMatrix, i - 1, j) + " DEL ";
                case INSERTION:
                    return getActionsFromBacktrack(backtrackMatrix, i, j - 1) + " INS ";
                case MATCH:
                    return getActionsFromBacktrack(backtrackMatrix, i - 1, j - 1) + " MAT ";
                case FREE:
                    return "FREE ";
            }
        }
        return "";
    }

    public static String getAlignmentFromBacktrack(Action[][] backtrackMatrix, String s, int i, int j,
                                                   boolean isReference) {
        if (isReference) {
            if (i != 0 && j != 0) {
                Action action = backtrackMatrix[i][j];
                switch (action) {
                    case DELETION:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i - 1, j, isReference) + s.charAt(i - 1);
                    case INSERTION:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i, j - 1, isReference) + "-";
                    case MATCH:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i - 1, j - 1, isReference) + s.charAt(i - 1);
                    case FREE:
                        return "";
                }
            }
        } else {
            if (i != 0 && j != 0) {
                Action action = backtrackMatrix[i][j];
                switch (action) {
                    case DELETION:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i - 1, j, isReference) + "-";
                    case INSERTION:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i, j - 1, isReference) + s.charAt(j - 1);
                    case MATCH:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i - 1, j - 1, isReference) + s.charAt(j - 1);
                    case FREE:
                        return "";
                }
            }
        }
        return "";
    }

    public static int getScoreFromBacktrack(Action[][] backtrackMatrix, Map<Map.Entry, Integer> scoringMatrix,
                                            int indelPenalty, String v, String w, int i, int j) {
        if (i != 0 && j != 0) {
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
                case FREE:
                    return 0;
            }
        }
        return 0;
    }

    /**
     * Prints a backtrack matrix.
     * @param matrix    matrix to print
     */
    public static void printMatrix(Action[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String v = br.readLine();
            String w = br.readLine();

            Map<Map.Entry, Integer> scoringMatrix = DataTableUtils
                    .getScoringMatrix("/home/slee/working/ucsd-bioinformatics/src/test/resources/PAM250_1.txt");

            int indelPenalty = 5;
            maxLength = iMax = jMax = 0;

            Action[][] backtrackMatrix = getBacktrackMatrix(scoringMatrix, indelPenalty, v, w);

//            printMatrix(backtrackMatrix);
//            System.out.println(getActionsFromBacktrack(backtrackMatrix, v.length(), w.length()));

            int score = getScoreFromBacktrack(backtrackMatrix, scoringMatrix, indelPenalty,
                    v, w, iMax, jMax);
            String vAlign = getAlignmentFromBacktrack(backtrackMatrix, v, iMax, jMax, true);
            String wAlign = getAlignmentFromBacktrack(backtrackMatrix, w, iMax, jMax, false);

            return ConsoleCapturer.toString(score + "\n" + vAlign + "\n" + wAlign);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_247_9.txt");
        System.out.println(result);
    }
}
