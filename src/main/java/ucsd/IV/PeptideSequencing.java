package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PeptideSequencing {
    public static Map<Integer, List<Map.Entry<Integer, String>>> getGraph(List<Integer> spectralVector) {
        List<Integer> spectralVectorWithZero = new ArrayList<>(spectralVector);
        spectralVectorWithZero.add(0, 0);
        Map<Integer, List<Map.Entry<Integer, String>>> graphSpectrum = new HashMap<>();
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTableNoDuplicatesSingleAminos();
        Map<Integer, String> reverseAminoMassTable = new HashMap<>();
        for (String amino : aminoMassTable.keySet()) {
            reverseAminoMassTable.put(aminoMassTable.get(amino), amino);
        }
        for (int i = 0; i < spectralVectorWithZero.size() - 1; i++) {
            graphSpectrum.put(i, new ArrayList<>());
            for (int j = i + 1; j < spectralVectorWithZero.size(); j++) {
                int massDifference = j - i;
                if (reverseAminoMassTable.containsKey(massDifference)) {
                    graphSpectrum.get(i).add(new AbstractMap.SimpleEntry<>(j, reverseAminoMassTable.get(massDifference)));
                }
            }
        }
        return graphSpectrum;
    }

    //add peptides for all paths in graphSpectrum via DFS
    public static void addPeptides(List<Integer> spectralVector, Map<Integer, List<Map.Entry<Integer, String>>> graph,
                                   List<Map.Entry<String, Integer>> scoredPeptides, String peptide, int score, int startNode) {
        if (!graph.containsKey(startNode)) {
            scoredPeptides.add(new AbstractMap.SimpleEntry<>(peptide, score));
        } else {
            for (Map.Entry<Integer, String> edge : graph.get(startNode)) {
                addPeptides(spectralVector, graph, scoredPeptides, peptide + edge.getValue(), score + spectralVector.get(edge.getKey() - 1), edge.getKey());
            }
        }
    }

    public static String getPeptide(List<Integer> spectralVector) {
        Map<Integer, List<Map.Entry<Integer, String>>> graph = getGraph(spectralVector);
//        System.out.println(GraphSpectrum.getGraphSpectrumAsString(graph));

        List<Map.Entry<String, Integer>> scoredPeptides = new ArrayList<>();
        addPeptides(spectralVector, graph, scoredPeptides, "", 0, 0);

        int maxScore = 0;
        String idealPeptide = "";

        for (Map.Entry<String, Integer> scoredPeptide : scoredPeptides) {
//            System.out.println(peptide);
//            System.out.println(spectrum);
            String peptide = scoredPeptide.getKey();
            int score = scoredPeptide.getValue();
            if (score > maxScore) {
                maxScore = score;
                idealPeptide = peptide;
            }
        }
        return idealPeptide;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<Integer> spectralVector = Arrays.asList(br.readLine().split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());

            String result = getPeptide(spectralVector);

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_11813_10.txt");
        System.out.println(result);
    }
}
