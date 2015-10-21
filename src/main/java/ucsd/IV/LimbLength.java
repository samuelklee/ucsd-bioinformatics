package ucsd.IV;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LimbLength {
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

    public static String matrixAsString(List<List<Integer>> matrix) {
        return matrix.stream().map(l -> l.stream().map(Object::toString).collect(Collectors.joining(" ")))
                .collect(Collectors.joining("\n"));
    }

    public static int getLimbLength(List<List<Integer>> distanceMatrix, int n, int j) {
        int length = 1000000;
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                if (i != j && k != j) {
                    int lengthFromPath = (distanceMatrix.get(i).get(j) + distanceMatrix.get(j).get(k) - distanceMatrix.get(i).get(k)) / 2;
                    if (lengthFromPath < length) {
                        length = lengthFromPath;
                    }
                }
            }
        }
        return length;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int n = Integer.parseInt(br.readLine());
            int j = Integer.parseInt(br.readLine());

            List<List<Integer>> distanceMatrix = readMatrix(br);
            int length = getLimbLength(distanceMatrix, n, j);

            return ConsoleCapturer.toString(length);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_10329_11.txt");
        System.out.println(result);
    }
}
