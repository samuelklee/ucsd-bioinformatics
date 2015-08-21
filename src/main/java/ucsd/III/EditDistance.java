package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EditDistance {
    private enum Action {
        INSERTION, DELETION, MATCH
    }

    public static int getEditDistance(String v, String w) {
        int[][] lengthMatrix = new int[v.length() + 1][w.length() + 1];

        //initialize edges
        for (int i = 0; i <= v.length(); i++) {
            lengthMatrix[i][0] = i;
        }
        for (int j = 0; j <= w.length(); j++) {
            lengthMatrix[0][j] = j;
        }
        for (int i = 1; i <= v.length(); i++) {
            for (int j = 1; j <= w.length(); j++) {
                List<Integer> scores = Arrays.asList(
                        lengthMatrix[i - 1][j] + 1,
                        lengthMatrix[i][j - 1] + 1,
                        v.charAt(i - 1) == w.charAt(j - 1) ?
                                lengthMatrix[i - 1][j - 1] : lengthMatrix[i - 1][j - 1] + 1);
                lengthMatrix[i][j] = Collections.min(scores);
            }
        }
        return lengthMatrix[v.length()][w.length()];
    }

    public static Action[][] getBacktrackMatrix(String v, String w) {
        Action[][] backtrackMatrix = new Action[v.length() + 1][w.length() + 1];
        int[][] lengthMatrix = new int[v.length() + 1][w.length() + 1];

        //initialize edges
        for (int i = 0; i <= v.length(); i++) {
            lengthMatrix[i][0] = -i;
        }
        for (int j = 0; j <= w.length(); j++) {
            lengthMatrix[0][j] = -j;
        }
        for (int i = 1; i <= v.length(); i++) {
            for (int j = 1; j <= w.length(); j++) {
                List<Integer> scores = Arrays.asList(
                        lengthMatrix[i - 1][j] + 1,
                        lengthMatrix[i][j - 1] + 1,
                        v.charAt(i - 1) == w.charAt(j - 1) ?
                                lengthMatrix[i - 1][j - 1] : lengthMatrix[i - 1][j - 1] + 1);
                lengthMatrix[i][j] = Collections.min(scores);
                if (lengthMatrix[i][j] ==  scores.get(0)) {
                    backtrackMatrix[i][j] = Action.DELETION;
                } else if (lengthMatrix[i][j] == scores.get(1)) {
                    backtrackMatrix[i][j] = Action.INSERTION;
                } else if (lengthMatrix[i][j] == scores.get(2)) {
                    backtrackMatrix[i][j] = Action.MATCH;
                }
            }
        }
        return backtrackMatrix;
    }

    public static String getActionsFromBacktrack(Action[][] backtrackMatrix, int i, int j) {
        if (i == 0 & j != 0) {
            return getActionsFromBacktrack(backtrackMatrix, i, j - 1) + " INS ";
        }
        if (i != 0 & j == 0) {
            return getActionsFromBacktrack(backtrackMatrix, i - 1, j) + " DEL ";
        }
        if (i != 0 && j != 0) {
            Action action = backtrackMatrix[i][j];
            switch (action) {
                case DELETION:
                    return getActionsFromBacktrack(backtrackMatrix, i - 1, j) + " DEL ";
                case INSERTION:
                    return getActionsFromBacktrack(backtrackMatrix, i, j - 1) + " INS ";
                case MATCH:
                    return getActionsFromBacktrack(backtrackMatrix, i - 1, j - 1) + " MAT ";
            }
        }
        return "";
    }

    public static int getEditDistanceFromBacktrack(Action[][] backtrackMatrix, String v, String w, int i, int j) {
        if (i == 0 && j == 0) {
            return 0;
        }
        if (i == 0) {
            return j;
        }
        if (j == 0) {
            return i;
        }
        Action action = backtrackMatrix[i][j];
        switch (action) {
            case DELETION:
                return getEditDistanceFromBacktrack(backtrackMatrix, v, w, i - 1, j) + 1;
            case INSERTION:
                return getEditDistanceFromBacktrack(backtrackMatrix, v, w, i, j - 1) + 1;
            case MATCH:
                return getEditDistanceFromBacktrack(backtrackMatrix, v, w, i - 1, j - 1) +
                        v.charAt(i -1) == w.charAt(j - 1) ? 0 : 1;
            default: return 0;
        }
    }

    public static String getAlignmentFromBacktrack(Action[][] backtrackMatrix, String s, int i, int j,
                                                   boolean isReference) {
        if (isReference) {
            if (i == 0 & j != 0) {
                return getAlignmentFromBacktrack(backtrackMatrix, s, i, j - 1, isReference) + "-";
            }
            if (i != 0 & j == 0) {
                return getAlignmentFromBacktrack(backtrackMatrix, s, i - 1, j, isReference) + s.charAt(i - 1);
            }
            if (i != 0 && j != 0) {
                Action action = backtrackMatrix[i][j];
                switch (action) {
                    case DELETION:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i - 1, j, isReference) + s.charAt(i - 1);
                    case INSERTION:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i, j - 1, isReference) + "-";
                    case MATCH:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i - 1, j - 1, isReference) + s.charAt(i - 1);
                }
            }
        } else {
            if (i == 0 & j != 0) {
                return getAlignmentFromBacktrack(backtrackMatrix, s, i, j - 1, isReference) + s.charAt(j - 1);
            }
            if (i != 0 & j == 0) {
                return getAlignmentFromBacktrack(backtrackMatrix, s, i - 1, j, isReference) + "-";
            }
            if (i != 0 && j != 0) {
                Action action = backtrackMatrix[i][j];
                switch (action) {
                    case DELETION:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i - 1, j, isReference) + "-";
                    case INSERTION:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i, j - 1, isReference) + s.charAt(j - 1);
                    case MATCH:
                        return getAlignmentFromBacktrack(backtrackMatrix, s, i - 1, j - 1, isReference) + s.charAt(j - 1);
                }
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

//            Action[][] backtrackMatrix = getBacktrackMatrix(v, w);
//
//            printMatrix(backtrackMatrix);
//            System.out.println(getActionsFromBacktrack(backtrackMatrix, v.length(), w.length()));
//
//            System.out.println(getAlignmentFromBacktrack(backtrackMatrix, v, v.length(), w.length(), true));
//            System.out.println(getAlignmentFromBacktrack(backtrackMatrix, w, v.length(), w.length(), false));
//
//            int score = getEditDistanceFromBacktrack(backtrackMatrix, v, w, v.length(), w.length());

            int score = getEditDistance(v, w);

            return ConsoleCapturer.toString(score);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_248_3.txt");
        System.out.println(result);
    }
}
