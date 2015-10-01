package ucsd.IandII;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardCyclopeptideSequencing {
    public static List<String> expand(List<String> peptides, List<String> aminos) {
        List<String> expandedPeptides = new ArrayList<>();
        for (String peptide : peptides) {
            for (String amino : aminos) {
                StringBuilder newPeptide = new StringBuilder(peptide);
                newPeptide.append(amino);
                expandedPeptides.add(newPeptide.toString());
            }
        }
        return expandedPeptides;
    }

    public static List<Integer> getLeaderPeptide(List<Integer> spectrum, int numberOfPlaces, List<String> aminos) {
        List<String> leaderboard = new ArrayList<>(Arrays.asList(""));
        int parentMass = Collections.max(spectrum);
        List<Integer> leaderPeptide = new ArrayList<>();
        int leaderPeptideScore = 0;
        int peptideLength = 1;
        while (!leaderboard.isEmpty() && peptideLength < 50) {
            System.out.println(peptideLength);
            leaderboard = expand(leaderboard, aminos);
//            System.out.println("leaderboard:");
//            System.out.println(leaderboard);
            for (Iterator<String> iterator = leaderboard.iterator(); iterator.hasNext();) {
                String peptideString = iterator.next();
                List<Integer> peptide = PeptideSpectrum.getAminoMassesFromPeptide(peptideString);
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
            leaderboard = TrimLeaderboard.trim(leaderboard, spectrum, numberOfPlaces);
            peptideLength++;
        }
        return leaderPeptide;
    }

    public static List<Integer> getLeaderPeptide(List<Integer> spectrum, int numberOfPlaces) {
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTableNoDuplicatesSingleAminos();
        List<String> aminos = new ArrayList<>(aminoMassTable.keySet());

        return getLeaderPeptide(spectrum, numberOfPlaces, aminos);
    }

    public static List<String> getLeaderboard(List<Integer> spectrum, int numberOfPlaces, List<String> aminos) {
        List<String> leaderboard = new ArrayList<>(Arrays.asList(""));
        List<String> leaders = new ArrayList<>();
        int parentMass = Collections.max(spectrum);
        int leaderPeptideScore = 0;
        int peptideLength = 1;
        while (!leaderboard.isEmpty() && peptideLength < 50) {
            System.out.println(peptideLength + " " + leaderboard.size());
            leaderboard = expand(leaderboard, aminos);
//            System.out.println("leaderboard:");
//            System.out.println(leaderboard);
            for (Iterator<String> iterator = leaderboard.iterator(); iterator.hasNext();) {
                String peptideString = iterator.next();
                List<Integer> peptide = PeptideSpectrum.getAminoMassesFromPeptide(peptideString);
                int mass = peptide.stream().mapToInt(Integer::valueOf).sum();
                if (mass == parentMass) {
                    int peptideScore = CyclopeptideScoring.getCyclicScore(peptide, spectrum);
                    if (peptideScore > leaderPeptideScore) {
                        leaderPeptideScore = peptideScore;
                        leaders = new ArrayList<>();
                        leaders.add(peptideString);
                    } else if (peptideScore == leaderPeptideScore) {
                        leaders.add(peptideString);
                    }

                } else if (mass > parentMass) {
                    iterator.remove();
                }
            }
            leaderboard = TrimLeaderboard.trim(leaderboard, spectrum, numberOfPlaces);
            peptideLength++;
        }
        System.out.println(leaderPeptideScore);
        return leaders;
    }

    public static List<String> getLeaderboard(List<Integer> spectrum, int numberOfPlaces) {
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTableNoDuplicatesSingleAminos();
        List<String> aminos = new ArrayList<>(aminoMassTable.keySet());

        return getLeaderboard(spectrum, numberOfPlaces, aminos);
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int numberOfPlaces = Integer.parseInt(br.readLine());
            String[] spectrumString = br.readLine().split("\\s+");
            List<Integer> spectrum = Arrays.asList(spectrumString).stream().map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());

            List<Integer> leaderPeptide = getLeaderPeptide(spectrum, numberOfPlaces);
            int score = CyclopeptideScoring.getCyclicScore(leaderPeptide, spectrum);

            return ConsoleCapturer.toString(leaderPeptide.stream().map(i -> i.toString())
                    .collect(Collectors.joining("-")) + "\n" + score);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static String doWorkSpectrum25(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            int numberOfPlaces = Integer.parseInt(br.readLine());
            String[] spectrumString = br.readLine().split("\\s+");
            List<Integer> spectrum = Arrays.asList(spectrumString).stream().map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());

            List<String> leaderboard = getLeaderboard(spectrum, numberOfPlaces);

            return ConsoleCapturer.toString(leaderboard.stream().map(s -> PeptideSpectrum.getAminoMassesFromPeptide(s).stream().map(i -> i.toString()).collect(Collectors.joining("-")))
                    .collect(Collectors.joining(" ")) + "\n" + leaderboard.size());
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
//        String result = doWork("src/test/resources/IandII/dataset_102_7.txt");
//        String result = doWorkSpectrum25("/home/slee/working/ucsd-bioinformatics/src/test/resources/IandII/Tyrocidine_B1_Spectrum_10.txt");
        String result = doWorkSpectrum25("/home/slee/working/ucsd-bioinformatics/src/test/resources/IandII/spectrum_25_tyrocidine_b1.txt");
        System.out.println(result);
    }
}
