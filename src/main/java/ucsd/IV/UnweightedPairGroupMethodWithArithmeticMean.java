package ucsd.IV;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UnweightedPairGroupMethodWithArithmeticMean {
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

    public static AbstractMap.SimpleEntry<Integer, Integer> findClosestClusters(List<List<Double>> distanceMatrix, List<Integer> includedClusters) {
        double min = 100000.;
        AbstractMap.SimpleEntry<Integer, Integer> closestClusters = new AbstractMap.SimpleEntry<>(0, 0);
        for (int i = 0; i < distanceMatrix.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (distanceMatrix.get(i).get(j) < min && includedClusters.contains(i) && includedClusters.contains(j)) {
                    min = distanceMatrix.get(i).get(j);
                    closestClusters = new AbstractMap.SimpleEntry<>(i, j);
                }
            }
        }
        return closestClusters;
    }

    public static void addEdge(Map<Integer, Map<Integer, Double>> phylogeny, int origin, int destination, double weight) {
//        System.out.println("Adding edge from " + origin + " to " + destination + " with weight " + weight);
        if (!phylogeny.containsKey(origin)) {
            phylogeny.put(origin, new HashMap<>());
        }
        phylogeny.get(origin).put(destination, weight);
    }

    public static void addCluster(List<List<Double>> distanceMatrix, List<Integer> clusterSizes, int i, int j) {
        List<Double> newRow = new ArrayList<>();
        for (int m = 0; m < distanceMatrix.size(); m++) {
            final double distance = (distanceMatrix.get(i).get(m) * clusterSizes.get(i) + distanceMatrix.get(j).get(m) * clusterSizes.get(j)) / (clusterSizes.get(i) + clusterSizes.get(j));
            newRow.add(distance);
        }
        newRow.add(0.);
        distanceMatrix.add(newRow);
        for (int m = 0; m < distanceMatrix.size() - 1; m++) {
            distanceMatrix.get(m).add(distanceMatrix.get(distanceMatrix.size() - 1).get(m));
        }
    }

    public static String getMatrixAsString(List<List<Double>> matrix) {
        return matrix.stream().map(l -> l.stream().map(Object::toString).collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));
    }

    public static Map<Integer, Map<Integer, Double>> getPhylogeny(List<List<Double>> distanceMatrix, int n) {
        Map<Integer, Map<Integer, Double>> phylogeny = new HashMap<>();
        List<Integer> clusters = IntStream.range(0, n).boxed().collect(Collectors.toList());
        List<Integer> clusterSizes = new ArrayList<>(Collections.nCopies(n, 1));
        List<Double> ages = new ArrayList<>(Collections.nCopies(n, 0.));
        int internalNode = n;
        while (clusters.size() > 1) {
//            System.out.println(getMatrixAsString(distanceMatrix));
//            System.out.println();
            AbstractMap.SimpleEntry<Integer, Integer> closestClusters = findClosestClusters(distanceMatrix, clusters);
            int i = closestClusters.getKey();
            int j = closestClusters.getValue();
//            System.out.println("Merging clusters " + i + " and " + j + "\n");
            double weight = distanceMatrix.get(i).get(j) / 2.;
            ages.add(weight);
            addEdge(phylogeny, i, internalNode, Math.abs(ages.get(i) - ages.get(internalNode)));
            addEdge(phylogeny, internalNode, i, Math.abs(ages.get(i) - ages.get(internalNode)));
            addEdge(phylogeny, j, internalNode, Math.abs(ages.get(j) - ages.get(internalNode)));
            addEdge(phylogeny, internalNode, j, Math.abs(ages.get(j) - ages.get(internalNode)));
            clusters.add(internalNode);
            clusters.remove(new Integer(i));
            clusters.remove(new Integer(j));
            clusterSizes.add(clusterSizes.get(i) + clusterSizes.get(j));
            addCluster(distanceMatrix, clusterSizes, i, j);
            internalNode++;
        }
        return phylogeny;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int n = Integer.parseInt(br.readLine());

            List<List<Double>> distanceMatrix = readMatrix(br);
            Map<Integer, Map<Integer, Double>> phylogeny = getPhylogeny(distanceMatrix, n);
            String result = getPhylogenyAsString(phylogeny);

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10332_8.txt");
        System.out.println(result);
    }
}
