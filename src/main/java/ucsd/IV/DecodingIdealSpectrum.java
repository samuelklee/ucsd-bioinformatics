package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.IandII.PeptideSpectrum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DecodingIdealSpectrum {
    //add peptides for all paths in graphSpectrum via DFS
    public static void addPeptides(Map<Integer, List<Map.Entry<Integer, String>>> graphSpectrum, List<String> peptides, String peptide, int startNode) {
        if (!graphSpectrum.containsKey(startNode)) {
            peptides.add(peptide);
        } else {
            for (Map.Entry<Integer, String> edge : graphSpectrum.get(startNode)) {
                addPeptides(graphSpectrum, peptides, peptide + edge.getValue(), edge.getKey());
            }
        }
    }

    public static List<Integer> getIdealSpectrum(String peptide) {
        List<Integer> idealSpectrum = new ArrayList<>();
        for (int i = 1; i < peptide.length(); i++) {
            String prefix = peptide.substring(0, i);
            String suffix = peptide.substring(i, peptide.length());
            int prefixMass = PeptideSpectrum.getAminoMassesFromPeptide(prefix).stream().mapToInt(Integer::intValue).sum();
            int suffixMass = PeptideSpectrum.getAminoMassesFromPeptide(suffix).stream().mapToInt(Integer::intValue).sum();
            idealSpectrum.add(prefixMass);
            idealSpectrum.add(suffixMass);
        }
        idealSpectrum.add(PeptideSpectrum.getAminoMassesFromPeptide(peptide).stream().mapToInt(Integer::intValue).sum());
        Collections.sort(idealSpectrum);
        return idealSpectrum;
    }

    public static String getIdealPeptide(List<Integer> spectrum) {
        Map<Integer, List<Map.Entry<Integer, String>>> graphSpectrum = GraphSpectrum.getGraphSpectrum(spectrum);
//        System.out.println(GraphSpectrum.getGraphSpectrumAsString(graphSpectrum));

        List<String> peptides = new ArrayList<>();
        addPeptides(graphSpectrum, peptides, "", 0);

        for (String peptide : peptides) {
//            System.out.println(peptide);
//            System.out.println(spectrum);
//            System.out.println(getIdealSpectrum(peptide));
            if (getIdealSpectrum(peptide).equals(spectrum)) {
                return peptide;
            }
        }
        return null;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<Integer> spectrum = Arrays.asList(br.readLine().split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());

            String result = getIdealPeptide(spectrum);

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_11813_4.txt");
        System.out.println(result);
    }
}
