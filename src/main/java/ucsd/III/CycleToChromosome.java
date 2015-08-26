package ucsd.III;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CycleToChromosome {
    public static List<Integer> getChromosome(List<Integer> cycle) {
        List<Integer> chromosome = new ArrayList<>(cycle.size() / 2);
        for (int j = 0; j < cycle.size() / 2; j++) {
            if (cycle.get(2*j) < cycle.get(2*j + 1)) {
                chromosome.add(j, cycle.get(2*j + 1) / 2);
            } else {
                chromosome.add(j, -cycle.get(2*j) / 2);
            }
        }
        return chromosome;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {

            String[] cycleStrings = br.readLine().replaceAll("\\(|\\)", "").split("\\s+");
            List<Integer> cycle = Arrays.stream(cycleStrings).map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<Integer> chromosome = getChromosome(cycle);

            String result = "(" + chromosome.stream().map(i -> String.format("%+d", i)).collect(Collectors.joining(" ")) + ")";
            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/III/dataset_8222_5.txt");
        System.out.println(result);
    }
}
