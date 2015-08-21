package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GreedySorting {
    public static List<Integer> getKSortingReversal(List<Integer> permutation, int k) {
        List<Integer> kSortingReversal = new ArrayList<>();
        int kIndex = Math.max(permutation.indexOf(k + 1), permutation.indexOf(-(k + 1)));
        List<Integer> reversedSublist = IntStream.iterate(kIndex, i -> i - 1).
                limit(kIndex - k + 1).boxed().map(i -> -permutation.get(i)).collect(Collectors.toList());
        kSortingReversal.addAll(permutation.subList(0, k));
        kSortingReversal.addAll(reversedSublist);
        kSortingReversal.addAll(permutation.subList(k + reversedSublist.size(), permutation.size()));
        return kSortingReversal;
    }

    public static List<List<Integer>> getGreedySortPermutationSequence(List<Integer> permutation) {
        List<List<Integer>> greedySortPermutationSequence = new ArrayList<>();
        for (int k = 0; k < permutation.size(); k++) {
            if (permutation.get(k) != k + 1) {
                List<Integer> kSortingReversal = getKSortingReversal(permutation, k);
                permutation = kSortingReversal;
                greedySortPermutationSequence.add(new ArrayList<>(permutation));
                if (permutation.get(k) == -(k + 1)) {
                    permutation.set(k, k + 1);
                    greedySortPermutationSequence.add(new ArrayList<>(permutation));
                }
            }
        }
        return greedySortPermutationSequence;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String[] permutationStrings = br.readLine().replaceAll("\\(|\\)", "").split("\\s+");
            List<Integer> permutation = Arrays.stream(permutationStrings).map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<List<Integer>> greedySortPermutationSequence = getGreedySortPermutationSequence(permutation);

            String result = greedySortPermutationSequence.stream()
                    .map(l -> "(" + l.stream().map(i -> String.format("%+d", i)).collect(Collectors.joining(" ")) + ")")
                    .collect(Collectors.joining("\n"));

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_286_3.txt");
        System.out.println(result);
    }
}
