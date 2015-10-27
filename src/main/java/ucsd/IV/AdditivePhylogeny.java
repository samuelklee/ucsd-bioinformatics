package ucsd.IV;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AdditivePhylogeny {
    public static List<List<Integer>> readMatrix(BufferedReader br) {
        List<List<Integer>> matrix = new ArrayList<>();
        String inputString;
        try {
            while ((inputString = br.readLine()) != null) {
                matrix.add(Arrays.asList(inputString.split(" ")).stream().map(Integer::parseInt)
                        .collect(Collectors.toList()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
        return matrix;
    }

    public static void addEdge(Map<Integer, Map<Integer, Integer>> phylogeny, int origin, int destination, int weight) {
        if (!phylogeny.containsKey(origin)) {
            phylogeny.put(origin, new HashMap<>());
        }
        phylogeny.get(origin).put(destination, weight);
    }

    public static List<Integer> getNodesOnPath(Map<Integer, Map<Integer, Integer>> phylogeny, int i, int k, int numLeaves, Set<Integer> visitedNodes) {
        if (phylogeny.get(i).containsKey(k)) {
            return new ArrayList<>(Arrays.asList(i, k));
        }
        List<Integer> unvisitedNodes = phylogeny.get(i).keySet().stream()
                .filter(n -> n >= numLeaves && !visitedNodes.contains(n)).collect(Collectors.toList());
        if (unvisitedNodes.size() == 0) {
            return new ArrayList<>();
        }
        visitedNodes.add(i);
        List<Integer> nodesOnPath = new ArrayList<>(Collections.singletonList(i));
        for (int node : unvisitedNodes) {
            nodesOnPath.addAll(getNodesOnPath(phylogeny, node, k, numLeaves, visitedNodes));
            if (nodesOnPath.get(nodesOnPath.size() - 1) == k) {
                break;
            } else {
                nodesOnPath = new ArrayList<>(Collections.singletonList(i));
            }
        }
        return nodesOnPath;
    }

    //add leaf with limbLength at distance x from leaf i on path between leaves i and k, creating new internal node if needed
    public static void addLeaf(Map<Integer, Map<Integer, Integer>> phylogeny, int i, int k, int x, int leaf, int limbLength,
                               int numLeaves) {
        List<Integer> nodesOnPath = getNodesOnPath(phylogeny, i, k, numLeaves, new HashSet<>());
        System.out.println("Path: " + nodesOnPath);
        int distanceRemaining = x;
        int origin = i;
        for (int destination : nodesOnPath.subList(1, nodesOnPath.size())) {
            distanceRemaining -= phylogeny.get(origin).get(destination);
            if (distanceRemaining == 0) {
                addEdge(phylogeny, leaf, destination, limbLength);
                addEdge(phylogeny, destination, leaf, limbLength);
                break;
            }
            if (distanceRemaining < 0) {
                int weight = phylogeny.get(origin).get(destination);
                int originToNewNodeDistance = distanceRemaining + weight;
                int destinationToNewNodeDistance = -distanceRemaining;
                int maxNode = Collections.max(phylogeny.keySet());
                int newNode = maxNode < numLeaves ? numLeaves : maxNode + 1;
                System.out.println("Adding " + leaf + " with limb " + limbLength + " at node " + newNode + " between " + origin + " and " + destination);
                System.out.println();
                phylogeny.get(origin).remove(destination);
                phylogeny.get(destination).remove(origin);
                addEdge(phylogeny, origin, newNode, originToNewNodeDistance);
                addEdge(phylogeny, newNode, origin, originToNewNodeDistance);
                addEdge(phylogeny, destination, newNode, destinationToNewNodeDistance);
                addEdge(phylogeny, newNode, destination, destinationToNewNodeDistance);
                addEdge(phylogeny, newNode, leaf, limbLength);
                addEdge(phylogeny, leaf, newNode, limbLength);
                break;
            }
            origin = destination;
        }
    }

    public static Map<Integer, Map<Integer, Integer>> getPhylogeny(List<List<Integer>> distanceMatrix, int n) {
        //modifies distanceMatrix
        Map<Integer, Map<Integer, Integer>> phylogeny = new HashMap<>();
        if (n == 2) {
            int origin = 0;
            int destination = 1;
            int weight = distanceMatrix.get(origin).get(destination);
            addEdge(phylogeny, origin, destination, weight);
            addEdge(phylogeny, destination, origin, weight);
            return phylogeny;
        }
        int limbLength = LimbLength.getLimbLength(distanceMatrix, n - 1, n - 1);
        for (int j = 0; j < n - 1; j++) {
            int newDjn = distanceMatrix.get(j).get(n - 1) - limbLength;
            distanceMatrix.get(j).set(n - 1, newDjn);
            distanceMatrix.get(n - 1).set(j, newDjn);
        }
//        System.out.println(DistanceBetweenLeaves.getMatrixAsString(distanceMatrix));
//        System.out.println();
        int i;
        int k = 0;
        int x = 0;
        outerloop:
        for (i = 0; i < n - 1; i++) {
            for (k = 0; k < n - 1; k++) {
                if (i != k && distanceMatrix.get(i).get(k) == distanceMatrix.get(i).get(n - 1) + distanceMatrix.get(n - 1).get(k)) {
                    x = distanceMatrix.get(i).get(n - 1);
                    break outerloop;
                }
            }
        }
        phylogeny = getPhylogeny(distanceMatrix, n - 1);
        System.out.println(phylogeny);
        System.out.println("Adding node at distance " + x + " between " + i + " and " + k);
        addLeaf(phylogeny, i, k, x, n - 1, limbLength, distanceMatrix.size());
        return phylogeny;
    }

    public static String getPhylogenyAsString(Map<Integer, Map<Integer, Integer>> phylogeny) {
        StringBuilder phylogenyStringBuilder = new StringBuilder();
        for (int origin : phylogeny.keySet()) {
            Map<Integer, Integer> edges = phylogeny.get(origin);
            for (int destination : edges.keySet()) {
                int weight = edges.get(destination);
                phylogenyStringBuilder.append(origin).append("->").append(destination).append(":")
                        .append(weight).append("\n");
            }
        }
        return phylogenyStringBuilder.toString();
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int n = Integer.parseInt(br.readLine());

            List<List<Integer>> distanceMatrix = readMatrix(br);
//            System.out.println(DistanceBetweenLeaves.getMatrixAsString(distanceMatrix));
//            System.out.println();
            Map<Integer, Map<Integer, Integer>> phylogeny = getPhylogeny(distanceMatrix, n);
            String phylogenyString = getPhylogenyAsString(phylogeny);

            return ConsoleCapturer.toString(phylogenyString);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10330_6.txt");
        System.out.println(result);
    }
}
