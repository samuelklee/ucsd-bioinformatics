package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ColoredEdges {
    public static List<Map.Entry<Integer, Integer>> getEdges(List<List<Integer>> genome) {
        List<Map.Entry<Integer, Integer>> edges = new ArrayList<>();
        for (List<Integer> chromosome : genome) {
            List<Integer> cycle = ChromosomeToCycle.getCycle(chromosome);
            cycle.add(cycle.get(0));
            for (int j = 0; j < chromosome.size(); j++) {
                edges.add(new AbstractMap.SimpleEntry<>(cycle.get(2*j + 1), cycle.get(2*j + 2)));
            }
        }
        return edges;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            String[] genomeStrings = br.readLine().split("\\)\\(");
            List<List<Integer>> genome = Arrays.stream(genomeStrings)
                    .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split("\\s+")).map(Integer::parseInt)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            List<Map.Entry<Integer, Integer>> edges = getEdges(genome);

            String result = edges.stream().map(p -> "(" + p.getKey() + ", " + p.getValue() + ")")
                    .collect(Collectors.joining(", "));
            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_8222_7.txt");
        System.out.println(result);
    }
}
