package ucsd.IV;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UnweightedPairGroupMethodWithArithmeticMean {
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

    public static AbstractMap.SimpleEntry<Integer, Integer> findClosestClusters(List<List<Integer>> distanceMatrix) {
        int min = 100000;
        AbstractMap.SimpleEntry<Integer, Integer> closestClusters = new AbstractMap.SimpleEntry<>(0, 0);
        for (int i = 0; i < distanceMatrix.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (distanceMatrix.get(i).get(j) < min) {
                    min = distanceMatrix.get(i).get(j);
                    closestClusters = new AbstractMap.SimpleEntry<>(i, j);
                }
            }
        }
        return closestClusters;
    }

    public static void addEdge(Map<Integer, Map<Integer, Double>> phylogeny, int origin, int destination, double weight) {
        if (!phylogeny.containsKey(origin)) {
            phylogeny.put(origin, new HashMap<>());
        }
        phylogeny.get(origin).put(destination, weight);
    }

    public static Map<Integer, Map<Integer, Double>> getPhylogeny(List<List<Integer>> distanceMatrix, int n) {
        Map<Integer, Map<Integer, Double>> phylogeny = new HashMap<>();
        List<Integer> clusters = IntStream.range(0, n).boxed().collect(Collectors.toList());
        List<Integer> ages = Collections.nCopies(0, n);
        int nextInternalNode = n;
        while (clusters.size() > 0) {
            AbstractMap.SimpleEntry<Integer, Integer> closestClusters = findClosestClusters(distanceMatrix);
            int i = closestClusters.getKey();
            int j = closestClusters.getValue();
            double weight = distanceMatrix.get(i).get(j) / 2.;

        }
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int n = Integer.parseInt(br.readLine());

            List<List<Integer>> distanceMatrix = readMatrix(br);
            Map<Integer, Map<Integer, Double>> phylogeny = getPhylogeny(distanceMatrix, n);
            String result = getPhylogenyAsString(phylogeny);
            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10329_11.txt");
        System.out.println(result);
    }
}
