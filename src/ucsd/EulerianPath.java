package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class EulerianPath {
    public static Object getTail(Map<Object, List<Object>> graph) {
        //tail is unique string in union of keys and values in graph
//        List<String> findTail = new ArrayList<>(graph.keySet());
//        findTail.addAll(graph.values().stream().flatMap(l -> l.stream()).collect(Collectors.toSet()));
//        String tail = findTail.stream().reduce((a, b) -> new BigInteger(a).xor(new BigInteger(b)).toString())
//                .orElse(null);

//        if (tail.equals("0")) {
//            if (!graph.values().contains("0")) {
//                return null;
//            }
//        }

        List<Object> findTail = graph.values().stream().flatMap(l -> l.stream()).collect(Collectors.toList());
        findTail.removeAll(graph.keySet());

        if (findTail.isEmpty()) {
            return null;
        }

        return findTail.get(0);
    }

    public static Object getHead(Map<Object, List<Object>> graph) {
        //map of nodes -> outgoing edge count; count number of values for each key in graph
        Map<Object, Integer> outCount = graph.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size()));
        //map of nodes -> ingoing edge count; count occurrences of key in values for each key in graph
        Map<Object, Long> inCount = graph.values().stream().flatMap(l -> l.stream())
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

        for (Object node : outCount.keySet()) {
            if (outCount.get(node) != (long) inCount.getOrDefault(node, (long) 0)) {
                return node;
            }
        }

        return null;
    }

    public static Map<Object, List<Object>> balance(Map<Object, List<Object>> graph){
        Map<Object, List<Object>> balancedGraph = new HashMap<>(graph);
        Object tail = getTail(graph);
        Object head = getHead(graph);

//        System.out.println(tail + " " + head);

        if (tail == null || head == null) {
            return balancedGraph;
        }

        if (balancedGraph.containsKey(tail)) {
            balancedGraph.get(tail).add(head);
        } else {
            balancedGraph.put(tail, Arrays.asList(head));
        }
        return balancedGraph;
    }

    /**
     * Balance graph and get Eulerian path.
     * @param graph unbalanced graph with Eulerian path
     * @return      list of nodes visited
     */
    public static LinkedList<Object> getNodesVisited(Map<Object, List<Object>> graph) {
        Map<Object, List<Object>> balancedGraph = balance(graph);

        Object startNode;
        if (balancedGraph.equals(graph)) {
            startNode = graph.keySet().toArray()[0];
        } else {
            startNode = getTail(graph);
        }

        EulerianCycle cycle = new EulerianCycle(balancedGraph);

        LinkedList<Object> nodesVisited = cycle.getNodesVisited(startNode);
        nodesVisited.add(startNode);
        nodesVisited.remove(0);

        return nodesVisited;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        Map<Object, List<Object>> graph = new HashMap<>();

        String edgeInputString;
        while ((edgeInputString = br.readLine()) != null) {
            String[] nodeStrings = edgeInputString.split(" -> ");
            String node1 = nodeStrings[0];
            List<Object> node2 = new ArrayList<>(Arrays.asList(nodeStrings[1].split(",")));
            graph.put(node1, node2);
        }

        LinkedList<Object> nodesVisited = getNodesVisited(graph);

        System.out.println(nodesVisited.stream().map(o -> o.toString()).collect(Collectors.joining("->")));
    }
}
