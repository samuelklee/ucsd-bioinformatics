package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.IandII.HammingDistance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SmallParsimony {
    private static Random random = new Random();
    private static List<String> BASES = Arrays.asList("A", "C", "G", "T");

    public static Map<String, List<String>> readAdjacencyList(BufferedReader br) {
        Map<String, List<String>> adjacencyList= new HashMap<>();
        String inputString;
        try {
            while ((inputString = br.readLine()) != null) {
                String[] inputStringArray = inputString.split("->");
                String origin = inputStringArray[0];
                String destination = inputStringArray[1];
                if (!adjacencyList.containsKey(origin)) {
                    adjacencyList.put(origin, new ArrayList<>());
                }
                adjacencyList.get(origin).add(destination);
            }
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
        return adjacencyList;
    }

    public static String getAdjacencyAsString(Map<String, List<String>> adjacencyList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String origin : adjacencyList.keySet()) {
            for (String destination : adjacencyList.get(origin)) {
                stringBuilder.append(origin);
                stringBuilder.append("->");
                stringBuilder.append(destination);
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int n = Integer.parseInt(br.readLine());

            Map<String, List<String>> adjacencyList = readAdjacencyList(br);
            BinaryTree tree = initializeBinaryTreeWithScores(adjacencyList);
            tree.setValues();
            int score = tree.getSmallParsimonyScore();
            String result = tree.getSmallParsimonyAdjacencyListString();
//            System.out.println(score + "\n" + result);
            return ConsoleCapturer.toString(score + "\n" + result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10335_10.txt");
        System.out.println(result);
    }

    public static class BinaryTree {
        private static int LARGE_NUMBER = 1000000;
        private String value;
        private BinaryTree left, right, parent;
        private boolean isLeaf, isRoot;
        private List<List<Integer>> scores; //indexed by string position, ACGT
        private List<List<List<Boolean>>> consistentWithParent = new ArrayList<>(); //indexed by string position, parent base, ACGT

        public BinaryTree() {}

        public BinaryTree(String value, BinaryTree left, BinaryTree right) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.isLeaf = (left == null && right == null);
            if (!isLeaf) {
                this.left.parent = this;
                this.right.parent = this;
            }
            this.isRoot = false;
        }

        public void setRoot() {
            isRoot = true;
            parent = null;
        }

        public void setScores(int leafValueLength) {
            List<List<Integer>> scores = new ArrayList<>(leafValueLength);
            if (isLeaf) {
                for (int i = 0; i < leafValueLength; i++) {
                    scores.add(new ArrayList<>());
                    for (String base : BASES) {
                        if (value.substring(i, i + 1).equals(base)) {
                            scores.get(i).add(0);
                        } else {
                            scores.get(i).add(LARGE_NUMBER);
                        }
                    }
                }
            } else {
                for (int i = 0; i < leafValueLength; i++) {
                    scores.add(new ArrayList<>());
                    left.consistentWithParent.add(new ArrayList<>());
                    right.consistentWithParent.add(new ArrayList<>());
                    for (int j = 0; j <  BASES.size(); j++) {
                        int k = j;
                        List<Integer> leftScores = left.scores.get(i);
                        List<Integer> rightScores = right.scores.get(i);
                        int leftMinScore = Collections.min(IntStream.range(0, BASES.size()).boxed()
                                .map(b -> leftScores.get(b) + inverseKroneckerDelta(k, b))
                                .collect(Collectors.toList()));
                        int rightMinScore = Collections.min(IntStream.range(0, BASES.size()).boxed()
                                .map(b -> rightScores.get(b) + inverseKroneckerDelta(k, b))
                                .collect(Collectors.toList()));
                        int score = leftMinScore + rightMinScore;
                        scores.get(i).add(score);

                        left.consistentWithParent.get(i).add(IntStream.range(0, BASES.size()).boxed()
                                .map(b -> leftScores.get(b) + inverseKroneckerDelta(k, b) == leftMinScore).collect(Collectors.toList()));
                        right.consistentWithParent.get(i).add(IntStream.range(0, BASES.size()).boxed()
                                .map(b -> rightScores.get(b) + inverseKroneckerDelta(k, b) == rightMinScore).collect(Collectors.toList()));
                    }
                }
            }
            this.scores = scores;
        }

        public void setValues() {
            if (isRoot) {
                StringBuilder valueBuilder = new StringBuilder();
                for (List<Integer> score : scores) {
                    valueBuilder.append(BASES.get(score.indexOf(Collections.min(score))));
                }
                value = valueBuilder.toString();
                left.setValues();
                right.setValues();
            } else if (!isLeaf) {
                StringBuilder valueBuilder = new StringBuilder();
                for (int i = 0; i < scores.size(); i++) {
                    int valueIndex = i;
                    String parentBase = parent.value.substring(i, i + 1);
                    int parentBaseIndex = BASES.indexOf(parentBase);
                    List<String> consistentBases =
                            IntStream.range(0, BASES.size()).boxed().filter(n -> consistentWithParent.get(valueIndex).get(parentBaseIndex).get(n))
                                    .map(BASES::get).collect(Collectors.toList());
//                    valueBuilder.append(consistentBases.get(random.nextInt(consistentBases.size())));
                    if (consistentBases.contains(parentBase)) {
                        valueBuilder.append(parentBase);
                    } else {
                        valueBuilder.append(consistentBases.get(random.nextInt(consistentBases.size())));
                    }
                }
                value = valueBuilder.toString();
                left.setValues();
                right.setValues();
            }
        }

        public int getSmallParsimonyScore() {
            if (isLeaf) {
                return 0;
            }
            return HammingDistance.getHammingDistance(value, left.value) + HammingDistance.getHammingDistance(value, right.value) + left.getSmallParsimonyScore() + right.getSmallParsimonyScore();
        }

        public String getSmallParsimonyAdjacencyListString() {
            if (isLeaf) {
                return "";
            }
            int leftDistance = HammingDistance.getHammingDistance(value, left.value);
            int rightDistance = HammingDistance.getHammingDistance(value, right.value);
            String leftEdge = value + "->" + left.value + ":" + leftDistance + "\n"
                    + left.value + "->" + value + ":" + leftDistance + "\n";
            String rightEdge = value + "->" + right.value + ":" + rightDistance + "\n"
                    + right.value + "->" + value + ":" + rightDistance + "\n";
            return leftEdge + rightEdge + left.getSmallParsimonyAdjacencyListString() + right.getSmallParsimonyAdjacencyListString();
        }
        public String getSmallParsimonyUnrootedAdjacencyListString() {
            if (isLeaf) {
                return "";
            }
            if (isRoot) {
                int leftDistance = HammingDistance.getHammingDistance(value, left.value);
                int rightDistance = HammingDistance.getHammingDistance(value, right.value);
                String edge = left.value + "->" + right.value + ":" + leftDistance + rightDistance + "\n"
                        + right.value + "->" + left.value + ":" + leftDistance + rightDistance + "\n";
                return edge + left.getSmallParsimonyAdjacencyListString() + right.getSmallParsimonyAdjacencyListString();
            }
            int leftDistance = HammingDistance.getHammingDistance(value, left.value);
            int rightDistance = HammingDistance.getHammingDistance(value, right.value);
            String leftEdge = value + "->" + left.value + ":" + leftDistance + "\n"
                    + left.value + "->" + value + ":" + leftDistance + "\n";
            String rightEdge = value + "->" + right.value + ":" + rightDistance + "\n"
                    + right.value + "->" + value + ":" + rightDistance + "\n";
            return leftEdge + rightEdge + left.getSmallParsimonyAdjacencyListString() + right.getSmallParsimonyAdjacencyListString();
        }
    }

    public static int inverseKroneckerDelta(int i, int j) {
        if (i == j) {
            return 0;
        }
        return 1;
    }

    public static BinaryTree initializeBinaryTreeWithScores(Map<String, List<String>> adjacencyList) {
        Set<String> nodesSet = new HashSet<>(adjacencyList.keySet());
        adjacencyList.keySet().stream().map(adjacencyList::get).flatMap(Collection::stream)
                .distinct().forEach(nodesSet::add);
        List<String> nodes = new ArrayList<>(nodesSet);
        int leafValueLength = Collections.max(nodes.stream().map(String::length).collect(Collectors.toList()));


        Map<String, BinaryTree> treeMap = new HashMap<>();
        int nodeIndex = 0;
        BinaryTree tree = new BinaryTree();
        while (nodes.size() > 0) {
            String node = nodes.get(nodeIndex);
            if (!adjacencyList.containsKey(node)) {
                BinaryTree leaf = new BinaryTree(node, null, null);
                leaf.setScores(leafValueLength);
                treeMap.put(node, leaf);
                nodes.remove(node);
            } else {
                List<String> childrenOfNode = adjacencyList.get(node);
                if (treeMap.keySet().containsAll(childrenOfNode)) {
                    tree = new BinaryTree(node, treeMap.get(childrenOfNode.get(0)), treeMap.get(childrenOfNode.get(1)));
                    tree.setScores(leafValueLength);
                    treeMap.put(node, tree);
                    nodes.remove(node);
                } else {
                    nodeIndex++;
                }
            }
            if (nodeIndex >= nodes.size()) {
                nodeIndex = 0;
            }
        }
        tree.setRoot();
        return tree;
    }
}
