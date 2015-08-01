package ucsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FrequentWordsWithMismatches {
    /**
     * Returns k-mers in text that occur most frequently, up to an allowed number of mismatches.
     * @param text      text to search within
     * @param k         length of k-mers to consider
     * @param distance  limit for number of mismatches
     * @return          set of k-mers that occur most frequently
     */
    public static Set<String> getWords(String text, int k, int distance) {
        Set<String> frequentPatterns = new HashSet<String>();

        int numberOfKmers = (int) Math.pow(4, k);

        int[] count = new int[numberOfKmers];
        String[] kmers = new String[numberOfKmers];

        for (int i = 0; i < numberOfKmers; i++) {
            kmers[i] = IndexToPattern.getPattern(i, k);
            count[i] = ApproximatePatternCount.getCount(text, kmers[i], distance);
        }
        int maxCount = Arrays.stream(count).max().getAsInt();

        for (int i = 0; i < numberOfKmers; i++) {
            if (count[i] == maxCount) {
                frequentPatterns.add(kmers[i]);
            }
        }

        return frequentPatterns;
    }

    /**
     * Returns k-mers in text that occur most frequently, counting reverse complements,
     * up to an allowed number of mismatches.
     * @param text      text to search within
     * @param k         length of k-mers to consider
     * @param distance  limit for number of mismatches
     * @return          set of k-mers that occur most frequently
     */
    public static Set<String> getWordsWithReverseComplement(String text, int k, int distance) {
        Set<String> frequentPatterns = new HashSet<String>();

        int numberOfKmers = (int) Math.pow(4, k);

        int[] count = new int[numberOfKmers];
        String[] kmers = new String[numberOfKmers];

        for (int i = 0; i < numberOfKmers; i++) {
            kmers[i] = IndexToPattern.getPattern(i, k);
            count[i] = ApproximatePatternCount.getCount(text, kmers[i], distance)
                    + ApproximatePatternCount.getCount(text, ReverseComplement.getReverseComplement(kmers[i]), distance);
        }
        int maxCount = Arrays.stream(count).max().getAsInt();

        for (int i = 0; i < numberOfKmers; i++) {
            if (count[i] == maxCount) {
                frequentPatterns.add(kmers[i]);
            }
        }

        return frequentPatterns;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String text = br.readLine();
        String[] inputIntegerStrings = br.readLine().split("\\s+");

        int k = Integer.parseInt(inputIntegerStrings[0]);
        int distance = Integer.parseInt(inputIntegerStrings[1]);

        Set<String> frequentPatterns = getWordsWithReverseComplement(text, k, distance);

        for (String pattern : frequentPatterns)
            System.out.print(pattern + " ");
        System.out.println();
    }
}
