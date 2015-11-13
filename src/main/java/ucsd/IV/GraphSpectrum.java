package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GraphSpectrum {
    public static Map<Integer, List<Map.Entry<Integer, String>>> getGraphSpectrum(List<Integer> spectrum) {
        List<Integer> spectrumWithZero = new ArrayList<>(spectrum);
        spectrumWithZero.add(0, 0);
        Map<Integer, List<Map.Entry<Integer, String>>> graphSpectrum = new HashMap<>();
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTableNoDuplicatesSingleAminos();
        Map<Integer, String> reverseAminoMassTable = new HashMap<>();
        for (String amino : aminoMassTable.keySet()) {
            reverseAminoMassTable.put(aminoMassTable.get(amino), amino);
        }
        for (int i = 0; i < spectrumWithZero.size() - 1; i++) {
            graphSpectrum.put(spectrumWithZero.get(i), new ArrayList<>());
            for (int j = i + 1; j < spectrumWithZero.size(); j++) {
                int massDifference = spectrumWithZero.get(j) - spectrumWithZero.get(i);
                if (reverseAminoMassTable.containsKey(massDifference)) {
                    graphSpectrum.get(spectrumWithZero.get(i)).add(new AbstractMap.SimpleEntry<>(spectrumWithZero.get(j), reverseAminoMassTable.get(massDifference)));
                }
            }
        }
        return graphSpectrum;
    }

    public static String getGraphSpectrumAsString(Map<Integer, List<Map.Entry<Integer, String>>> graphSpectrum) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer origin : graphSpectrum.keySet()) {
            for (Map.Entry<Integer, String> edge : graphSpectrum.get(origin)) {
                stringBuilder.append(origin);
                stringBuilder.append("->");
                stringBuilder.append(edge.getKey());
                stringBuilder.append(":");
                stringBuilder.append(edge.getValue());
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<Integer> spectrum = Arrays.asList(br.readLine().split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());

            Map<Integer, List<Map.Entry<Integer, String>>> graphSpectrum = getGraphSpectrum(spectrum);
            String result = getGraphSpectrumAsString(graphSpectrum);

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_11813_2.txt");
        System.out.println(result);
    }
}
