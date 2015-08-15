package ucsd.III;

import org.apache.commons.lang3.StringUtils;
import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LinearSpaceAlignment {
    private enum Action {
        INSERTION, DELETION, MATCH
    }

    private static class Node {
        private int row;
        private Action action;

        public Node(int row, Action action) {
            this.row = row;
            this.action = action;
        }
    }

    public static Action getPreviousActionFromPreviousColumn(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                                             String v, String w,
                                                             int[] previousColumnLengthsFromSource, int previousColumnIndex,
                                                             int rowIndex) {
        int[] columnLengths = new int[v.length() + 1];
        if (rowIndex == 0) {
            return Action.INSERTION;
        }
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
                    case 2: throw new RuntimeException("Middle edge should not be a DELETION.");
                }
            }
        }
        return null;
    }

    public static Node getMiddleNode(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                     String v, String w,
                                     int top, int bottom, int left, int right) {
        String vCut = v.substring(top, bottom);
        String wCut = w.substring(left, right);

        int middleColumnIndex = wCut.length() / 2;
        int[] middleColumnLengthsFromSource = MiddleEdge.getColumnLengthsFromSource(scoringMatrix, indelPenalty, vCut, wCut, middleColumnIndex);
        int[] nextToMiddleColumnLengths = MiddleEdge.getColumnLengths(scoringMatrix, indelPenalty, vCut, wCut, middleColumnIndex + 1);
        int nextToMiddleNodeMaxRowIndex = MiddleEdge.getMaxNodeRowIndex(nextToMiddleColumnLengths);

        Action middleEdgeAction = getPreviousActionFromPreviousColumn(scoringMatrix, indelPenalty, vCut, wCut,
                middleColumnLengthsFromSource, middleColumnIndex, nextToMiddleNodeMaxRowIndex);

        int middleNodeRowIndex = nextToMiddleNodeMaxRowIndex;
        if (middleEdgeAction == Action.MATCH) {
            middleNodeRowIndex--;
        }

        middleNodeRowIndex += top;

        return new Node(middleNodeRowIndex, middleEdgeAction);
    }

    public static String getLongestPath(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty, String v, String w,
                                      int top, int bottom, int left, int right) {
        if (left == right) {
            if (bottom == top) {
                return "";
            }
            return StringUtils.repeat("DELETION", " ", bottom - top) + " ";
        }
        if (top == bottom) {
            if (left == right) {
                return "";
            }
            return StringUtils.repeat("INSERTION", " ", right - left) + " ";
        }

        int middle = (left + right) / 2;
        Node middleNode = getMiddleNode(scoringMatrix, indelPenalty, v, w, top, bottom, left, right);
        int middleNodeRow = middleNode.row;
        System.out.println(middleNodeRow + " " + middle + " " + middleNode.action);
        StringBuilder pathStringBuilder = new StringBuilder();
        pathStringBuilder.append(getLongestPath(scoringMatrix, indelPenalty, v, w, top, middleNodeRow, left, middle));
        pathStringBuilder.append(middleNode.action.toString() + " ");
//        String path = getLongestPath(scoringMatrix, indelPenalty, v, w, top, middleNodeRow, left, middle);
//        path += middleNode.action.toString() + " ";
        if (middleNode.action == Action.INSERTION || middleNode.action == Action.MATCH) {
            middle = middle + 1;
        }
        if (middleNode.action == Action.MATCH) {
            middleNodeRow = middleNodeRow + 1;
        }
        pathStringBuilder.append(getLongestPath(scoringMatrix, indelPenalty, v, w,
                middleNodeRow, bottom, middle, right));
//        path += getLongestPath(scoringMatrix, indelPenalty, v, w, middleNodeRow, bottom, middle, right);
        return pathStringBuilder.toString();
    }

    public static List<String> getAlignmentFromPath(String path, String v, String w) {
        String vAlign, wAlign;
        vAlign = wAlign = "";
        List<String> actions = Arrays.asList(path.split("\\s+"));
        for (String action : actions) {
            switch (action) {
                case "MATCH":
                    vAlign += v.charAt(0);
                    wAlign += w.charAt(0);
                    v = v.substring(1);
                    w = w.substring(1);
                    break;
                case "INSERTION":
                    vAlign += "-";
                    wAlign += w.charAt(0);
                    w = w.substring(1);
                    break;
                case "DELETION":
                    vAlign += v.charAt(0);
                    wAlign += "-";
                    v = v.substring(1);
                    break;
            }
        }
        return Arrays.asList(vAlign, wAlign);
    }

    public static int getLengthFromPath(Map<Map.Entry, Integer> scoringMatrix, int indelPenalty,
                                        String path, String v, String w) {
        int length, i, j;
        length = i = j = 0;
        List<String> actions = Arrays.asList(path.split("\\s+"));
        for (String action : actions) {
            switch (action) {
                case "MATCH":
                    length += scoringMatrix.get(new AbstractMap.SimpleEntry(
                            v.charAt(i), w.charAt(j)));
                    i++;
                    j++;
                    break;
                case "INSERTION":
                    length -= indelPenalty;
                    j++;
                    break;
                case "DELETION":
                    length -= indelPenalty;
                    i++;
                    break;
            }
        }
        return length;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String v = br.readLine();
            String w = br.readLine();

            Map<Map.Entry, Integer> scoringMatrix = DataTableUtils
                    .getScoringMatrix("/home/slee/working/ucsd-bioinformatics/src/test/resources/BLOSUM62.txt");

            int indelPenalty = 5;

            String path = getLongestPath(scoringMatrix, indelPenalty, v, w, 0, v.length(), 0, w.length());

            int length = getLengthFromPath(scoringMatrix, indelPenalty, path, v, w);
            List<String> alignment = getAlignmentFromPath(path, v, w);
//            System.out.println(path);

            return ConsoleCapturer.toString(length + "\n" + alignment.stream().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_250_14.txt");
        System.out.println(result);
    }
}
