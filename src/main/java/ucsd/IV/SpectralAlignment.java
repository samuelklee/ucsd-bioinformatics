package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpectralAlignment {
    public static int[][][] getScoreMatrix(String peptide, List<Integer> spectralVector, int maxNumModifications, List<Integer> diff) {
        int massPlusDelta = spectralVector.size();

        int[][][] scoreMatrix = new int[peptide.length() + 1][massPlusDelta + 1][maxNumModifications + 1];

        //initialize edges
        for (int i = 0; i <= peptide.length(); i++) {
            for (int j = 0; j <= massPlusDelta; j++) {
                for (int k = 0; k < maxNumModifications + 1; k++) {
                    scoreMatrix[i][j][k] = -100000;
                }
            }
        }
        scoreMatrix[0][0][0] = 0;
        for (int i = 1; i <= peptide.length(); i++) {
            for (int j = 1; j <= massPlusDelta; j++) {
                for (int k = 0; k < maxNumModifications + 1; k++) {
                    int iIdx = i;
                    int kIdx = k;
                    if (j >= diff.get(i)) {
                        List<Integer> scores = new ArrayList<>();
                        if (k > 0) {
                            scores = IntStream.range(0, j).boxed()
                                    .map(jp -> scoreMatrix[iIdx - 1][jp][kIdx - 1])
                                    .collect(Collectors.toList());
                        }
                        scores.add(0, scoreMatrix[i - 1][j - diff.get(i)][k]);
                        scoreMatrix[i][j][k] = spectralVector.get(j - 1) + Collections.max(scores);
                    } else {
                        scoreMatrix[i][j][k] = -100000;
                    }
                }
            }
        }
        return scoreMatrix;
    }

    public static int getScore(String peptide, List<Integer> spectralVector, int maxNumModifications, List<Integer> diff) {
        int massPlusDelta = spectralVector.size();
        int[][][] scoreMatrix = getScoreMatrix(peptide, spectralVector, maxNumModifications, diff);
        return Collections.max(IntStream.range(0, maxNumModifications + 1).boxed()
                .map(k -> scoreMatrix[peptide.length()][massPlusDelta][k]).collect(Collectors.toList()));
    }

    public static int[][][] getBacktrackMatrix(String peptide, List<Integer> spectralVector, int maxNumModifications, List<Integer> diff) {
        int massPlusDelta = spectralVector.size();

        int[][][] backtrackMatrix = new int[peptide.length() + 1][massPlusDelta + 1][maxNumModifications + 1];
        int[][][] scoreMatrix = new int[peptide.length() + 1][massPlusDelta + 1][maxNumModifications + 1];

        //initialize edges
        for (int i = 0; i <= peptide.length(); i++) {
            for (int j = 0; j <= massPlusDelta; j++) {
                for (int k = 0; k < maxNumModifications + 1; k++) {
                    scoreMatrix[i][j][k] = -100000;
                }
            }
        }
        scoreMatrix[0][0][0] = 0;
        for (int i = 1; i <= peptide.length(); i++) {
            for (int j = 1; j <= massPlusDelta; j++) {
                for (int k = 0; k < maxNumModifications + 1; k++) {
                    int iIdx = i;
                    int kIdx = k;
                    if (j >= diff.get(i)) {
                        List<Integer> scores = new ArrayList<>();
                        if (k > 0) {
                            scores = IntStream.range(0, j).boxed()
                                    .map(jp -> scoreMatrix[iIdx - 1][jp][kIdx - 1])
                                    .collect(Collectors.toList());
                        }
                        scores.add(0, scoreMatrix[i - 1][j - diff.get(i)][k]);
                        scoreMatrix[i][j][k] = spectralVector.get(j - 1) + Collections.max(scores);
                        backtrackMatrix[i][j][k] = IntStream.range(0, scores.size()).boxed()
                                .max(Comparator.comparing(scores::get)).get();
                    } else {
                        scoreMatrix[i][j][k] = -100000;
                        backtrackMatrix[i][j][k] = -1;
                    }
                }
            }
        }
        return backtrackMatrix;
    }

    public static void getPathFromBacktrack(int[][][] backtrackMatrix, int i, int j, int k, List<Integer> diff, List<Integer> path) {
        if (i <= 0 && j == 0 && k == 0) {
            return;
        }
        int action = backtrackMatrix[i][j][k];
        if (action == 0) {
            getPathFromBacktrack(backtrackMatrix, i - 1, j - diff.get(i), k, diff, path);
            path.add(0);
        } else {
            getPathFromBacktrack(backtrackMatrix, i - 1, action - 1, k - 1, diff, path);
            path.add(j - (action - 1) - diff.get(i));
        }
    }

    public static String getCharacterModification(int action, String character) {
        if (action == 0) {
            return character;
        }
        if (action < 0) {
            return character + "(" + action + ")";
        }
        return character + "(+" + action + ")";
    }

    public static String getAlignmentFromPath(List<Integer> path, String peptide) {
        return IntStream.range(0, path.size()).boxed().map(i -> getCharacterModification(path.get(i), peptide.substring(i, i + 1))).collect(Collectors.joining(""));
    }

    /**
     * Prints a 3D backtrack matrix as a series of matrices.
     * @param matrix    matrix to print
     */
    public static void printMatrix(int[][][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;
        int numMats = matrix[0][0].length;
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

    public static String doWork(String dataFileName, Map<String, Integer> aminoMassTable) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            String peptide = br.readLine();
            List<Integer> spectralVector = Arrays.asList(br.readLine().split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());
//            System.out.println(spectralVector);
            int maxNumModifications = Integer.parseInt(br.readLine());

            List<Integer> diff = Arrays.stream(peptide.split("")).map(aminoMassTable::get).collect(Collectors.toList());
            diff.add(0, 0);

            int[][][] backtrackMatrix = getBacktrackMatrix(peptide, spectralVector, maxNumModifications, diff);

//            printMatrix(backtrackMatrix);
//            System.out.println("score");
//            printMatrix(getScoreMatrix(peptide, spectralVector, maxNumModifications, diff));

            int score = getScore(peptide, spectralVector, maxNumModifications, diff);
//            System.out.println(score);
            List<Integer> path = new ArrayList<>();
            getPathFromBacktrack(backtrackMatrix, peptide.length(), spectralVector.size(), maxNumModifications, diff, path);
//            System.out.println(path);
            String alignment = getAlignmentFromPath(path, peptide);
//            System.out.println(alignment);
            return ConsoleCapturer.toString(alignment);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTable();
        String result = doWork("src/test/resources/IV/dataset_11866_14.txt", aminoMassTable);
        System.out.println(result);
    }
}
