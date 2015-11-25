package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;
import ucsd.IandII.MotifEnumeration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PeptideSpectrumMatch {
    public static Set<String> getMatches(List<List<Integer>> spectralVectors, String proteome, int scoreThreshold) {
        Set<String> matches = new HashSet<>();
        int iteration = 1;
        for (List<Integer> spectralVector : spectralVectors) {
            System.out.println(iteration++ + " / " + spectralVectors.size());
            String peptide = PeptideIdentification.getIdealPeptide(spectralVector, proteome);
            if (!peptide.equals("") && PeptideIdentification.scorePeptide(spectralVector, peptide) >= scoreThreshold) {
                matches.add(peptide);
            }
        }
        return matches;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<List<Integer>> spectralVectors = new ArrayList<>();
            String[] input = br.readLine().split(" ");
            while (input.length > 1) {
                spectralVectors.add(Arrays.asList(input).stream().map(Integer::parseInt).collect(Collectors.toList()));
                input = br.readLine().split(" ");
            }
//            System.out.println(spectralVectors);
            String proteome = input[0];
//            System.out.println(proteome);
            int scoreThreshold = Integer.parseInt(br.readLine());

            Set<String> matches = getMatches(spectralVectors, proteome, scoreThreshold);

            String result = matches.stream().collect(Collectors.joining("\n"));

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_11866_5.txt");
        System.out.println(result);
    }
}
