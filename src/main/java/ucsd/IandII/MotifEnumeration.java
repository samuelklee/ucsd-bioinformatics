package ucsd.IandII;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MotifEnumeration {
    /**
     * Returns set of k-mers present in list of texts.
     * @param texts list of texts to search within
     * @param k     length of k-mer
     * @return      set of k-mers
     */
    public static Set<String> getPatterns(List<String> texts, int k) {
        Set<String> patterns = new HashSet<>();
        for (String text : texts) {
            for (int i = 0; i <= text.length() - k; i++){
                patterns.add(text.substring(i, i + k));
            }
        }
        return patterns;
    }

    /**
     * Returns whether a k-mer is present (up to d mismatches) in every text in a list.
     * @param texts     lists of texts to search within
     * @param pattern   pattern to search for
     * @param d         Hamming distance
     * @return          whether k-mer is present
     */
    public static boolean isPatternInAllTextsWithMismatches(List<String> texts, String pattern, int d) {
        for (String text : texts) {
            if (ApproximatePatternCount.getCount(text, pattern, d) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns set of (k,d)-motifs in list of texts.  A (k,d)-motif is a k-mer that appears with less than d
     * mismatches in every text.
     * @param texts list of texts to search within
     * @param k     length of k-mers
     * @param d     Hamming distance
     * @return      set of (k,d)-motifs
     */
    public static Set<String> getMotifs(List<String> texts, int k, int d) {
        Set<String> motifs = new HashSet<>();
        Set<String> patterns = getPatterns(texts, k);
        for (String pattern : patterns) {
            for (String patternNeighbor : Neighbors.getNeighbors(pattern, d)) {
                if (isPatternInAllTextsWithMismatches(texts, patternNeighbor, d)) {
                    motifs.add(patternNeighbor);
                }
            }
        }
        return motifs;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String[] inputIntegerStrings = br.readLine().split("\\s+");

        int k = Integer.parseInt(inputIntegerStrings[0]);
        int d = Integer.parseInt(inputIntegerStrings[1]);

        List<String> texts = new ArrayList<>();
        String text;
        while ((text = br.readLine()) != null) {
            texts.add(text);
        }

        Set<String> motifs = getMotifs(texts, k, d);

        System.out.println(motifs.stream().collect(Collectors.joining(" ")));
    }
}
