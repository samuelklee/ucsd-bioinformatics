package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class ManhattanTouristDiagonal {
    /**
     * Returns the length of the longest path from node (0, 0) to node (n, m).
     * @param n                 n + 1 is number of node rows
     * @param m                 m + 1 is number of node columns
     * @param downMatrix        n x m + 1 matrix; downMatrix[i][j] is length of vertical edge exiting node (i, j)
     * @param rightMatrix       n + 1 x m matrix; rightMatrix[i][j] is length of horizontal edge exiting node (i, j)
     * @param diagonalMatrix    n + 1 x m matrix; rightMatrix[i][j] is length of diagonal edge exiting node (i, j)
     * @return                  length of longest path to node (n, m)
     */
    public static int getLongestPathLength(int n, int m,
                                           int[][] downMatrix, int[][] rightMatrix, int[][] diagonalMatrix) {
        int lengthMatrix[][] = new int[n + 1][m + 1]; //lengthMatrix[i][j] = longest path length to node (i, j).
        lengthMatrix[0][0] = 0;
        //initialize edges
        for (int i = 1; i <= n; i++) {
            lengthMatrix[i][0] = lengthMatrix[i - 1][0] + downMatrix[i - 1][0];
        }
        for (int j = 1; j <= m; j++) {
            lengthMatrix[0][j] = lengthMatrix[0][j - 1] + rightMatrix[0][j - 1];
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                lengthMatrix[i][j] = Collections.max(Arrays.asList(
                        lengthMatrix[i - 1][j] + downMatrix[i - 1][j],
                        lengthMatrix[i][j - 1] + rightMatrix[i][j - 1],
                        lengthMatrix[i - 1][j - 1] + diagonalMatrix[i - 1][j - 1]));
            }
        }
        return lengthMatrix[n][m];
    }

    /**
     * Reads an n x m matrix from a BufferedReader.
     * @param br    BufferedReader to read from
     * @param n     number of rows in matrix
     * @param m     number of columns in matrix
     * @return      n x m matrix
     */
    public static int[][] readMatrix(BufferedReader br, int n, int m) {
        int[][] matrix = new int[n][m];
        try {
            for (int i = 0; i < n; i++) {
                String[] row = br.readLine().split(" ");
                for (int j = 0; j < m; j++) {
                    matrix[i][j] = Integer.parseInt(row[j]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return matrix;
    }

    /**
     * Prints a matrix.
     * @param matrix    matrix to print
     */
    public static void printMatrix(int[][] matrix) {
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
            String[] inputIntegerStrings = br.readLine().split(" ");

            int n = Integer.parseInt(inputIntegerStrings[0]);
            int m = Integer.parseInt(inputIntegerStrings[1]);

            int[][] downMatrix = readMatrix(br, n, m + 1);

            String dash = br.readLine();
            if (!dash.equals("-")) {
                throw new RuntimeException("Matrix not loaded correctly.");
            }

            int[][] rightMatrix = readMatrix(br, n + 1, m);

            dash = br.readLine();
            if (!dash.equals("-")) {
                throw new RuntimeException("Matrix not loaded correctly.");
            }

            int[][] diagonalMatrix = readMatrix(br, n, m);

            int longestPathLength = getLongestPathLength(n, m, downMatrix, rightMatrix, diagonalMatrix);

            return ConsoleCapturer.toString(longestPathLength);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/sample/ManhattanTouristDiagonal.txt");
        System.out.println(result);
    }
}
