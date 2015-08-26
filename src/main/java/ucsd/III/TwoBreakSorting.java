package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TwoBreakSorting {
    public static List<Integer> getCycle(List<Integer> chromosome) {
        List<Integer> cycle = new ArrayList<>(2*chromosome.size());
        for (int j = 1; j <= chromosome.size(); j++) {
            int i = chromosome.get(j);
            if (i > 0) {
                cycle.set(2*j - 1, 2*i - 1);
                cycle.set(2*j, 2*i);
            } else {
                cycle.set(2*j - 1, -2*i);
                cycle.set(2*j, -2*i - 1);
            }
        }
        return cycle;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            String[] chromosomeStrings = br.readLine().replaceAll("\\(|\\)", "").split("\\s+");
            List<Integer> chromosome = Arrays.stream(chromosomeStrings).map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<Integer> cycle = getCycle(chromosome);

            String result = "(" + cycle.stream().map(String::valueOf).collect(Collectors.joining(" ")) + ")";
            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/sample/ChromosomeToCycle.txt");
        System.out.println(result);
    }
}
