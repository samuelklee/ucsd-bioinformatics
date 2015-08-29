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

        List<Map.Entry<Integer, Integer>> edgesPCopy = new ArrayList<>(edges);

        boolean isNewCycle = true;

        int first, second;
        int firstElementOfCycle = -1;
        int lastNode = -1;
        List<Integer> indexList;
        while (edgesPCopy.size() > 0) {
            if (isNewCycle) {
                cycles.add(new ArrayList<>());
                first = edgesPCopy.get(0).getKey();
                second = edgesPCopy.get(0).getValue();
                edgesPCopy.remove(0);

                cycles.get(cycles.size() - 1).add(first);
                cycles.get(cycles.size() - 1).add(second);

                firstElementOfCycle = first;
                if (second % 2 == 0) {
                    lastNode = second - 1;
                } else {
                    lastNode = second + 1;
                }
                isNewCycle = false;
            } else {
                indexList = new ArrayList<>();
                edgesPCopy.stream().map(p -> Arrays.asList(p.getKey(), p.getValue())).forEach(indexList::addAll);
                int index = indexList.indexOf(lastNode) / 2;
                first = edgesPCopy.get(index).getKey();
                second = edgesPCopy.get(index).getValue();
                edgesPCopy.remove(index);

                if (lastNode == first) {
                    cycles.get(cycles.size() - 1).add(first);
                    cycles.get(cycles.size() - 1).add(second);
                    if (second % 2 == 0) {
                        lastNode = second - 1;
                    } else {
                        lastNode = second + 1;
                    }
                } else {
                    cycles.get(cycles.size() - 1).add(second);
                    cycles.get(cycles.size() - 1).add(first);
                    if (first % 2 == 0) {
                        lastNode = first - 1;
                    } else {
                        lastNode = first + 1;
                    }
                }
                if (lastNode == firstElementOfCycle) {
                    Collections.rotate(cycles.get(cycles.size() - 1), 1);
                    isNewCycle = true;
                }
            }
        }
        return cycles;
    }

//    public static List<List<Integer>> getCycles(List<Map.Entry<Integer, Integer>> edges) {
//        List<List<Integer>> cycles = new ArrayList<>();
//        int lastNodeOfBlackEdge = -1;
//        int lastNodeOfColoredEdge = -1;
//        for (Map.Entry<Integer, Integer> edge : edges) {
//            int first = edge.getKey();
//            int second = edge.getValue();
//
//            if (first != lastNodeOfBlackEdge && second != lastNodeOfBlackEdge) {
//                if (cycles.size() >= 1) {
//                    Collections.rotate(cycles.get(cycles.size() - 1), 1);
//                }
//                cycles.add(new ArrayList<>());
//                lastNodeOfBlackEdge = -1;
//            }
//            if (first == lastNodeOfBlackEdge || lastNodeOfBlackEdge == -1) {
//                cycles.get(cycles.size() - 1).add(first);
//                cycles.get(cycles.size() - 1).add(second);
//                lastNodeOfColoredEdge = second;
//            } else if (second == lastNodeOfBlackEdge) {
//                cycles.get(cycles.size() - 1).add(second);
//                cycles.get(cycles.size() - 1).add(first);
//                lastNodeOfColoredEdge = first;
//            }
//
//            if (lastNodeOfColoredEdge != -1) {
//                if (lastNodeOfColoredEdge % 2 == 0) {
//                    lastNodeOfBlackEdge = lastNodeOfColoredEdge - 1;
//                } else {
//                    lastNodeOfBlackEdge = lastNodeOfColoredEdge + 1;
//                }
//            }
//        }
//        Collections.rotate(cycles.get(cycles.size() - 1), 1);
//        return cycles;
//    }

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
