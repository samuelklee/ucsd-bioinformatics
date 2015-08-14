package ucsd.III;

import org.apache.commons.lang3.ArrayUtils;
import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MiddleEdge {
    private enum Action {
        INSERTION, MATCH
    }

    public static int[] getColumnLengthsFromPreviousColumn(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                                           String v, String w,
                                                           int[] previousColumnLengths, int previousColumnIndex) {
        int[] columnLengths = new int[v.length() + 1];
        columnLengths[0] = previousColumnLengths[0] - indelPenalty;
        for (int i = 1; i <= v.length(); i++) {
            int matchScore = scoringMatrix.get(new AbstractMap.SimpleEntry(
                    v.charAt(i - 1), w.charAt(previousColumnIndex)));
            columnLengths[i] = Collections.max(Arrays.asList(
                    previousColumnLengths[i] - indelPenalty,
                    columnLengths[i - 1] - indelPenalty,
                    previousColumnLengths[i - 1] + matchScore));
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
            columnLengths = getColumnLengthsFromPreviousColumn(scoringMatrix, indelPenalty, v, w,
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

    public static int getNodeRowIndex(int[] columnLengths) {
        return IntStream.range(0, columnLengths.length).boxed().max(Comparator.comparing(i -> columnLengths[i])).get();
    }



//    public static Action getMiddleNodeAction(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
//                                             int middleNodeRow, int middleNodeLength,
//                                             String v, String w) {
//        int middleColumn = w.length() / 2;
//
//    }

    public static int getNodeAfterMiddleNodeRow(int middleNodeRow, Action middleNodeAction) {
        switch (middleNodeAction) {
            case INSERTION:
                return middleNodeRow;
            case MATCH:
                return middleNodeRow + 1;
            default:
                throw new RuntimeException("Middle edge should not have DELETION action.");
        }
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
            int[] middleColumnLengths = getColumnLengths(scoringMatrix, indelPenalty, v, w, middleColumnIndex);
            int middleNodeRowIndex = getNodeRowIndex(middleColumnLengths);
            int[] nextToMiddleColumnLengths = getColumnLengthsFromPreviousColumn(scoringMatrix, indelPenalty, v, w,
                    middleColumnLengths, middleColumnIndex);
            int nextToMiddleNodeRowIndex = getNodeRowIndex(nextToMiddleColumnLengths);

//            Action middleNodeAction = getMiddleNodeAction(scoringMatrix, indelPenalty,
//                    middleNodeRow, middleNodeLength, v, w);
//            int nodeAfterMiddleNodeRow = getNodeAfterMiddleNodeRow(middleNodeRow, middleNodeAction);

            return ConsoleCapturer.toString("(" + middleNodeRowIndex + ", " + middleColumnIndex + ") (" +
                    nextToMiddleNodeRowIndex + ", " + (middleColumnIndex + 1) + ")");
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_250_12.txt");
        System.out.println(result);
    }
}
