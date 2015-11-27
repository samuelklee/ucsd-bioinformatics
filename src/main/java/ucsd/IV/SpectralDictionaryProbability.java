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
//    public static double AMINO_PROBABILITY = 1. / 2.;
//    public static List<Integer> aminoMasses = new ArrayList<>(DataTableUtils.getAminoMassTableXZ().values());

    public static double AMINO_PROBABILITY = 1. / 20.;
    public static List<Integer> aminoMasses = new ArrayList<>(DataTableUtils.getAminoMassTable().values());

    public static double getProbability(List<Integer> spectralVector, int i, int score) {
        if (i == 0 && score == 0) {
            return 1.;
        }
        if (i <= 0 || score < 0) {
            return 0.;
        }
        double probability = 0.;
        for (int mass : aminoMasses) {
            probability += AMINO_PROBABILITY * getProbability(spectralVector, i - mass, score - spectralVector.get(i - 1));
        }
        return probability;
    }

    public static double getDictionaryProbability(List<Integer> spectralVector, int threshold, int maxScore) {
        double probability = 0.;
        int length = spectralVector.size();
        for (int t = threshold; t <= maxScore; t++) {
            probability += getProbability(spectralVector, length, t);
//            System.out.println(i + ": " + size);
        }
        return probability;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<Integer> spectralVector = Arrays.asList(br.readLine().split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());
            int threshold = Integer.parseInt(br.readLine());
            int maxScore = Integer.parseInt(br.readLine());

            String result = Double.toString(getDictionaryProbability(spectralVector, threshold, maxScore));

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_11866_11.txt");
        System.out.println(result);
    }
}
