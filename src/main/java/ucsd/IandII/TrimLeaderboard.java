package ucsd.IandII;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TrimLeaderboard {
    public static List<String> trim(List<String> leaderboard, List<Integer> spectrum, int numberOfPlaces) {
        if (leaderboard.isEmpty()) {
            return leaderboard;
        }
        Map<String, Integer> scoredPeptides = new HashMap<>();
        for (String peptideString : leaderboard) {
            List<Integer> peptide = PeptideSpectrum.getAminoMassesFromPeptide(peptideString);
            int score = LinearPeptideScoring.getLinearScore(peptide, spectrum);
            scoredPeptides.put(peptideString, score);
        }

        List<Map.Entry<String, Integer>> sortedScoredPeptides =
                scoredPeptides.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

//        System.out.println(sortedScoredPeptides);

        List<String> sortedLeaderboard = sortedScoredPeptides.stream().map(e -> e.getKey())
                .collect(Collectors.toList());
        List<Integer> sortedLinearScores = sortedScoredPeptides.stream().map(e -> e.getValue())
                .collect(Collectors.toList());

        for (int j = numberOfPlaces - 1; j < sortedLeaderboard.size(); j++) {
            if (sortedLinearScores.get(j) < sortedLinearScores.get(numberOfPlaces - 1)) {
                return sortedLeaderboard.subList(0, j);
            }
        }
        return sortedLeaderboard;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<String> leaderboard = Arrays.asList(br.readLine().split("\\s+"));
            String[] spectrumString = br.readLine().split("\\s+");
            List<Integer> spectrum = Arrays.asList(spectrumString).stream().map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());

            int numberOfPlaces = Integer.parseInt(br.readLine());

            List<String> trimmedLeaderboard = trim(leaderboard, spectrum, numberOfPlaces);

            return ConsoleCapturer.toString(trimmedLeaderboard.stream().collect(Collectors.joining(" ")));
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IandII/dataset_4913_3.txt");
        System.out.println(result);
    }
}
