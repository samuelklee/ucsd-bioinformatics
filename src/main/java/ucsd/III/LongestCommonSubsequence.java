package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class LongestCommonSubsequence {
    private enum Action {
        INSERTION, DELETION, MATCH
    }

    public static Action[][] getBacktrackMatrix(String v, String w) {
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
                lengthMatrix[i][j] = Collections.max(Arrays.asList(
                        lengthMatrix[i - 1][j],
                        lengthMatrix[i][j - 1],
                        v.charAt(i - 1) == w.charAt(j - 1) ? lengthMatrix[i - 1][j - 1] + 1 : 0));
                if (lengthMatrix[i][j] ==  lengthMatrix[i - 1][j]) {
                    backtrackMatrix[i][j] = Action.DELETION;
                } else if (lengthMatrix[i][j] == lengthMatrix[i][j - 1]) {
                    backtrackMatrix[i][j] = Action.INSERTION;
                } else if (lengthMatrix[i][j] == lengthMatrix[i - 1][j - 1] + 1 && v.charAt(i - 1) == w.charAt(j - 1)) {
                    backtrackMatrix[i][j] = Action.MATCH;
                }
            }
        }
        return backtrackMatrix;
    }

    public static String getLCSFromBacktrack(Action[][] backtrackMatrix, String v, int i, int j) {
        if (i != 0 && j != 0) {
            Action action = backtrackMatrix[i][j];
            switch (action) {
                case DELETION:
                    return getLCSFromBacktrack(backtrackMatrix, v, i - 1, j);
                case INSERTION:
                    return getLCSFromBacktrack(backtrackMatrix, v, i, j - 1);
                case MATCH:
                    return getLCSFromBacktrack(backtrackMatrix, v, i - 1, j - 1) + v.charAt(i - 1);
            }
        }
        return "";
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

            Action[][] backtrackMatrix = getBacktrackMatrix(v, w);

//            printMatrix(backtrackMatrix);

            String lcs = getLCSFromBacktrack(backtrackMatrix, v, v.length(), w.length());

            return ConsoleCapturer.toString(lcs);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III//sample/MultipleLongestCommonSubsequence.txt"); //dataset_245_5.txt");
        System.out.println(result);
    }
}
