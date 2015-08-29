package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TwoBreakDistance {
    public static List<Map.Entry<Integer, Integer>> sortEdges(List<Map.Entry<Integer, Integer>> edges) {
        List<Map.Entry<Integer, Integer>> sortedEdges = new ArrayList<>(edges);
        Collections.sort(sortedEdges, (edge1, edge2) ->
                Math.min(edge1.getKey(), edge1.getValue()) - Math.min(edge2.getKey(), edge2.getValue()));
        return sortedEdges;
    }

    public static List<List<Integer>> getCycles(List<Map.Entry<Integer, Integer>> edgesP,
                                                List<Map.Entry<Integer, Integer>> edgesQ) {
        List<List<Integer>> cycles = new ArrayList<>();

        List<Map.Entry<Integer, Integer>> edgesPCopy = new ArrayList<>(edgesP);
        List<Map.Entry<Integer, Integer>> edgesQCopy = new ArrayList<>(edgesQ);

        boolean onP = true;
        boolean isNewCycle = true;

        int first, second;
        int firstElementOfCycle = -1;
        int lastNode = -1;
        List<Integer> indexList;
        while (edgesPCopy.size() > 0 || edgesQCopy.size() > 0) {
            if (isNewCycle) {
                cycles.add(new ArrayList<>());
                if (onP) {
                    first = edgesPCopy.get(0).getKey();
                    second = edgesPCopy.get(0).getValue();
                    edgesPCopy.remove(0);
                    onP = false;
                } else {
                    first = edgesQCopy.get(0).getKey();
                    second = edgesQCopy.get(0).getValue();
                    edgesQCopy.remove(0);
                    onP = true;
                }
                cycles.get(cycles.size() - 1).add(first);
                cycles.get(cycles.size() - 1).add(second);
                firstElementOfCycle = first;
                lastNode = second;
                isNewCycle = false;
            } else {
                if (onP) {
                    indexList = new ArrayList<>();
                    edgesPCopy.stream().map(p -> Arrays.asList(p.getKey(), p.getValue())).forEach(indexList::addAll);
                    int index = indexList.indexOf(lastNode) / 2;
                    first = edgesPCopy.get(index).getKey();
                    second = edgesPCopy.get(index).getValue();
                    edgesPCopy.remove(index);
                    onP = false;
                } else {
                    indexList = new ArrayList<>();
                    edgesQCopy.stream().map(p -> Arrays.asList(p.getKey(), p.getValue())).forEach(indexList::addAll);
                    int index = indexList.indexOf(lastNode) / 2;
                    first = edgesQCopy.get(index).getKey();
                    second = edgesQCopy.get(index).getValue();
                    edgesQCopy.remove(index);
                    onP = true;
                }
                if (lastNode == first) {
                    cycles.get(cycles.size() - 1).add(second);
                    lastNode = second;
                } else {
                    cycles.get(cycles.size() - 1).add(first);
                    lastNode = first;
                }
                if (first == firstElementOfCycle || second == firstElementOfCycle) {
                    isNewCycle = true;
                }
            }
        }
        return cycles;
    }

    public static int getDistance(List<List<Integer>> genomeP, List<List<Integer>> genomeQ) {
        List<Map.Entry<Integer, Integer>> edgesP = ColoredEdges.getEdges(genomeP);
        List<Map.Entry<Integer, Integer>> edgesQ = ColoredEdges.getEdges(genomeQ);

        List<List<Integer>> cycles = getCycles(edgesP, edgesQ);

        return edgesP.size() - cycles.size();
    }

    public static List<List<Integer>> genomeFromString(String genomeString) {
        return Arrays.stream(genomeString.split("\\)\\("))
                .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split("\\s+")).map(Integer::parseInt)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            String genomePString = br.readLine();
            String genomeQString = br.readLine();

            List<List<Integer>> genomeP = genomeFromString(genomePString);
            List<List<Integer>> genomeQ = genomeFromString(genomeQString);

            int twoBreakDistance = getDistance(genomeP, genomeQ);

            return ConsoleCapturer.toString(twoBreakDistance);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_288_4.txt");
        System.out.println(result);
    }
}
