package ucsd.IV;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NearestNeighbors {
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

    public static Map<String, List<String>> copyAdjacencyList(Map<String, List<String>> adjacencyList) {
        Map<String, List<String>> adjacencyListCopy = new HashMap<>();
        for (String origin : adjacencyList.keySet()) {
            adjacencyListCopy.put(origin, new ArrayList<>(adjacencyList.get(origin)));
        }
        return adjacencyListCopy;
    }

    public static Map<String, List<String>> swapNodes(Map<String, List<String>> adjacencyList, String a, String b, String c, String d) {
        Map<String, List<String>> swappedAdjacencyList = copyAdjacencyList(adjacencyList);
        swappedAdjacencyList.get(a).remove(c);
        swappedAdjacencyList.get(a).add(d);
        swappedAdjacencyList.get(c).remove(a);
        swappedAdjacencyList.get(c).add(b);
        swappedAdjacencyList.get(b).remove(d);
        swappedAdjacencyList.get(b).add(c);
        swappedAdjacencyList.get(d).remove(b);
        swappedAdjacencyList.get(d).add(a);
        return swappedAdjacencyList;
    }

    public static List<Map<String, List<String>>> getNearestNeighbors(Map<String, List<String>> adjacencyList,
                                                                      String a, String b) {
        List<String> aChildren = new ArrayList<>(adjacencyList.get(a));
        aChildren.remove(b);
//        System.out.println(aChildren);
        List<String> bChildren = new ArrayList<>(adjacencyList.get(b));
        bChildren.remove(a);
//        System.out.println(bChildren);
        List<Map<String, List<String>>> neighbors = new ArrayList<>();
        neighbors.add(swapNodes(adjacencyList, a, b, aChildren.get(0), bChildren.get(0)));
        neighbors.add(swapNodes(adjacencyList, a, b, aChildren.get(0), bChildren.get(1)));
        return neighbors;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String[] edgeString = br.readLine().split("\\s+");
            String a = edgeString[0];
            String b = edgeString[1];

            Map<String, List<String>> adjacencyList = readAdjacencyList(br);
            System.out.println(getAdjacencyAsString(adjacencyList));
            List<Map<String, List<String>>> neighbors = getNearestNeighbors(adjacencyList, a, b);
            String neighbor1 = getAdjacencyAsString(neighbors.get(0));
            String neighbor2 = getAdjacencyAsString(neighbors.get(1));
            System.out.println(neighbor1 + "\n" + neighbor2);
            return ConsoleCapturer.toString(neighbor1 + "\n" + neighbor2);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10336_6.txt");
        System.out.println(result);
    }
}
