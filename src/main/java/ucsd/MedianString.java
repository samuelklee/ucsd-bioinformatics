package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MedianString {
    private static final int LARGE_NUMBER = 100000;
    /**
     * Returns minimum distance for pattern vs. all k-mers in texts (i.e., sum of minimum Hamming distance over k-mers
     * in each text).
     * @param texts     texts to search within
     * @param pattern   pattern to compare against
     * @return
     */
    public static int getDistanceBetweenPatternAndTexts(List<String> texts, String pattern) {
        int k = pattern.length();
        int distance = 0;
        for (String text : texts) {
            int minHammingDistance = LARGE_NUMBER;
            for (int i = 0; i <= text.length() - k; i++) {
                String patternPrime = text.substring(i, i + k);
                int hammingDistance = HammingDistance.getHammingDistance(pattern, patternPrime);
                if (hammingDistance < minHammingDistance) {
                    minHammingDistance = hammingDistance;
                }
            }
            distance += minHammingDistance;
        }
        return distance;
    }

    public static String getMedian(List<String> texts, int k) {
        int minPatternDistance = LARGE_NUMBER;
        String median = null;
        for (int i = 0; i <= (int) Math.pow(4, k) - 1; i++) {
            String pattern = IndexToPattern.getPattern(i, k);
            int patternDistance = getDistanceBetweenPatternAndTexts(texts, pattern);
            if (patternDistance < minPatternDistance) {
                minPatternDistance = patternDistance;
                median = pattern;
            }
        }
        return median;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

//        String pattern = br.readLine();
//        String[] inputTexts = br.readLine().split("\\s+");
//
//        List<String> texts = new ArrayList<>();
//        for (String text : inputTexts) {
//            texts.add(text);
//        }
//
//        int distance = getDistanceBetweenPatternAndTexts(texts, pattern);
//        System.out.println(distance);

        int k = Integer.parseInt(br.readLine());

        List<String> texts = new ArrayList<>();

        String text;
        while ((text = br.readLine()) != null) {
            texts.add(text);
        }

        String median = getMedian(texts, k);

        System.out.println(median);
    }
}
