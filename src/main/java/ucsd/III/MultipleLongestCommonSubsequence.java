package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultipleLongestCommonSubsequence {
    private static final String INSERTION_1_NAME = "INS_1";
    private static final String INSERTION_2_NAME = "INS_2";
    private static final String INSERTION_3_NAME = "INS_3";
    private static final String INSERTION_23_NAME = "INS_23";
    private static final String INSERTION_13_NAME = "INS_13";
    private static final String INSERTION_12_NAME = "INS_12";

    private enum Action {
        MATCH,
        INSERTION_1,
        INSERTION_2,
        INSERTION_3,
        INSERTION_23,
        INSERTION_13,
        INSERTION_12
    }

    public static int getScore(String v, String w, String x) {
        int[][][] lengthMatrix = new int[v.length() + 1][w.length() + 1][x.length() + 1];

        //initialize edges
        for (int i = 0; i <= v.length(); i++) {
            lengthMatrix[i][0][0] = 0;
        }
        for (int j = 0; j <= w.length(); j++) {
            lengthMatrix[0][j][0] = 0;
        }
        for (int k = 0; k <= x.length(); k++) {
            lengthMatrix[0][0][k] = 0;
        }
        for (int i = 1; i <= v.length(); i++) {
            for (int j = 1; j <= w.length(); j++) {
                for (int k = 1; k <= x.length(); k++) {
                    lengthMatrix[i][j][k] = Collections.max(Arrays.asList(
                            v.charAt(i - 1) == w.charAt(j - 1) && w.charAt(j - 1) == x.charAt(k - 1) ?
                                    lengthMatrix[i - 1][j - 1][k - 1] + 1 : 0,
                            lengthMatrix[i - 1][j][k],
                            lengthMatrix[i][j - 1][k],
                            lengthMatrix[i][j][k - 1],
                            lengthMatrix[i][j - 1][k - 1],
                            lengthMatrix[i - 1][j][k - 1],
                            lengthMatrix[i - 1][j - 1][k]));
                }
            }
        }
        return lengthMatrix[v.length()][w.length()][x.length()];
    }

    public static Action[][][] getBacktrackMatrix(String v, String w, String x) {
        Action[][][] backtrackMatrix = new Action[v.length() + 1][w.length() + 1][x.length() + 1];
        int[][][] lengthMatrix = new int[v.length() + 1][w.length() + 1][x.length() + 1];

        //initialize edges
        for (int i = 0; i <= v.length(); i++) {
            lengthMatrix[i][0][0] = 0;
        }
        for (int j = 0; j <= w.length(); j++) {
            lengthMatrix[0][j][0] = 0;
        }
        for (int k = 0; k <= x.length(); k++) {
            lengthMatrix[0][0][k] = 0;
        }
        for (int i = 1; i <= v.length(); i++) {
            for (int j = 1; j <= w.length(); j++) {
                for (int k = 1; k <= x.length(); k++) {
                    List<Integer> newLengths = Arrays.asList(
                            v.charAt(i - 1) == w.charAt(j - 1) && w.charAt(j - 1) == x.charAt(k - 1) ?
                                    lengthMatrix[i - 1][j - 1][k - 1] + 1 : 0,
                            lengthMatrix[i - 1][j][k],
                            lengthMatrix[i][j - 1][k],
                            lengthMatrix[i][j][k - 1],
                            lengthMatrix[i][j - 1][k - 1],
                            lengthMatrix[i - 1][j][k - 1],
                            lengthMatrix[i - 1][j - 1][k]);
                    int indexOfLongest = IntStream.range(0, newLengths.size()).boxed()
                            .max(Comparator.comparing(n -> newLengths.get(n))).get();
                    lengthMatrix[i][j][k] = newLengths.get(indexOfLongest);
                    switch (indexOfLongest) {
                        case 0: backtrackMatrix[i][j][k] = Action.MATCH;
                                break;
                        case 1: backtrackMatrix[i][j][k] = Action.INSERTION_1;
                                break;
                        case 2: backtrackMatrix[i][j][k] = Action.INSERTION_2;
                                break;
                        case 3: backtrackMatrix[i][j][k] = Action.INSERTION_3;
                                break;
                        case 4: backtrackMatrix[i][j][k] = Action.INSERTION_23;
                                break;
                        case 5: backtrackMatrix[i][j][k] = Action.INSERTION_13;
                                break;
                        case 6: backtrackMatrix[i][j][k] = Action.INSERTION_12;
                                break;
                    }
                }
            }
        }
        return backtrackMatrix;
    }

    public static String getLongestPathFromBacktrack(Action[][][] backtrackMatrix, int i, int j, int k) {
        if (i == 0 && j == 0 && k == 0) {
            return "";
        }
        if (i != 0 && j == 0 && k == 0) {
            return getLongestPathFromBacktrack(backtrackMatrix, i - 1, j, k) + " " + INSERTION_1_NAME;
        }
        if (i == 0 && j != 0 && k == 0) {
            return getLongestPathFromBacktrack(backtrackMatrix, i, j - 1, k) + " " + INSERTION_2_NAME;
        }
        if (i == 0 && j == 0 && k != 0) {
            return getLongestPathFromBacktrack(backtrackMatrix, i, j, k - 1) + " " + INSERTION_3_NAME;
        }
        if (i == 0 && j != 0 && k != 0) {
            return getLongestPathFromBacktrack(backtrackMatrix, i, j - 1, k - 1) + " " + INSERTION_23_NAME;
        }
        if (i != 0 && j == 0 && k != 0) {
            return getLongestPathFromBacktrack(backtrackMatrix, i - 1, j, k - 1) + " " + INSERTION_13_NAME;
        }
        if (i != 0 && j != 0 && k == 0) {
            return getLongestPathFromBacktrack(backtrackMatrix, i - 1, j - 1, k) + " " + INSERTION_12_NAME;
        }
        Action action = backtrackMatrix[i][j][k];
        switch (action) {
            case MATCH:
                return getLongestPathFromBacktrack(backtrackMatrix, i - 1, j - 1, k - 1) + " " + "MAT";
            case INSERTION_1:
                return getLongestPathFromBacktrack(backtrackMatrix, i - 1, j, k) + " " + INSERTION_1_NAME;
            case INSERTION_2:
                return getLongestPathFromBacktrack(backtrackMatrix, i, j - 1, k) + " " + INSERTION_2_NAME;
            case INSERTION_3:
                return getLongestPathFromBacktrack(backtrackMatrix, i, j, k - 1) + " " + INSERTION_3_NAME;
            case INSERTION_23:
                return getLongestPathFromBacktrack(backtrackMatrix, i, j - 1, k - 1) + " " + INSERTION_23_NAME;
            case INSERTION_13:
                return getLongestPathFromBacktrack(backtrackMatrix, i - 1, j, k - 1) + " " + INSERTION_13_NAME;
            case INSERTION_12:
                return getLongestPathFromBacktrack(backtrackMatrix, i - 1, j - 1, k) + " " + INSERTION_12_NAME;
            default:
                return "";
        }
    }

    public static List<String> getAlignmentFromPath(String path, String v, String w, String x) {
        String vAlign, wAlign, xAlign;
        vAlign = wAlign = xAlign = "";
        List<String> actions = Arrays.asList(path.split("\\s+"));
        for (String action : actions) {
            switch (action) {
                case "MAT":
                    vAlign += v.charAt(0);
                    wAlign += w.charAt(0);
                    xAlign += x.charAt(0);
                    v = v.substring(1);
                    w = w.substring(1);
                    x = x.substring(1);
                    break;
                case "INS_1":
                    vAlign += v.charAt(0);
                    wAlign += "-";
                    xAlign += "-";
                    v = v.substring(1);
                    break;
                case "INS_2":
                    vAlign += "-";
                    wAlign += w.charAt(0);
                    xAlign += "-";
                    w = w.substring(1);
                    break;
                case "INS_3":
                    vAlign += "-";
                    wAlign += "-";
                    xAlign += x.charAt(0);
                    x = x.substring(1);
                    break;
                case "INS_23":
                    vAlign += "-";
                    wAlign += w.charAt(0);
                    xAlign += x.charAt(0);
                    w = w.substring(1);
                    x = x.substring(1);
                    break;
                case "INS_13":
                    vAlign += v.charAt(0);
                    wAlign += "-";
                    xAlign += x.charAt(0);
                    v = v.substring(1);
                    x = x.substring(1);
                    break;
                case "INS_12":
                    vAlign += v.charAt(0);
                    wAlign += w.charAt(0);
                    xAlign += "-";
                    v = v.substring(1);
                    w = w.substring(1);
                    break;
            }
        }
        return Arrays.asList(vAlign, wAlign, xAlign);
    }

    /**
     * Prints a 3D backtrack matrix as a series of matrices.
     * @param matrix    matrix to print
     */
    public static void printMatrix(Action[][][] matrix) {
        int numMats = matrix.length;
        int numRows = matrix[0].length;
        int numCols = matrix[0][0].length;
        for (int i = 0; i < numMats; i++) {
            for (int j = 0; j < numRows; j++) {
                for (int k = 0; k < numCols; k++) {
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
            String x = br.readLine();

            Action[][][] backtrackMatrix = getBacktrackMatrix(v, w, x);

//            printMatrix(backtrackMatrix);

            int score = getScore(v, w, x);
            String path = getLongestPathFromBacktrack(backtrackMatrix, v.length(), w.length(), x.length());
            List<String> alignment = getAlignmentFromPath(path, v, w, x);
//            System.out.println(getLongestPathFromBacktrack(backtrackMatrix, v.length(), w.length(), x.length()));

            return ConsoleCapturer.toString(score + "\n" + alignment.stream().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_251_5.txt");
        System.out.println(result);
    }
}
