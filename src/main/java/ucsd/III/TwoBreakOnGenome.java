package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TwoBreakOnGenome {
    public static List<List<Integer>> doTwoBreak(List<List<Integer>> genome, List<Integer> twoBreak) {
        List<Map.Entry<Integer, Integer>> edges = ColoredEdges.getEdges(genome);
//        System.out.println(edges);

        List<Map.Entry<Integer, Integer>> edgesAfterTwoBreak = TwoBreakOnGenomeGraph.doTwoBreak(edges, twoBreak);
//        System.out.println(edgesAfterTwoBreak);
        return GraphToGenome.getGenome(edgesAfterTwoBreak);
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            String[] genomeStrings = br.readLine().split("\\)\\(");
            List<List<Integer>> genome = Arrays.stream(genomeStrings)
                    .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split("\\s+")).map(Integer::parseInt)
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            List<Integer> twoBreak = Arrays.stream(br.readLine().split(", ")).map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<List<Integer>> genomeAfterTwoBreak = doTwoBreak(genome, twoBreak);

            String result = genomeAfterTwoBreak.stream()
                    .map(l -> "(" + l.stream().map(i -> String.format("%+d", i)).collect(Collectors.joining(" ")) + ")")
                    .collect(Collectors.joining(""));

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_8224_3.txt");
        System.out.println(result);
    }
}
