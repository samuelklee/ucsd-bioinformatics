package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TwoBreakOnGenomeGraph {
    public static List<Map.Entry<Integer, Integer>> sortEdges(List<Map.Entry<Integer, Integer>> edges) {
        List < Map.Entry < Integer, Integer >> sortedEdges = new ArrayList<>(edges);
        Collections.sort(sortedEdges, (edge1, edge2) ->
                Math.min(edge1.getKey(), edge1.getValue()) - Math.min(edge2.getKey(), edge2.getValue()));
        return sortedEdges;
    }

    public static List<Map.Entry<Integer, Integer>> doTwoBreak(List<Map.Entry<Integer, Integer>> edges,
                                                               List<Integer> twoBreak) {
        List<Map.Entry<Integer, Integer>> edgesAfterTwoBreak = new ArrayList<>(edges);
        List<Set<Integer>> edgesAsSets = edges.stream().map(p -> new HashSet<>(Arrays.asList(p.getKey(), p.getValue())))
                .collect(Collectors.toList());

        int twoBreak1Min = Math.min(twoBreak.get(0), twoBreak.get(1));
        int twoBreak2Min = Math.min(twoBreak.get(2), twoBreak.get(3));

        int index1 = edgesAsSets.indexOf(new HashSet<>(Arrays.asList(twoBreak.get(0), twoBreak.get(1))));
        int index2 = edgesAsSets.indexOf(new HashSet<>(Arrays.asList(twoBreak.get(2), twoBreak.get(3))));

        int minIndex = Math.min(index1, index2);
        int maxIndex = Math.max(index1, index2);

        if (twoBreak1Min < twoBreak2Min) {
            edgesAfterTwoBreak.set(minIndex, new AbstractMap.SimpleEntry<>(twoBreak.get(0), twoBreak.get(2)));
            edgesAfterTwoBreak.set(maxIndex, new AbstractMap.SimpleEntry<>(twoBreak.get(1), twoBreak.get(3)));
        } else {
            edgesAfterTwoBreak.set(maxIndex, new AbstractMap.SimpleEntry<>(twoBreak.get(0), twoBreak.get(2)));
            edgesAfterTwoBreak.set(minIndex, new AbstractMap.SimpleEntry<>(twoBreak.get(1), twoBreak.get(3)));
        }

//        System.out.println(edgesAfterTwoBreak);

        List<Map.Entry<Integer, Integer>> edgesAfterTwoBreakSorted = new ArrayList<>();

        edgesAfterTwoBreakSorted.addAll(edgesAfterTwoBreak.subList(0, minIndex + 1));
        edgesAfterTwoBreakSorted.addAll(edgesAfterTwoBreak.subList(maxIndex + 1, edges.size()));
        List<Map.Entry<Integer, Integer>> edgesBetweenTwoBreak = edgesAfterTwoBreak.subList(minIndex + 1, maxIndex);
        edgesAfterTwoBreakSorted.addAll(edgesBetweenTwoBreak);
        edgesAfterTwoBreakSorted.add(edgesAfterTwoBreak.get(maxIndex));

        return edgesAfterTwoBreakSorted;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            String[] edgesStrings = br.readLine().split("\\), \\(");
            List<Map.Entry<Integer, Integer>> edges = Arrays.stream(edgesStrings)
                    .map(s -> s.replaceAll("\\(|\\)", "").split(", "))
                    .map(a -> new AbstractMap.SimpleEntry<>(Integer.valueOf(a[0]), Integer.valueOf(a[1])))
                    .collect(Collectors.toList());

            List<Integer> twoBreak = Arrays.stream(br.readLine().split(", ")).map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<Map.Entry<Integer, Integer>> edgesAfterTwoBreak = doTwoBreak(edges, twoBreak);

            String result = edgesAfterTwoBreak.stream().map(p -> "(" + p.getKey() + ", " + p.getValue() + ")")
                    .collect(Collectors.joining(", "));
            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_8224_2.txt");
        System.out.println(result);
    }
}
