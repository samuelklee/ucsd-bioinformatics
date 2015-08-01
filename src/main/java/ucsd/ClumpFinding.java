package ucsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class ClumpFinding {
    /**
     * Returns the set of k-mers that occur in clumps within windows in a text,
     * for a given occurrence threshold and window length.
     * @param text  text to search within
     * @param k     length of k-mers to search for
     * @param L     window length
     * @param t     occurrence threshold for clumps
     *              (i.e., number of times a k-mer must appear in window to qualify as appearing in a clump)
     * @return      set of k-mers occurring in clumps
     */
    public static Set<String> getClumpPatterns(String text, int k, int L, int t) {
        Set<String> frequentPatterns = new HashSet<>();

        int numberOfkmers = (int) Math.pow(4, k);
        boolean[] isClump = new boolean[numberOfkmers];

        //compute frequencies in first window
        String window = text.substring(0, L);
        int[] patternFrequencies = ComputingFrequencies.getPatternFrequencies(window, k);

        //identify patterns (indexed by i) in clumps in first window
        for (int i = 0; i <= numberOfkmers - 1; i++) {
            if (patternFrequencies[i] >= t) {
                isClump[i] = true;
            }
        }

        //slide window down text and adjust frequencies for first and last patterns in window
        for (int i = 1; i <= text.length() - L; i++) {
            String firstPattern = text.substring(i - 1, i - 1 + k);
            int firstPatternToNumber = (int) PatternToIndex.getIndex(firstPattern);
            patternFrequencies[firstPatternToNumber] = patternFrequencies[firstPatternToNumber] - 1;
            String lastPattern = text.substring(i + L - k, i + L);
            int lastPatternToNumber = (int) PatternToIndex.getIndex(lastPattern);
            patternFrequencies[lastPatternToNumber] = patternFrequencies[lastPatternToNumber] + 1;

            if (patternFrequencies[lastPatternToNumber] >= t) {
                isClump[lastPatternToNumber] = true;
            }
        }

        //add patterns in clumps from all windows to frequentPatterns
        for (int i = 0; i <= numberOfkmers - 1; i++) {
            if (isClump[i]) {
                String pattern = IndexToPattern.getPattern((long) i, k);
                frequentPatterns.add(pattern);
            }
        }

        return frequentPatterns;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String text = br.readLine();
        String[] inputIntegerStrings = br.readLine().split("\\s+");

        int k = Integer.parseInt(inputIntegerStrings[0]); //find k-mer patterns
        int L = Integer.parseInt(inputIntegerStrings[1]); //window size
        int t = Integer.parseInt(inputIntegerStrings[2]); //clump threshold for number of k-mers in windows

        Set<String> clumpPatterns = getClumpPatterns(text, k, L, t);

        for (String pattern : clumpPatterns)
            System.out.print(pattern + " ");
        System.out.println();

        System.out.println(clumpPatterns.size());
    }
}
