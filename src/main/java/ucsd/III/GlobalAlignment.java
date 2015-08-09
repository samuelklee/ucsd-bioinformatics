package ucsd.III;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GlobalAlignment {
    private enum Action {
        INSERTION, DELETION, MATCH
    }

    public static Action[][] getBacktrackMatrix(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                                String v, String w) {
        Action[][] backtrackMatrix = new Action[v.length() + 1][w.length() + 1];
        int[][] lengthMatrix = new int[v.length() + 1][w.length() + 1];

        //initialize edges
        for (int i = 0; i <= v.length(); i++) {
            lengthMatrix[i][0] = -indelPenalty*i;
        }
        for (int j = 0; j <= w.length(); j++) {
            lengthMatrix[0][j] = -indelPenalty*j;
        }
        for (int i = 1; i <= v.length(); i++) {
            for (int j = 1; j <= w.length(); j++) {
                List<Integer> scores = Arrays.asList(
                        lengthMatrix[i - 1][j] - indelPenalty,
                        lengthMatrix[i][j - 1] - indelPenalty,
                        lengthMatrix[i - 1][j - 1]
                                + scoringMatrix.get(new AbstractMap.SimpleEntry(v.charAt(i - 1), w.charAt(j - 1))));
                lengthMatrix[i][j] = Collections.max(scores);
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

    public static int getScoreFromBacktrack(Action[][] backtrackMatrix, Map<Map.Entry, Integer> scoringMatrix,
                                            int indelPenalty, String v, String w, int i, int j) {
        if (i == 0 && j == 0) {
            return 0;
        }
        if (i == 0) {
            return getScoreFromBacktrack(backtrackMatrix, scoringMatrix, indelPenalty, v, w, i, j - 1) - indelPenalty*j;
        }
        if (j == 0) {
            return getScoreFromBacktrack(backtrackMatrix, scoringMatrix, indelPenalty, v, w, i - 1, j) - indelPenalty*i;
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
                    .getScoringMatrix("/home/slee/working/ucsd-bioinformatics/src/test/resources/BLOSUM62.txt");

            int indelPenalty = 5;

            Action[][] backtrackMatrix = getBacktrackMatrix(scoringMatrix, indelPenalty, v, w);

//            printMatrix(backtrackMatrix);
//            System.out.println(getActionsFromBacktrack(backtrackMatrix, v.length(), w.length()));

            int score = getScoreFromBacktrack(backtrackMatrix, scoringMatrix, indelPenalty,
                    v, w, v.length(), w.length());
            String vAlign = getAlignmentFromBacktrack(backtrackMatrix, v, v.length(), w.length(), true);
            String wAlign = getAlignmentFromBacktrack(backtrackMatrix, w, v.length(), w.length(), false);

            return ConsoleCapturer.toString(score + "\n" + vAlign + "\n" + wAlign);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_247_3.txt");
        System.out.println(result);
    }
}
