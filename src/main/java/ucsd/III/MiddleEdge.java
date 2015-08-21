package ucsd.III;

import org.apache.commons.lang3.ArrayUtils;
import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

public class MiddleEdge {
    private enum Action {
        INSERTION, MATCH
    }

    public static Action getPreviousActionFromPreviousColumn(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                                             String v, String w,
                                                             int[] previousColumnLengthsFromSource, int previousColumnIndex,
                                                             int rowIndex) {
        int[] columnLengths = new int[v.length() + 1];
        columnLengths[0] = previousColumnLengthsFromSource[0] - indelPenalty;
        for (int i = 1; i <= v.length(); i++) {
            int matchScore = scoringMatrix.get(new AbstractMap.SimpleEntry(
                    v.charAt(i - 1), w.charAt(previousColumnIndex)));
            List<Integer> newLengths = Arrays.asList(
                    previousColumnLengthsFromSource[i - 1] + matchScore,
                    previousColumnLengthsFromSource[i] - indelPenalty,
                    columnLengths[i - 1] - indelPenalty);
            int indexOfLongest = IntStream.range(0, newLengths.size()).boxed()
                    .max(Comparator.comparing(n -> newLengths.get(n))).get();
            columnLengths[i] = newLengths.get(indexOfLongest);
            if (i == rowIndex) {
                switch (indexOfLongest) {
                    case 0: return Action.MATCH;
                    case 1: return Action.INSERTION;
                }
            }
        }
        return null;
    }

    public static int[] getColumnLengthsToSourceFromPreviousColumn(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                                                   String v, String w,
                                                                   int[] previousColumnLengthsFromSource, int previousColumnIndex) {
        int[] columnLengths = new int[v.length() + 1];
        columnLengths[0] = previousColumnLengthsFromSource[0] - indelPenalty;
        for (int i = 1; i <= v.length(); i++) {
            int matchScore = scoringMatrix.get(new AbstractMap.SimpleEntry(
                    v.charAt(i - 1), w.charAt(previousColumnIndex)));
            columnLengths[i] = Collections.max(Arrays.asList(
                    previousColumnLengthsFromSource[i - 1] + matchScore,
                    previousColumnLengthsFromSource[i] - indelPenalty,
                    columnLengths[i - 1] - indelPenalty));
        }
        return columnLengths;
    }

    public static int[] getColumnLengthsFromSource(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                                   String v, String w, int columnIndex) {
        int[] previousColumnLengths = new int[v.length() + 1];
        for (int i = 0; i <= v.length(); i++) {
            previousColumnLengths[i] = -indelPenalty*i;
        }
        int[] columnLengths = previousColumnLengths;
        for (int j = 1; j <= columnIndex; j++) {
            columnLengths = getColumnLengthsToSourceFromPreviousColumn(scoringMatrix, indelPenalty, v, w,
                    previousColumnLengths, j - 1);
            previousColumnLengths = columnLengths;
        }
        return columnLengths;
    }

    public static int[] getColumnLengthsToSink(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                                 String v, String w, int columnIndex) {
        String vReverse = new StringBuilder(v).reverse().toString();
        String wReverse = new StringBuilder(w).reverse().toString();
        int[] columnLengths = getColumnLengthsFromSource(scoringMatrix, indelPenalty, vReverse, wReverse,
                w.length() - columnIndex);
        ArrayUtils.reverse(columnLengths);
        return columnLengths;
    }

    public static int[] getColumnLengths(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                         String v, String w, int columnIndex) {
        int[] columnLengths = new int[v.length() + 1];
        int[] columnLengthsFromSource = getColumnLengthsFromSource(scoringMatrix, indelPenalty, v, w, columnIndex);
        int[] columnLengthsToSink = getColumnLengthsToSink(scoringMatrix, indelPenalty, v, w, columnIndex);
        for (int i = 0; i <= v.length(); i++) {
            columnLengths[i] = columnLengthsFromSource[i] + columnLengthsToSink[i];
        }
        return columnLengths;
    }

    public static int getMaxNodeRowIndex(int[] columnLengths) {
        return IntStream.range(0, columnLengths.length).boxed().max(Comparator.comparing(i -> columnLengths[i])).get();
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String v = br.readLine();
            String w = br.readLine();

            Map<Map.Entry, Integer> scoringMatrix = DataTableUtils
                    .getScoringMatrix("/home/slee/working/ucsd-bioinformatics/src/test/resources/BLOSUM62.txt");

            int indelPenalty = 5;

//            for (int j = 0; j <= w.length(); j++) {
//                int[] column = getColumnLengthsFromSource(scoringMatrix, indelPenalty, v, w, j);
//                System.out.println(Arrays.stream(column).mapToObj(Integer::toString).collect(Collectors.joining(" ")));
//            }
//            System.out.println();
//            for (int j = 0; j <= w.length(); j++) {
//                int[] column = getColumnLengthsToSink(scoringMatrix, indelPenalty, v, w, j);
//                System.out.println(Arrays.stream(column).mapToObj(Integer::toString).collect(Collectors.joining(" ")));
//            }
//            System.out.println();
//            for (int j = 0; j <= w.length(); j++) {
//                int[] column = getColumnLengths(scoringMatrix, indelPenalty, v, w, j);
//                System.out.println(Arrays.stream(column).mapToObj(Integer::toString).collect(Collectors.joining(" ")));
//            }

            int middleColumnIndex = w.length() / 2;
            int[] middleColumnLengthsFromSource = getColumnLengthsFromSource(scoringMatrix, indelPenalty, v, w, middleColumnIndex);
            int[] nextToMiddleColumnLengths = getColumnLengths(scoringMatrix, indelPenalty, v, w, middleColumnIndex + 1);
            int nextToMiddleNodeMaxRowIndex = getMaxNodeRowIndex(nextToMiddleColumnLengths);

            Action middleEdgeAction = getPreviousActionFromPreviousColumn(scoringMatrix, indelPenalty, v, w,
                    middleColumnLengthsFromSource, middleColumnIndex, nextToMiddleNodeMaxRowIndex);

            int middleNodeRowIndex = nextToMiddleNodeMaxRowIndex;
            if (middleEdgeAction == Action.MATCH) {
                middleNodeRowIndex = nextToMiddleNodeMaxRowIndex - 1;
            }

            return ConsoleCapturer.toString("(" + middleNodeRowIndex + ", " + middleColumnIndex + ") (" +
                    nextToMiddleNodeMaxRowIndex + ", " + (middleColumnIndex + 1) + ")");
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_250_12.txt");
        System.out.println(result);
    }
}
