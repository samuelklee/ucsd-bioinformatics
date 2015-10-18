package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DistanceBetweenLeaves {
    public static Map<Integer, Map<Integer, Integer>> readWeightedGraph(BufferedReader br) {
        Map<Integer, Map<Integer, Integer>> weightedGraph = new HashMap<>();
        String edgeInputString;
        try {
            while ((edgeInputString = br.readLine()) != null) {
                String[] nodeStrings = edgeInputString.split("->");
                int node1 = Integer.parseInt(nodeStrings[0]);
                String[] node2AndWeightStrings = nodeStrings[1].split(":");
                int node2 = Integer.parseInt(node2AndWeightStrings[0]);
                int weight = Integer.parseInt(node2AndWeightStrings[1]);
                if (!weightedGraph.containsKey(node1)) {
                    weightedGraph.put(node1, new HashMap<>());
                }
                weightedGraph.get(node1).put(node2, weight);
            }
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
        return weightedGraph;
    }

    public static String matrixAsString(List<List<Integer>> matrix) {
        return matrix.stream().map(l -> l.stream().map(Object::toString).collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));
    }

    public static List<List<Integer>> zeroMatrix(int n) {
        List<List<Integer>> matrix = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            matrix.add(new ArrayList<>(Collections.nCopies(n, 0)));
        }
        return matrix;
    }

    public static List<List<Integer>> getFullDistanceMatrix(Map<Integer, Map<Integer, Integer>> weightedGraph) {
        Set<Integer> nodes = weightedGraph.keySet();
        List<List<Integer>> fullDistanceMatrix = zeroMatrix(nodes.size());

        for (int node : nodes) {
            Map<Integer, Integer> outgoingEdges = weightedGraph.get(node);
            for (int destination : outgoingEdges.keySet()) {
                int weight = outgoingEdges.get(destination);
                fullDistanceMatrix.get(node).set(destination, weight);
            }
        }
        return fullDistanceMatrix;
    }

    public static List<List<Integer>> getDistanceMatrix(Map<Integer, Map<Integer, Integer>> weightedGraph,
                                                        int n) {
        List<List<Integer>> fullDistanceMatrix = getFullDistanceMatrix(weightedGraph);
        
        List<Integer> leaves = IntStream.range(0, n).boxed().collect(Collectors.toList());
        Set<Integer> nodes = weightedGraph.keySet();
        Set<Integer> internalNodes = new HashSet<>(nodes);
        internalNodes.removeAll(leaves);
        for (int internalNode : internalNodes) {
            Set<Integer> destinations = IntStream.range(0, nodes.size()).boxed()
                    .filter(j -> fullDistanceMatrix.get(internalNode).get(j) != 0).collect(Collectors.toSet());
            for (int destination1 : destinations) {
                int weight1 = fullDistanceMatrix.get(internalNode).get(destination1);
                for (int destination2 : destinations) {
                    if (destination1 != destination2) {
//                        System.out.println(destination1 + " " + destination2);
                        int weight2 = fullDistanceMatrix.get(internalNode).get(destination2);
                        int currentDistance = fullDistanceMatrix.get(destination1).get(destination2);
                        if (currentDistance == 0) {
                            fullDistanceMatrix.get(destination1).set(destination2, weight1 + weight2);
                        } else {
                            fullDistanceMatrix.get(destination1).set(destination2, Math.min(currentDistance, weight1 + weight2));
                        }
//                        System.out.println(matrixAsString(fullDistanceMatrix));
//                        System.out.println();
                    }
                }
            }
        }
        return IntStream.range(0, n).mapToObj(i -> fullDistanceMatrix.get(i).subList(0, n)).collect(Collectors.toList());
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int n = Integer.parseInt(br.readLine());

            Map<Integer, Map<Integer, Integer>> weightedGraph = readWeightedGraph(br);

            List<List<Integer>> distanceMatrix = getDistanceMatrix(weightedGraph, n);
//            System.out.println(matrixAsString(getFullDistanceMatrix(weightedGraph)));
//            System.out.println();
            String result = matrixAsString(distanceMatrix);
//            System.out.println(result);
            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10328_11.txt");
        System.out.println(result);
    }
}
