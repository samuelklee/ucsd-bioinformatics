package ucsd.IV;

import ucsd.ConsoleCapturer;
import ucsd.DataTableUtils;
import ucsd.IandII.MotifEnumeration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SpectralDictionarySize {
//    public static List<Integer> aminoMasses = new ArrayList<>(DataTableUtils.getAminoMassTableXZ().values());
    public static List<Integer> aminoMasses = new ArrayList<>(DataTableUtils.getAminoMassTable().values());

    public static int getSize(List<Integer> spectralVector, int i, int score) {
        if (i == 0 && score == 0) {
            return 1;
        }
        if (i <= 0 || score < 0) {
            return 0;
        }
        int size = 0;
        for (int mass : aminoMasses) {
            size += getSize(spectralVector, i - mass, score - spectralVector.get(i - 1));
        }
        return size;
    }

    public static int getDictionarySize(List<Integer> spectralVector, int threshold, int maxScore) {
        int size = 0;
        int length = spectralVector.size();
        for (int t = threshold; t <= maxScore; t++) {
            size += getSize(spectralVector, length, t);
//            System.out.println(i + ": " + size);
        }
        return size;
    }

    public static String doWork(String dataFileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFileName))) {
            List<Integer> spectralVector = Arrays.asList(br.readLine().split(" ")).stream().map(Integer::parseInt).collect(Collectors.toList());
            int threshold = Integer.parseInt(br.readLine());
            int maxScore = Integer.parseInt(br.readLine());

            String result = Integer.toString(getDictionarySize(spectralVector, threshold, maxScore));

            return ConsoleCapturer.toString(result);
        } catch (IOException e) {
            throw new RuntimeException("Input file not found.");
        }
    }

    public static void main(String[] args) throws IOException {
        String result = doWork("src/test/resources/IV/dataset_11866_8.txt");
        System.out.println(result);
    }
}
