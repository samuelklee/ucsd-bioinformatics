package ucsd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TopologicalOrdering {
    public static Map<Object, List<Object>> readGraph(String dataFileName) {
        Map<Object, List<Object>> graph = new HashMap<>();

        String edgeInputString;

        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            while ((edgeInputString = br.readLine()) != null) {
                String[] nodeStrings = edgeInputString.split(" -> ");
                String node1 = nodeStrings[0];
                List<Object> node2 = new ArrayList<>(Arrays.asList(nodeStrings[1].split(",")));
                graph.put(node1, node2);
            }
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
        return graph;
    }

    public static Map<Object, Integer> getInCount(Map<Object, List<Object>> graph) {
        return graph.values().stream().flatMap(l -> l.stream())
                .collect(Collectors.groupingBy(v -> v, Collectors.counting())).entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().intValue()));
    }
    public static List<Object> getHeads(Map<Object, List<Object>> graph) {
        //map of nodes -> ingoing edge count; count occurrences of key in values for each key in graph
        List<Object> children = new ArrayList<>(getInCount(graph).keySet());

        return graph.keySet().stream().filter(o -> !children.contains(o)).collect(Collectors.toList());
    }

    public static List<Object> getOrdering(Map<Object, List<Object>> graph) {
        Map<Object, List<Object>> graphCopy = new HashMap<>(graph);
        List<Object> ordering = new ArrayList<>();
        List<Object> candidates = getHeads(graphCopy);

        while (!candidates.isEmpty()) {
            Object node = candidates.remove(0);
            ordering.add(node);
            candidates.remove(node);

            List<Object> childrenOfNode = graphCopy.getOrDefault(node, new ArrayList<>());
            graphCopy.remove(node);
            List<Object> allRemainingChildren = new ArrayList<>(getInCount(graphCopy).keySet());
            if (!childrenOfNode.isEmpty()) {
                childrenOfNode.removeAll(allRemainingChildren);
                candidates.addAll(childrenOfNode);
            }
        }

        if (!graphCopy.isEmpty()) {
            throw new RuntimeException("The graph is not a directed acyclic graph.");
        }

        return ordering;
    }

    public static String doWork(String dataFileName) {
        Map<Object, List<Object>> graph = readGraph(dataFileName);

        List<Object> ordering = getOrdering(graph);

        return ConsoleCapturer.toString(ordering.stream().map(o -> o.toString()).collect(Collectors.joining(", ")));
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_254_2.txt");
        System.out.println(result);
    }
}
