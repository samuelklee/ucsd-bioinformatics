package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TwoBreakSorting {
    public static Map.Entry<Integer, Integer> orderEdge(Map.Entry<Integer, Integer> edge) {
        return new AbstractMap.SimpleEntry<>(
                Math.min(edge.getKey(), edge.getValue()), Math.max(edge.getKey(), edge.getValue()));
    }

    public static List<Map.Entry<Integer, Integer>> orderEdges(List<Map.Entry<Integer, Integer>> edges) {
        return edges.stream().map(TwoBreakSorting::orderEdge).collect(Collectors.toList());
    }

    public static List<List<List<Integer>>> getSortOrder(List<List<Integer>> genomeP, List<List<Integer>> genomeQ) {
        List<List<List<Integer>>> sortOrder = new ArrayList<>(Arrays.asList(genomeP));

        List<Map.Entry<Integer, Integer>> edgesP = orderEdges(ColoredEdges.getEdges(genomeP));
        List<Map.Entry<Integer, Integer>> edgesQ = orderEdges(ColoredEdges.getEdges(genomeQ));

        List<List<Integer>> cycles = TwoBreakDistance.getCycles(edgesP, edgesQ);
        List<List<Integer>> nonTrivialCycles = cycles.stream().filter(l -> l.size() > 2).collect(Collectors.toList());

        List<Integer> twoBreak;
        List<List<Integer>> genomePSorted = new ArrayList<>(genomeP);

        while (nonTrivialCycles.size() > 0) {
            List<Integer> cycle = nonTrivialCycles.get(0);
            int i, j, iPrime, jPrime;
            if (edgesQ.contains(orderEdge(new AbstractMap.SimpleEntry<>(cycle.get(1), cycle.get(2))))) {
                i = cycle.get(0);
                j = cycle.get(1);
                iPrime = cycle.get(2);
                jPrime = cycle.get(3);
            } else if (edgesQ.contains(orderEdge(new AbstractMap.SimpleEntry<>(cycle.get(0), cycle.get(1))))) {
                i = cycle.get(cycle.size() - 1);
                j = cycle.get(0);
                iPrime = cycle.get(1);
                jPrime = cycle.get(2);
            } else {
                throw new RuntimeException("Cannot find Q edge in non-trivial cycle.");
            }

            twoBreak = Arrays.asList(i, j, jPrime, iPrime);
            edgesP = TwoBreakOnGenomeGraph.doTwoBreak(edgesP, twoBreak);

            cycles = TwoBreakDistance.getCycles(edgesP, edgesQ);
            nonTrivialCycles = cycles.stream().filter(l -> l.size() > 2).collect(Collectors.toList());

            List<List<Integer>> genome = GraphToGenome.getGenome(edgesP);
            sortOrder.add(genome);
        }

        return sortOrder;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            String genomePString = br.readLine();
            String genomeQString = br.readLine();

            List<List<Integer>> genomeP = TwoBreakDistance.genomeFromString(genomePString);
            List<List<Integer>> genomeQ = TwoBreakDistance.genomeFromString(genomeQString);

            List<List<List<Integer>>> sortOrder = getSortOrder(genomeP, genomeQ);

            String result = sortOrder.stream()
                    .map(g -> g.stream()
                            .map(c -> "(" + c.stream().map(i -> String.format("%+d", i))
                                    .collect((Collectors.joining(" "))) + ")")
                            .collect(Collectors.joining("")))
                    .collect(Collectors.joining("\n"));

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_288_5.txt");
        System.out.println(result);
    }
}
