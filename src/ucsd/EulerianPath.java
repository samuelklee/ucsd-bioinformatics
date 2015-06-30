package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class EulerianPath {
    public static String getTail(Map<String, List<String>> graph) {
        //tail is unique string in union of keys and values in graph
        List<String> findTail = new ArrayList<>(graph.keySet());
        findTail.addAll(graph.values().stream().flatMap(l -> l.stream()).collect(Collectors.toSet()));
        String tail = findTail.stream().reduce((a, b) -> new BigInteger(a).xor(new BigInteger(b)).toString())
                .orElse(null);

        if (tail.equals("0")) {
            if (!graph.values().contains("0")) {
                return null;
            }
        }

        return tail;
    }

    public static String getHead(Map<String, List<String>> graph) {
        //map of nodes -> outgoing edge count; count number of values for each key in graph
        Map<String, Integer> outCount = graph.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size()));
        //map of nodes -> ingoing edge count; count occurrences of key in values for each key in graph
        Map<String, Long> inCount = graph.values().stream().flatMap(l -> l.stream())
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

        for (String node : outCount.keySet()) {
            if (outCount.get(node) != (long) inCount.getOrDefault(node, (long) 0)) {
                return node;
            }
        }

        return null;
    }

    public static Map<String, List<String>> balance(Map<String, List<String>> graph){
        Map<String, List<String>> balancedGraph = new HashMap<>(graph);
        String tail = getTail(graph);
        String head = getHead(graph);

        if (tail == null) {
            return balancedGraph;
        }

        if (balancedGraph.containsKey(tail)) {
            balancedGraph.get(tail).add(head);
        } else {
            balancedGraph.put(tail, Arrays.asList(head));
        }
        return balancedGraph;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        Map<String, List<String>> graph = new HashMap<>();

        String edgeInputString;
        while ((edgeInputString = br.readLine()) != null) {
            String[] nodeStrings = edgeInputString.split(" -> ");
            String node1 = nodeStrings[0];
            List<String> node2 = new ArrayList<>(Arrays.asList(nodeStrings[1].split(",")));
            graph.put(node1, node2);
        }

        Map<String, List<String>> balancedGraph = balance(graph);
        String startNode = getTail(graph);

        EulerianCycle cycle = new EulerianCycle(balancedGraph);

        List<String> nodesVisited = cycle.getNodesVisited(startNode);
        nodesVisited.add(startNode);
        nodesVisited.remove(0);

        System.out.println(nodesVisited.stream().collect(Collectors.joining("->")));
    }
}
