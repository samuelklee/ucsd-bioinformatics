package ucsd.IandII;

import ucsd.ConsoleCapturer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ConvolutionCyclopeptideSequencing {
    public static List<List<Integer>> expand(List<List<Integer>> peptides, List<Integer> aminoMasses) {
        List<List<Integer>> expandedPeptides = new ArrayList<>();
        for (List<Integer> peptide : peptides) {
            for (int aminoMass : aminoMasses) {
                List<Integer> newPeptide = new ArrayList<>(peptide);
                newPeptide.add(aminoMass);
                expandedPeptides.add(newPeptide);
            }
        }
        return expandedPeptides;
    }

    public static List<List<Integer>> trim(List<List<Integer>> leaderboard, List<Integer> spectrum, int numberOfPlaces) {
        if (leaderboard.isEmpty()) {
            return leaderboard;
        }
        Map<List<Integer>, Integer> scoredPeptides = new HashMap<>();
        for (List<Integer> peptide : leaderboard) {
            int score = LinearPeptideScoring.getLinearScore(peptide, spectrum);
            scoredPeptides.put(peptide, score);
        }

        List<Map.Entry<List<Integer>, Integer>> sortedScoredPeptides =
                scoredPeptides.entrySet().stream().sorted(Map.Entry.<List<Integer>, Integer>comparingByValue().reversed())
                        .collect(Collectors.toList());

        System.out.println(sortedScoredPeptides);

        List<List<Integer>> sortedLeaderboard = sortedScoredPeptides.stream().map(e -> e.getKey())
                .collect(Collectors.toList());
        List<Integer> sortedLinearScores = sortedScoredPeptides.stream().map(e -> e.getValue())
                .collect(Collectors.toList());

        for (int j = numberOfPlaces; j < sortedLeaderboard.size(); j++) {
            if (sortedLinearScores.get(j) < sortedLinearScores.get(numberOfPlaces - 1)) {
                return sortedLeaderboard.subList(0, j);
            }
        }
        return sortedLeaderboard;
    }

    public static List<Integer> getAminoMasses(List<Integer> spectralConvolution, int numberOfAminos) {
        Set<Integer> aminoMasses = spectralConvolution.stream().filter(i -> 57 <= i && i <= 200).collect(Collectors.toSet());;

        Map<Integer, Integer> aminoMassFrequency = new HashMap<>();
        for (int aminoMass : aminoMasses) {
            aminoMassFrequency.put(aminoMass, Collections.frequency(spectralConvolution, aminoMass));
        }

        List<Map.Entry<Integer, Integer>> sortedAminoMassFrequency =
                aminoMassFrequency.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                        .collect(Collectors.toList());

        List<Integer> sortedAminoMasses = sortedAminoMassFrequency.stream().map(e -> e.getKey())
                .collect(Collectors.toList());
        List<Integer> sortedFrequencies = sortedAminoMassFrequency.stream().map(e -> e.getValue())
                .collect(Collectors.toList());

        for (int j = numberOfAminos; j < sortedAminoMasses.size(); j++) {
            if (sortedFrequencies.get(j) < sortedFrequencies.get(numberOfAminos - 1)) {
                return sortedAminoMasses.subList(0, j);
            }
        }
        return sortedAminoMasses;
    }

    public static List<Integer> getLeaderPeptide(List<Integer> spectrum, int numberOfPlaces, List<Integer> aminoMasses) {
        List<List<Integer>> leaderboard = new ArrayList<>(Arrays.asList(Arrays.asList()));
        int parentMass = Collections.max(spectrum);
        List<Integer> leaderPeptide = new ArrayList<>();
        int leaderPeptideScore = 0;
        int peptideLength = 1;
        while (!leaderboard.isEmpty() && peptideLength < 50) {
            System.out.println(peptideLength);
            leaderboard = expand(leaderboard, aminoMasses);
//            System.out.println("leaderboard:");
//            System.out.println(leaderboard);
            for (Iterator<List<Integer>> iterator = leaderboard.iterator(); iterator.hasNext();) {
                List<Integer> peptide = iterator.next();
                int mass = peptide.stream().mapToInt(Integer::valueOf).sum();
                if (mass == parentMass) {
                    int peptideScore = CyclopeptideScoring.getCyclicScore(peptide, spectrum);
                    if (peptideScore > leaderPeptideScore) {
                        leaderPeptide = peptide;
                        leaderPeptideScore = peptideScore;
                    }
                } else if (mass > parentMass) {
                    iterator.remove();
                }
            }
            leaderboard = trim(leaderboard, spectrum, numberOfPlaces);
            peptideLength++;
        }
        return leaderPeptide;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int numberOfAminos = Integer.parseInt(br.readLine());
            int numberOfPlaces = Integer.parseInt(br.readLine());
            String[] spectrumString = br.readLine().split("\\s+");
            List<Integer> spectrum = Arrays.asList(spectrumString).stream().map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());

            List<Integer> spectralConvolution = SpectralConvolution.getSpectralConvolution(spectrum);
            List<Integer> aminoMasses = getAminoMasses(spectralConvolution, numberOfAminos);

            List<Integer> leaderPeptide = getLeaderPeptide(spectrum, numberOfPlaces, aminoMasses);
            int score = CyclopeptideScoring.getCyclicScore(leaderPeptide, spectrum);

            return ConsoleCapturer.toString(leaderPeptide.stream().map(i -> i.toString())
                    .collect(Collectors.joining("-")) + "\n" + score);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static List<List<Integer>> getLeaderboard(List<Integer> spectrum, int numberOfPlaces, List<Integer> aminoMasses) {
        List<List<Integer>> leaderboard = new ArrayList<>(Arrays.asList(Arrays.asList()));
        List<List<Integer>> leaders = new ArrayList<>();
        int parentMass = Collections.max(spectrum);
        int leaderPeptideScore = 0;
        int peptideLength = 1;
        while (!leaderboard.isEmpty() && peptideLength < 50) {
            System.out.println(peptideLength + " " + leaderboard.size());
            leaderboard = expand(leaderboard, aminoMasses);
//            System.out.println("leaderboard:");
//            System.out.println(leaderboard);
            for (Iterator<List<Integer>> iterator = leaderboard.iterator(); iterator.hasNext();) {
                List<Integer> peptide = iterator.next();
                int mass = peptide.stream().mapToInt(Integer::valueOf).sum();
                if (mass == parentMass) {
                    int peptideScore = CyclopeptideScoring.getCyclicScore(peptide, spectrum);
                    if (peptideScore > leaderPeptideScore) {
                        leaderPeptideScore = peptideScore;
                        leaders = new ArrayList<>();
                        leaders.add(peptide);
                    } else if (peptideScore == leaderPeptideScore) {
                        leaders.add(peptide);
                    }

                } else if (mass > parentMass) {
                    iterator.remove();
                }
            }
            leaderboard = trim(leaderboard, spectrum, numberOfPlaces);
            peptideLength++;
        }
        System.out.println(leaderPeptideScore);
        return leaders;
    }

    public static String doWorkSpectrum25(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int numberOfAminos = Integer.parseInt(br.readLine());
            int numberOfPlaces = Integer.parseInt(br.readLine());
            String[] spectrumString = br.readLine().split("\\s+");
            List<Integer> spectrum = Arrays.asList(spectrumString).stream().map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());

            List<Integer> spectralConvolution = SpectralConvolution.getSpectralConvolution(spectrum);
            List<Integer> aminoMasses = getAminoMasses(spectralConvolution, numberOfAminos);
            List<List<Integer>> leaderboard = getLeaderboard(spectrum, numberOfPlaces, aminoMasses);

            return ConsoleCapturer.toString(leaderboard.stream().map(l -> l.stream().map(i -> i.toString()).collect(Collectors.joining("-")))
                    .collect(Collectors.joining(" ")) + "\n" + leaderboard.size());
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
//        String result = doWork("src/test/resources/IandII/dataset_104_7.txt");
        String result = doWorkSpectrum25("/home/slee/working/ucsd-bioinformatics/src/test/resources/IandII/dataset_104_8.txt");
        System.out.println(result);
    }
}
