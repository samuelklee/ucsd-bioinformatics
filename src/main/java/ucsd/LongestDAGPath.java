package ucsd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LongestDAGPath {
    private static final int LARGE_NEGATIVE_NUMBER = -100000;

    public static Map<Object, Map<Object, Integer>> readWeightedGraph(String dataFileName) {
        Map<Object, Map<Object, Integer>> weightedGraph = new HashMap<>();

        String edgeInputString;

        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            br.readLine();
            br.readLine();
            while ((edgeInputString = br.readLine()) != null) {
                String[] nodeStrings = edgeInputString.split("->");
                String origin = nodeStrings[0];
                String[] weightedEdgeStrings = nodeStrings[1].split(":");
                String destination = weightedEdgeStrings[0];
                int weight = Integer.parseInt(weightedEdgeStrings[1]);
                if (!weightedGraph.containsKey(origin)) {
                    weightedGraph.put(origin, new HashMap<>());
                }
                weightedGraph.get(origin).put(destination, weight);
            }
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
        return weightedGraph;
    }

    public static Map<Object, List<Object>> getGraphFromWeightedGraph(Map<Object, Map<Object, Integer>> weightedGraph) {
        Map<Object, List<Object>> graph = new HashMap<>();
        for (Object node : weightedGraph.keySet()) {
            List<Object> children = new ArrayList<>(weightedGraph.get(node).keySet());
            graph.put(node, children);
        }
        return graph;
    }

    public static Map<Object, List<Object>> getBacktrackGraph(Map<Object, Map<Object, Integer>> weightedGraph,
                                                              Object source, Object sink) {
        Map<Object, List<Object>> backtrackGraph = new HashMap<>();
        Map<Object, Integer> lengthMap = new HashMap<>();

        //get all nodes from graph
        Set<Object> nodes = new HashSet<>();
        nodes.addAll(weightedGraph.keySet());
        for (Object node : weightedGraph.keySet()) {
            nodes.addAll(weightedGraph.get(node).keySet());
        }

        for (Object node : nodes) {
            lengthMap.put(node, LARGE_NEGATIVE_NUMBER);
        }
        lengthMap.put(source, 0);

        List<Object> ordering = TopologicalOrdering.getOrdering(getGraphFromWeightedGraph(weightedGraph));
        List<Object> truncatedOrdering = ordering.subList(ordering.indexOf(source),
                Math.min(ordering.indexOf(sink) + 1, ordering.size()));

//        System.out.println(truncatedOrdering.stream().map(s -> s.toString()).collect(Collectors.joining("->")));

        //make weighted graph with reversed edges using only non-truncated nodes
        Map<Object, Map<Object, Integer>> truncatedReversedWeightedGraph = new HashMap<>();
        for (Object node : truncatedOrdering) {
            if (!weightedGraph.containsKey(node)) {
                continue;
            }
            Map<Object, Integer> weightedChildren = weightedGraph.get(node);
            for (Object destination : weightedChildren.keySet()) {
                if (!truncatedReversedWeightedGraph.keySet().contains(destination)) {
                    truncatedReversedWeightedGraph.put(destination, new HashMap<>());
                }
                truncatedReversedWeightedGraph.get(destination).put(node, weightedChildren.get(destination));
            }
        }

//        System.out.println("TRUNCATED REVERSED");
//        printWeightedGraph(truncatedReversedWeightedGraph);

        //drop edges from truncated reversed weighted graph that don't give longest path
        for (Object node : truncatedOrdering) {
            if (!truncatedReversedWeightedGraph.containsKey(node)) {
                continue;
            }
            Map<Object, Integer> weightedParents = truncatedReversedWeightedGraph.get(node);
            Object parentLongestLength = weightedParents.keySet().stream()
                    .max(Comparator.comparing(p -> weightedParents.get(p) + lengthMap.get(p))).get();
            int longestLength = weightedParents.get(parentLongestLength) + lengthMap.get(parentLongestLength);
            if (longestLength >= 0) {
                for (Object parent : weightedParents.keySet()) {
                    if (weightedParents.get(parent) + lengthMap.get(parent) == longestLength) {
                        lengthMap.put(node, longestLength);
                        if (!backtrackGraph.containsKey(node)) {
                            backtrackGraph.put(node, new ArrayList<>());
                        }
                        backtrackGraph.get(node).add(parent);
                    }
                }
            }
        }
        return backtrackGraph;
    }

    public static int getLength(List<Object> path, Map<Object, Map<Object, Integer>> weightedGraph) {
        int length = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Object origin = path.get(i);
            Object destination = path.get(i + 1);
            length += weightedGraph.get(origin).get(destination);
        }
        return length;
    }

    public static void printGraph(Map<Object, List<Object>> graph) {
        for (Object node : graph.keySet()) {
            for (Object child : graph.get(node)) {
                System.out.println(node.toString() + "->" + child.toString());
            }
        }
    }

    public static void printWeightedGraph(Map<Object, Map<Object, Integer>> weightedGraph) {
        for (Object node : weightedGraph.keySet()) {
            for (Object child : weightedGraph.get(node).keySet()) {
                System.out.println(node.toString() + "->" + child.toString() + ":"
                        + weightedGraph.get(node).get(child).toString());
            }
        }
    }
    public static List<Object> getPathFromBacktrack(Map<Object, List<Object>> backtrackGraph,
                                                    Object source, Object sink) {
        if (!backtrackGraph.containsKey(sink)) {
            return new ArrayList(Arrays.asList(sink));
        }
        for (Object parent : backtrackGraph.get(sink)) {
            List<Object> path = getPathFromBacktrack(backtrackGraph, source, parent);
            path.add(sink);
            if (path.get(0).equals(source)) {
                return path;
            }
        }
        throw new RuntimeException("No path found.");
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String source = br.readLine();
            String sink = br.readLine();

            Map<Object, Map<Object, Integer>> weightedGraph = readWeightedGraph(dataFileName);

//            printGraph(getGraphFromWeightedGraph(weightedGraph));

            Map<Object, List<Object>> backtrackGraph = getBacktrackGraph(weightedGraph, source, sink);

//            System.out.println("BACKTRACK");
//            printGraph(backtrackGraph);

            List<Object> path = getPathFromBacktrack(backtrackGraph, source, sink);

//            System.out.println(path.stream().map(s -> s.toString()).collect(Collectors.joining("->")));

            int length = getLength(path, weightedGraph);

            return ConsoleCapturer.toString(length + "\n" +
                    path.stream().map(s -> s.toString()).collect(Collectors.joining("->")));
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }

    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_245_7.txt");
        System.out.println(result);
    }
}
