package ucsd.IV;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SmallParsimony {
    public static List<List<Double>> readMatrix(BufferedReader br) {
        List<List<Double>> matrix = new ArrayList<>();
        String inputString;
        try {
            while ((inputString = br.readLine()) != null) {
                matrix.add(Arrays.asList(inputString.split("\\s+")).stream().map(Double::parseDouble)
                        .collect(Collectors.toList()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
        return matrix;
    }

    public static String getPhylogenyAsString(Map<Integer, Map<Integer, Double>> phylogeny) {
        StringBuilder phylogenyStringBuilder = new StringBuilder();
        for (int origin : phylogeny.keySet()) {
            Map<Integer, Double> edges = phylogeny.get(origin);
            for (int destination : edges.keySet()) {
                double weight = edges.get(destination);
                phylogenyStringBuilder.append(origin).append("->").append(destination).append(":")
                        .append(String.format("%.3f", weight)).append("\n");
            }
        }
        return phylogenyStringBuilder.toString();
    }

    public static void addEdge(Map<Integer, Map<Integer, Double>> phylogeny, int origin, int destination, double weight) {
        System.out.println("Adding edge from " + origin + " to " + destination + " with weight " + weight);
        if (!phylogeny.containsKey(origin)) {
            phylogeny.put(origin, new HashMap<>());
        }
        phylogeny.get(origin).put(destination, weight);
    }

    public static double getTotalDistance(List<List<Double>> distanceMatrix, int i) {
        return distanceMatrix.get(i).stream().mapToDouble(Double::doubleValue).sum();
    }

    public static List<List<Double>> getNeighborJoiningMatrix(List<List<Double>> distanceMatrix) {
        int n = distanceMatrix.size();
        List<List<Double>> neighborJoiningMatrix = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            neighborJoiningMatrix.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    neighborJoiningMatrix.get(i).add(0.);
                } else {
                    neighborJoiningMatrix.get(i).add((n - 2) * distanceMatrix.get(i).get(j) - getTotalDistance(distanceMatrix, i) - getTotalDistance(distanceMatrix, j));
                }
            }
        }
        return neighborJoiningMatrix;
    }

    public static AbstractMap.SimpleEntry<Integer, Integer> findNeighbors(List<List<Double>> neighborJoiningMatrix) {
        double min = 100000.;
        AbstractMap.SimpleEntry<Integer, Integer> closestClusters = new AbstractMap.SimpleEntry<>(0, 0);
        for (int i = 0; i < neighborJoiningMatrix.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (neighborJoiningMatrix.get(i).get(j) < min) {
                    min = neighborJoiningMatrix.get(i).get(j);
                    closestClusters = new AbstractMap.SimpleEntry<>(i, j);
                }
            }
        }
        return closestClusters;
    }

    public static void updateDistanceMatrix(List<List<Double>> distanceMatrix, int i, int j) {
        List<Double> newRow = new ArrayList<>();
        for (int m = 0; m < distanceMatrix.size(); m++) {
            final double distance = 0.5 * (distanceMatrix.get(i).get(m) + distanceMatrix.get(j).get(m) - distanceMatrix.get(i).get(j));
            newRow.add(distance);
        }
        newRow.add(0.);
        distanceMatrix.add(newRow);
        for (int m = 0; m < distanceMatrix.size() - 1; m++) {
            distanceMatrix.get(m).add(distanceMatrix.get(distanceMatrix.size() - 1).get(m));
        }
        distanceMatrix.remove(Math.min(i, j));
        distanceMatrix.remove(Math.max(i, j) - 1);
        for (List<Double> row : distanceMatrix) {
            row.remove(Math.min(i, j));
            row.remove(Math.max(i, j) - 1);
        }
    }

    public static String getMatrixAsString(List<List<Double>> matrix) {
        return matrix.stream().map(l -> l.stream().map(Object::toString).collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));
    }

    public static Map<Integer, Map<Integer, Double>> getPhylogeny(List<List<Double>> distanceMatrix, int n, List<Integer> leaves, int numLeaves) {
        Map<Integer, Map<Integer, Double>> phylogeny = new HashMap<>();
        if (n == 2) {
            int destination = numLeaves + 1;
            double weight = distanceMatrix.get(0).get(1);
            addEdge(phylogeny, numLeaves, destination, weight);
            addEdge(phylogeny, destination, numLeaves, weight);
            return phylogeny;
        }
//        System.out.println(getMatrixAsString(distanceMatrix));
//        System.out.println();
        List<List<Double>> neighborJoiningMatrix = getNeighborJoiningMatrix(distanceMatrix);
//        System.out.println(getMatrixAsString(neighborJoiningMatrix));
//        System.out.println();
        AbstractMap.SimpleEntry<Integer, Integer> neighbors = findNeighbors(neighborJoiningMatrix);
        int i = neighbors.getKey();
        int j = neighbors.getValue();
        int iName = leaves.get(i);
        int jName = leaves.get(j);
        System.out.println("Neighbors " + iName + " and " + jName + " found.\n");
        leaves.remove(i);
        leaves.remove(j);
        double delta = (getTotalDistance(distanceMatrix, i) - getTotalDistance(distanceMatrix, j)) / (n - 2);
        double limbLengthi = 0.5 * (distanceMatrix.get(i).get(j) + delta);
        double limbLengthj = 0.5 * (distanceMatrix.get(i).get(j) - delta);
        updateDistanceMatrix(distanceMatrix, i, j);
        int newNode = numLeaves + n - 3;
        leaves.add(newNode);
        phylogeny = getPhylogeny(distanceMatrix, n - 1, leaves, numLeaves);
        System.out.println("new " + newNode);
        addEdge(phylogeny, iName, newNode, limbLengthi);
        addEdge(phylogeny, newNode, iName, limbLengthi);
        addEdge(phylogeny, jName, newNode, limbLengthj);
        addEdge(phylogeny, newNode, jName, limbLengthj);
        return phylogeny;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int n = Integer.parseInt(br.readLine());

            List<List<Double>> distanceMatrix = readMatrix(br);
            Map<Integer, Map<Integer, Double>> phylogeny = getPhylogeny(distanceMatrix, n, new ArrayList<>(IntStream.range(0, n).boxed().collect(Collectors.toList())), n);
            String result = getPhylogenyAsString(phylogeny);
            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10333_6.txt");
        System.out.println(result);
    }
}
