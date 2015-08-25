package ucsd.III;

import ucsd.ConsoleCapturer;
import ucsd.IandII.ReverseComplement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SharedkMers {
    public static Map<String, List<Integer>> getkMersAndPositions(String v, int k) {
        Map<String, List<Integer>> kMersAndPositions = new HashMap<>();
        for (int i = 0; i <= v.length() - k; i++) {
            String kMer = v.substring(i, i + k);
            if (!kMersAndPositions.containsKey(kMer)) {
                kMersAndPositions.put(kMer, new ArrayList<>(Arrays.asList(i)));
            } else {
                kMersAndPositions.get(kMer).add(i);
            }

        }
        return kMersAndPositions;
    }

    public static Set<Map.Entry<Integer, Integer>> getSharedkMerPositions(String v, String w, int k) {
        Set<Map.Entry<Integer, Integer>> sharedkMerPositions = new HashSet<>();
        Map<String, List<Integer>> vkMerPositions = getkMersAndPositions(v, k);
        Map<String, List<Integer>> wkMerPositions = getkMersAndPositions(w, k);
        Map<String, List<Integer>> vReverseComplementkMerPositions = getkMersAndPositions(ReverseComplement.getReverseComplement(v), k);
        Map<String, List<Integer>> wReverseComplementkMerPositions = getkMersAndPositions(ReverseComplement.getReverseComplement(w), k);
        for (String kMer : vkMerPositions.keySet()) {
            if (wkMerPositions.containsKey(kMer)) {
                for (int i : vkMerPositions.get(kMer)) {
                    for (int j : wkMerPositions.get(kMer)) {
                        sharedkMerPositions.add(new AbstractMap.SimpleEntry(i, j));
                    }
                }
            }
            if (wReverseComplementkMerPositions.containsKey(kMer)) {
                for (int i : vkMerPositions.get(kMer)) {
                    for (int j : wReverseComplementkMerPositions.get(kMer)) {
                        sharedkMerPositions.add(new AbstractMap.SimpleEntry(i, w.length() - k - j));
                    }
                }
            }
        }
        for (String kMer : vReverseComplementkMerPositions.keySet()) {
            if (wkMerPositions.containsKey(kMer)) {
                for (int i : vReverseComplementkMerPositions.get(kMer)) {
                    for (int j : wkMerPositions.get(kMer)) {
                        sharedkMerPositions.add(new AbstractMap.SimpleEntry(v.length() - k - i, j));
                    }
                }
            }
            if (wReverseComplementkMerPositions.containsKey(kMer)) {
                for (int i : vReverseComplementkMerPositions.get(kMer)) {
                    for (int j : wReverseComplementkMerPositions.get(kMer)) {
                        sharedkMerPositions.add(new AbstractMap.SimpleEntry(v.length() - k - i, w.length() - k - j));
                    }
                }
            }
        }
        return sharedkMerPositions;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            int k = Integer.parseInt(br.readLine());
            String v = br.readLine();
            String w = br.readLine();

            Set<Map.Entry<Integer, Integer>> sharedkMerPositions = getSharedkMerPositions(v, w, k);

            String result = sharedkMerPositions.stream().map(p -> "(" + p.getKey() + ", " + p.getValue() + ")")
                    .collect(Collectors.joining("\n"));
            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_289_5.txt");
        System.out.println(result);
    }
}
