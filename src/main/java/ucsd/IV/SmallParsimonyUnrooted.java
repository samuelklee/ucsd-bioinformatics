package ucsd.IV;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SmallParsimonyUnrooted {
    public static Map<String, List<String>> addRoot(Map<String, List<String>> unrootedAdjacencyList) {
        Map<String, List<String>> adjacencyList = new HashMap<>();
        List<String> nodes = new ArrayList<>(unrootedAdjacencyList.keySet());
        int leafValueLength = Collections.max(nodes.stream().map(String::length).collect(Collectors.toList()));
        List<String> internalNodes = nodes.stream().filter(s -> s.length() < leafValueLength).collect(Collectors.toList());

        String edgeNodeA = null;
        String edgeNodeB = null;
        outerloop:
        for (String nodeA : internalNodes) {
            for (String nodeB : internalNodes) {
                if (unrootedAdjacencyList.get(nodeA).contains(nodeB)) {
                    edgeNodeA = nodeA;
                    edgeNodeB = nodeB;
                    break outerloop;
                }
            }
        }

        List<String> nodesToVisit = new ArrayList<>(Arrays.asList(edgeNodeA, edgeNodeB));
        List<String> nodesVisited = new ArrayList<>();
        adjacencyList.put("r", Arrays.asList(edgeNodeA, edgeNodeB));
        unrootedAdjacencyList.get(edgeNodeA).remove(edgeNodeB);
        unrootedAdjacencyList.get(edgeNodeB).remove(edgeNodeA);
        while (!nodesToVisit.isEmpty()) {
            String currentNode = nodesToVisit.get(0);
            if (internalNodes.contains(currentNode)) {
                List<String> children = unrootedAdjacencyList.get(currentNode).stream().filter(n -> !nodesVisited.contains(n)).collect(Collectors.toList());
                adjacencyList.put(currentNode, children);
                nodesToVisit.addAll(children);
                nodesVisited.add(currentNode);
            }
            nodesToVisit.remove(currentNode);
        }

        return adjacencyList;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int n = Integer.parseInt(br.readLine());

            Map<String, List<String>> unrootedAdjacencyList = SmallParsimony.readAdjacencyList(br);
            Map<String, List<String>> adjacencyList = addRoot(unrootedAdjacencyList);
//            System.out.println(SmallParsimony.getAdjacencyAsString(adjacencyList));
            SmallParsimony.BinaryTree tree = SmallParsimony.initializeBinaryTreeWithScores(adjacencyList);
            tree.setValues();
            int score = tree.getSmallParsimonyScore();
            String result = tree.getSmallParsimonyUnrootedAdjacencyListString();
//            System.out.println(score + "\n" + result);
            return ConsoleCapturer.toString(score + "\n" + result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10335_12.txt");
        System.out.println(result);
    }
}
