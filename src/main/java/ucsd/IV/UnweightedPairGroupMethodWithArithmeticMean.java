package ucsd.IV;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UnweightedPairGroupMethodWithArithmeticMean {
    public static List<List<Integer>> readMatrix(BufferedReader br) {
        List<List<Integer>> matrix = new ArrayList<>();
        String inputString;
        try {
            while ((inputString = br.readLine()) != null) {
                matrix.add(Arrays.asList(inputString.split(" ")).stream().map(Integer::parseInt)
                        .collect(Collectors.toList()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
        return matrix;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int n = Integer.parseInt(br.readLine());

            List<List<Integer>> distanceMatrix = readMatrix(br);

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10329_11.txt");
        System.out.println(result);
    }
}
