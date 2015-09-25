package ucsd.IandII;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LinearPeptideScoring {
    public static int getLinearScore(List<Integer> peptide, List<Integer> spectrum) {
        List<Integer> theoreticalSpectrum = CyclopeptideSequencing.getLinearSpectrum(peptide);
        Set<Integer> intersection = new HashSet<>(spectrum);
        intersection.retainAll(theoreticalSpectrum);
        int score = 0;
        for (int mass : intersection) {
            score += Math.min(Collections.frequency(theoreticalSpectrum, mass), Collections.frequency(spectrum, mass));
        }
        return score;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String peptideString = br.readLine();

            List<Integer> peptide = PeptideSpectrum.getAminoMassesFromPeptide(peptideString);

            String[] spectrumString = br.readLine().split("\\s+");
            List<Integer> spectrum = Arrays.asList(spectrumString).stream().map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());

            int score = getLinearScore(peptide, spectrum);

            return ConsoleCapturer.toString(score);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IandII/dataset_4913_1.txt");
        System.out.println(result);
    }
}
