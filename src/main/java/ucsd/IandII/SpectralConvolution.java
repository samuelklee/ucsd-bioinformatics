package ucsd.IandII;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SpectralConvolution {
    public static List<Integer> getSpectralConvolution(List<Integer> spectrum) {
        List<Integer> spectralConvolution = new ArrayList<>();
        for (int i = 0; i < spectrum.size() - 1; i++) {
            for (int j = i + 1; j < spectrum.size(); j++) {
                int massDifference = spectrum.get(i) - spectrum.get(j);
                if (massDifference != 0) {
                    spectralConvolution.add(Math.abs(massDifference));
                }
            }
        }
        return spectralConvolution;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            String[] spectrumString = br.readLine().split("\\s+");
            List<Integer> spectrum = Arrays.asList(spectrumString).stream().map(s -> Integer.parseInt(s))
                    .collect(Collectors.toList());

            List<Integer> spectralConvolution = getSpectralConvolution(spectrum);

            return ConsoleCapturer.toString(spectralConvolution.stream().map(i -> i.toString())
                    .collect(Collectors.joining(" ")));
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IandII/dataset_104_4.txt");
        System.out.println(result);
    }
}
