package ucsd.IV;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LargeParsimony {
    public static int LARGE_SCORE = 100000;

    public static List<Map.Entry<String, String>> getInternalEdges(SmallParsimony.BinaryTree tree) {
        Map<String, List<String>> unrootedAdjacencyList = tree.asUnrootedAdjacencyList();
        List<String> nodes = new ArrayList<>(unrootedAdjacencyList.keySet());
        List<String> leaves = tree.getLeaves();
        List<String> internalNodes = nodes.stream().filter(s -> !leaves.contains(s)).collect(Collectors.toList());

        List<Map.Entry<String, String>> internalEdges = new ArrayList<>();
        for (String nodeA : internalNodes) {
            for (String nodeB : internalNodes) {
                Map.Entry<String, String> edge = new AbstractMap.SimpleEntry<>(nodeA, nodeB);
                Map.Entry<String, String> reverseEdge = new AbstractMap.SimpleEntry<>(nodeB, nodeA);
                if (unrootedAdjacencyList.get(nodeA).contains(nodeB) && !internalEdges.contains(reverseEdge)) {
                    internalEdges.add(edge);
                }
            }
        }
        return internalEdges;
    }

    public static SmallParsimony.BinaryTree getSmallParsimonyTree(Map<String, List<String>> unrootedAdjacencyList) {
        Map<String, List<String>> adjacencyList = SmallParsimonyUnrooted.addRoot(unrootedAdjacencyList);

        SmallParsimony.BinaryTree tree = SmallParsimony.initializeBinaryTreeWithScores(adjacencyList);
        tree.setValues();
        return tree;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int n = Integer.parseInt(br.readLine());

            Map<String, List<String>> inputUnrootedAdjacencyList = SmallParsimony.readAdjacencyList(br);
            Map<String, List<String>> adjacencyList = SmallParsimonyUnrooted.addRoot(inputUnrootedAdjacencyList);

            SmallParsimony.BinaryTree tree = SmallParsimony.initializeBinaryTreeWithScores(adjacencyList);
            tree.setValues();
            int score = LARGE_SCORE;
            int newScore = tree.getSmallParsimonyScore();
            SmallParsimony.BinaryTree newTree = tree;

            List<String> results = new ArrayList<>();
            while (newScore < score) {
                score = newScore;
                tree = newTree;

                List<Map.Entry<String, String>> internalEdges = getInternalEdges(tree);
                for (Map.Entry<String, String> edge : internalEdges) {
                    String nodeA = edge.getKey();
                    String nodeB = edge.getValue();
                    Map<String, List<String>> unrootedAdjacencyList = tree.asUnrootedAdjacencyList();
                    List<Map<String, List<String>>> nearestNeighbors = NearestNeighbors.getNearestNeighbors(unrootedAdjacencyList, nodeA, nodeB);
                    for (Map<String, List<String>> neighbor : nearestNeighbors) {
                        SmallParsimony.BinaryTree neighborTree = getSmallParsimonyTree(neighbor);
                        int nearestNeighborScore = neighborTree.getSmallParsimonyScore();
                        if (nearestNeighborScore < newScore) {
                            newScore = nearestNeighborScore;
                            newTree = neighborTree;
                        }
                    }
                }
                results.add(newScore + "\n" + newTree.getSmallParsimonyUnrootedAdjacencyListString());
            }
            String result = results.stream().collect(Collectors.joining("\n"));
            System.out.println(result);
            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10336_8.txt");
    }
}
