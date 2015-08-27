package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GraphToGenome {
    public static List<List<Integer>> getCycles(List<Map.Entry<Integer, Integer>> edges) {
        List<List<Integer>> cycles = new ArrayList<>();
        boolean isNewCycle = true;
        int firstElementOfCycle = -1;
        for (Map.Entry<Integer, Integer> edge : edges) {
            int first = edge.getKey();
            int second = edge.getValue();
            if (isNewCycle) {
                firstElementOfCycle = first;
                cycles.add(new ArrayList<>());
                isNewCycle = false;
            }
            cycles.get(cycles.size() - 1).add(first);
            if ((cycles.get(cycles.size() - 1).size() > 1 && (second == firstElementOfCycle + 1 || second == firstElementOfCycle - 1)) ||
                    (Math.max(first, second) == Math.min(first, second) + 1 && Math.max(first, second) % 2 == 0)) {
                cycles.get(cycles.size() - 1).add(0, second);
                isNewCycle = true;
            } else {
                cycles.get(cycles.size() - 1).add(second);
            }
        }
        return cycles;
    }

    public static List<List<Integer>> getGenome(List<Map.Entry<Integer, Integer>> edges) {
        List<List<Integer>> genome = new ArrayList<>();
        for (List<Integer> cycle : getCycles(edges)) {
            List<Integer> chromosome = CycleToChromosome.getChromosome(cycle);
            genome.add(chromosome);
        }
        return genome;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            String[] edgesStrings = br.readLine().split("\\), \\(");
            List<Map.Entry<Integer, Integer>> edges = Arrays.stream(edgesStrings)
                    .map(s -> s.replaceAll("\\(|\\)", "").split(", "))
                    .map(a -> new AbstractMap.SimpleEntry<>(Integer.valueOf(a[0]), Integer.valueOf(a[1])))
                    .collect(Collectors.toList());

            List<List<Integer>> genome = getGenome(edges);

            String result = genome.stream()
                    .map(l -> "(" + l.stream().map(i -> String.format("%+d", i)).collect(Collectors.joining(" ")) + ")")
                    .collect(Collectors.joining(""));
            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_8222_8.txt");
        System.out.println(result);
    }
}
