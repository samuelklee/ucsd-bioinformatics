package ucsd.III;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;
import ucsd.IandII.ReverseComplement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SharedkMers {
    public static Map<String, List<Integer>> getkMersAndPositions(String v, int k) {
        Map<String, List<Integer>> kMersAndPositions = new HashMap<>();
        StringBuilder kMerBuilder = new StringBuilder("-" + v.substring(0, k - 1));
        for (int i = k; i <= v.length(); i++) {
            kMerBuilder.deleteCharAt(0).append(v.charAt(i - 1));
            if (!kMersAndPositions.containsKey(kMerBuilder.toString())) {
                kMersAndPositions.put(kMerBuilder.toString(), new ArrayList<>(Arrays.asList(i - k)));
            } else {
                kMersAndPositions.get(kMerBuilder.toString()).add(i - k);
            }
        }
        return kMersAndPositions;
    }
    
    public static Set<String> getIntersection(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection;
    }

    public static Set<Map.Entry<Integer, Integer>> getSharedkMerPositions(String v, String w, int k) {
        Set<Map.Entry<Integer, Integer>> sharedkMerPositions = new HashSet<>();
        Map<String, List<Integer>> vkMerPositions = getkMersAndPositions(v, k);
        System.out.println("String hashed.");
        Map<String, List<Integer>> wkMerPositions = getkMersAndPositions(w, k);
        System.out.println("String hashed.");
        Map<String, List<Integer>> vReverseComplementkMerPositions = getkMersAndPositions(ReverseComplement.getReverseComplement(v), k);
        System.out.println("String hashed.");
        Map<String, List<Integer>> wReverseComplementkMerPositions = getkMersAndPositions(ReverseComplement.getReverseComplement(w), k);
        System.out.println("String hashed.");

        System.out.println("Strings hashed.");

        Set<String> intersectionOfvAndwKeys = getIntersection(vkMerPositions.keySet(), wkMerPositions.keySet());
        Set<String> intersectionOfvAndwReverseComplementKeys = getIntersection(vkMerPositions.keySet(), wReverseComplementkMerPositions.keySet());
        Set<String> intersectionOfvReverseComplementAndwKeys = getIntersection(vReverseComplementkMerPositions.keySet(), wkMerPositions.keySet());
        Set<String> intersectionOfvReverseComplementAndwReverseComplementKeys = getIntersection(vReverseComplementkMerPositions.keySet(), wReverseComplementkMerPositions.keySet());
        
        for (String kMer : intersectionOfvAndwKeys) {
            for (int i : vkMerPositions.get(kMer)) {
                for (int j : wkMerPositions.get(kMer)) {
                    sharedkMerPositions.add(new AbstractMap.SimpleEntry(i, j));
                }
            }
        }
        for (String kMer : intersectionOfvAndwReverseComplementKeys) {
            for (int i : vkMerPositions.get(kMer)) {
                for (int j : wReverseComplementkMerPositions.get(kMer)) {
                    sharedkMerPositions.add(new AbstractMap.SimpleEntry(i, w.length() - k - j));
                }
            }
        }
        for (String kMer : intersectionOfvReverseComplementAndwKeys) {
            for (int i : vReverseComplementkMerPositions.get(kMer)) {
                for (int j : wkMerPositions.get(kMer)) {
                    sharedkMerPositions.add(new AbstractMap.SimpleEntry(v.length() - k - i, j));
                }
            }
        }
        for (String kMer : intersectionOfvReverseComplementAndwReverseComplementKeys) {
            for (int i : vReverseComplementkMerPositions.get(kMer)) {
                for (int j : wReverseComplementkMerPositions.get(kMer)) {
                    sharedkMerPositions.add(new AbstractMap.SimpleEntry(v.length() - k - i, w.length() - k - j));
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

    public static String doWork(String eColiFileName, String sEntericaFileName) {
    try (BufferedReader br = new BufferedReader(new FileReader(eColiFileName))) {
        int k = 30;

        String v = br.readLine();
        List<String> fasta = DataTableUtils.readFASTA(sEntericaFileName);
        String w = fasta.stream().collect(Collectors.joining(""));

        Set<Map.Entry<Integer, Integer>> sharedkMerPositions = getSharedkMerPositions(v, w, k);

        return ConsoleCapturer.toString(sharedkMerPositions.size());
    } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_289_5.txt");
        System.out.println(result);

        //needs -Xmx8g option
        result = doWork("src/test/resources/III/E-coli.txt", "src/test/resources/III/Salmonella-enterica.fasta");
        System.out.println(result);
    }
}
