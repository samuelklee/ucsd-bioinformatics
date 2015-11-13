package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PeptideToPeptideVector {
    public static List<Integer> getPeptideVector(List<String> peptide) {
        List<Integer> peptideVector = new ArrayList<>();
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTable();
        for (String amino : peptide) {
            int aminoMass = aminoMassTable.get(amino);
            peptideVector.addAll(Collections.nCopies(aminoMass - 1, 0));
            peptideVector.add(1);
        }
        return peptideVector;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<String> peptide = Arrays.asList(br.readLine().split(""));

            List<Integer> peptideVector = getPeptideVector(peptide);
            String result = peptideVector.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(" "));

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_11813_6.txt");
        System.out.println(result);
    }
}
