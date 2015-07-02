package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class MaximalNonBranchingPaths {
    public static Map<Object, Integer> getInCount(Map<Object, List<Object>> graph) {
        //map of nodes -> ingoing edge count; count occurrences of key in values for each key in graph
        return graph.values().stream().flatMap(l -> l.stream())
                .collect(Collectors.groupingBy(v -> v, Collectors.counting())).entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().intValue()));
    }

    public static Map<Object, Integer> getOutCount(Map<Object, List<Object>> graph) {
        //map of nodes -> outgoing edge count; count number of values for each key in graph
        return graph.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().size()));
    }

    /**
     * Return set of non-branching paths and isolated cycles in a graph.
     * @param graph     adjacency list for nodes in graph
     * @return          set of non-branching paths and isolated cycles
     */
    public static List<List<Object>> getPaths(Map<Object, List<Object>> graph) {
        List<List<Object>> paths = new ArrayList<>();

        Set<Object> nodes = graph.keySet();
        Map<Object, Integer> inCount = getInCount(graph);
        Map<Object, Integer> outCount = getOutCount(graph);

        Set<Object> oneInOneOutNodes = new HashSet<>(nodes);

        for (Object node : nodes) {
            if (!(inCount.getOrDefault(node, 0) == 1 && outCount.getOrDefault(node, 0) == 1)) {
                oneInOneOutNodes.remove(node);
                if (outCount.getOrDefault(node, 0) > 0) {
                    for (Object nextNode : graph.get(node)) {
                        List<Object> nonBranchingPath = new ArrayList<>(Arrays.asList(node, nextNode));
                        while (inCount.getOrDefault(nextNode, 0) == 1 && outCount.getOrDefault(nextNode, 0) == 1) {
                            Object nextNextNode = graph.get(nextNode).get(0);
                            nonBranchingPath.add(nextNextNode);
                            nextNode = nextNextNode;
                        }
                        paths.add(nonBranchingPath);
                    }
                }
            }
        }

        for (Object node : new HashSet<>(oneInOneOutNodes)) {
            Object nextNode = graph.get(node).get(0);
            List<Object> isolatedCycle = new ArrayList<>(Arrays.asList(node));
            while (oneInOneOutNodes.contains(nextNode)) {
                if (nextNode.equals(node)) {
                    isolatedCycle.add(node);
                    paths.add(isolatedCycle);
                    break;
                }
//                oneInOneOutNodes.remove(nextNode);
                isolatedCycle.add(nextNode);
                nextNode = graph.get(nextNode).get(0);
            }
        }

        return paths;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        Map<Object, List<Object>> graph = new HashMap<>();

        Object edgeInputString;
        while ((edgeInputString = br.readLine()) != null) {
            Object[] nodeStrings = edgeInputString.toString().split(" -> ");
            Object node1 = nodeStrings[0];
            List<Object> node2 = new ArrayList<>(Arrays.asList(nodeStrings[1].toString().split(",")));
            graph.put(node1, node2);
        }

        List<List<Object>> paths = getPaths(graph);

        for (List<Object> path : paths) {
            System.out.println(path.stream().map(o -> o.toString()).collect(Collectors.joining(" -> ")));
        }

    }
}
