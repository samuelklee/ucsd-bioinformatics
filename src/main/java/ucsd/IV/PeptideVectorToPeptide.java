package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PeptideVectorToPeptide {
    public static List<String> getPeptide(List<Integer> peptideVector) {
        List<String> peptide = new ArrayList<>();
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTable();
        Map<Integer, String> reverseAminoMassTable = new HashMap<>();
        for (String amino : aminoMassTable.keySet()) {
            reverseAminoMassTable.put(aminoMassTable.get(amino), amino);
        }
        int aminoMass = 0;
        for (int bit : peptideVector) {
            if (bit == 0) {
                aminoMass++;
            } else {
                peptide.add(reverseAminoMassTable.get(aminoMass + 1));
                aminoMass = 0;
            }
        }
        return peptide;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<Integer> peptideVector = Arrays.asList(br.readLine().split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());

            List<String> peptide = getPeptide(peptideVector);
            String result = peptide.stream().collect(Collectors.joining(""));

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_11813_8.txt");
        System.out.println(result);
    }
}
