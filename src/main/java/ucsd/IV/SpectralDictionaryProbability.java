package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpectralDictionaryProbability {
    public static double getProbability(List<Integer> spectralVector, int i, int score, double aminoProbability, List<Integer> aminoMasses) {
        if (i == 0 && score == 0) {
            return 1.;
        }
        if (i <= 0 || score < 0) {
            return 0.;
        }
        double probability = 0.;
        for (int mass : aminoMasses) {
            probability += aminoProbability * getProbability(spectralVector, i - mass, score - spectralVector.get(i - 1), aminoProbability, aminoMasses);
        }
        return probability;
    }

    public static double getDictionaryProbability(List<Integer> spectralVector, int threshold, int maxScore, double aminoProbability, List<Integer> aminoMasses) {
        double probability = 0.;
        int length = spectralVector.size();
        for (int t = threshold; t <= maxScore; t++) {
            probability += getProbability(spectralVector, length, t, aminoProbability, aminoMasses);
//            System.out.println(i + ": " + size);
        }
        return probability;
    }

    public static String doWork(String dataFileName, double aminoProbability, List<Integer> aminoMasses) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<Integer> spectralVector = Arrays.asList(br.readLine().split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());
            int threshold = Integer.parseInt(br.readLine());
            int maxScore = Integer.parseInt(br.readLine());

            String result = Double.toString(getDictionaryProbability(spectralVector, threshold, maxScore, aminoProbability, aminoMasses));

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        double aminoProbability = 1. / 20.;
        List<Integer> aminoMasses = new ArrayList<>(DataTableUtils.getAminoMassTable().values());
        String result = doWork("src/test/resources/IV/dataset_11866_11.txt", aminoProbability, aminoMasses);
        System.out.println(result);
    }
}
