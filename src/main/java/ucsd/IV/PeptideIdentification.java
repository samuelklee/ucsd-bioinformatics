package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;
import ucsd.IandII.MotifEnumeration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PeptideIdentification {
    public static int LARGE_NUMBER = 100000;

//    public static Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTableXZ();
    public static Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTable();

    public static List<Integer> getPeptideVector(String peptide) {
        List<Integer> peptideVector = new ArrayList<>();
        for (String amino : peptide.split("")) {
            int aminoMass = aminoMassTable.get(amino);
            peptideVector.addAll(Collections.nCopies(aminoMass - 1, 0));
            peptideVector.add(1);
        }
        return peptideVector;
    }

    public static int scorePeptide(List<Integer> spectralVector, String peptide) {
        List<Integer> peptideVector = getPeptideVector(peptide);
        if (spectralVector.size() != peptideVector.size()) {
            return -LARGE_NUMBER;
        }

        int score = 0;
        for (int i = 0; i < peptideVector.size(); i++) {
            score += spectralVector.get(i) * peptideVector.get(i);
        }
        return score;
    }

    public static String getIdealPeptide(List<Integer> spectralVector, String proteome) {
        int maxScore = 0;
        String idealPeptide = "";
//        System.out.println(spectralVector.size() / 57.);

//        for (int length = 1; length < spectralVector.size() / 4. + 1; length++) {
        for (int length = 1; length < spectralVector.size() / 57. + 1; length++) {
//            System.out.println(length);
            Set<String> peptides = MotifEnumeration.getPatterns(Collections.singletonList(proteome), length);
            for (String peptide : peptides) {
                int score = scorePeptide(spectralVector, peptide);
                if (score > maxScore) {
                    maxScore = score;
                    idealPeptide = peptide;
//                    System.out.println(peptide);
                }
            }
        }

        return idealPeptide;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<Integer> spectralVector = Arrays.asList(br.readLine().split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());
            String proteome = br.readLine();

            String result = getIdealPeptide(spectralVector, proteome);

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_11866_2.txt");
        System.out.println(result);
    }
}
