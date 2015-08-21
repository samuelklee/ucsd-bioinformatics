package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Breakpoints {
    public static int getNumberOfAdjacencies(List<Integer> permutation) {
        int numberOfAdjacencies = 0;
        for (int i = 0; i < permutation.size() - 1; i++) {
            if (permutation.get(i + 1) - permutation.get(i) == 1) {
                numberOfAdjacencies++;
            }
        }
        return numberOfAdjacencies;
    }

    public static int getNumberOfBreakpoints(int numberOfAdjacencies, int numberOfElements) {
        return numberOfElements + 1 - numberOfAdjacencies;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String[] permutationStrings = br.readLine().replaceAll("\\(|\\)", "").split("\\s+");
            List<Integer> permutation = Arrays.stream(permutationStrings).map(Integer::parseInt)
                    .collect(Collectors.toList());
            permutation.add(0, 0);
            permutation.add(permutation.size(), permutation.size());
            int numberOfAdjacencies = getNumberOfAdjacencies(permutation);
            int numberOfBreakpoints = getNumberOfBreakpoints(numberOfAdjacencies, permutation.size() - 2);

            return ConsoleCapturer.toString(numberOfBreakpoints);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_287_5.txt");
        System.out.println(result);
    }
}
