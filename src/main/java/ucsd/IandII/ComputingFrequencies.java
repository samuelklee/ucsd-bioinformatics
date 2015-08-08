package ucsd.IandII;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ComputingFrequencies {
    /**
     * Returns an integer array of the occurrence counts of all k-mers in a text.
     * @param text  text to search within
     * @param k     length of k-mers to search for
     * @return      array of occurrence counts for all k-mers, indexed in lexicographical order
     */
    public static int[] getPatternFrequencies(String text, int k) {
        int[] patternFrequencies = new int[(int) Math.pow(4, k)];

        for (int i = 0; i <= text.length() - k; i++) {
            String pattern = text.substring(i, i + k);
            long patternNumber = PatternToIndex.getIndex(pattern);
            patternFrequencies[(int) patternNumber]++;
        }
        return patternFrequencies;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String text = br.readLine();
        int k = Integer.parseInt(br.readLine());

        int[] frequencies = getPatternFrequencies(text, k);

        for (Integer frequency : frequencies)
            System.out.print(frequency + " ");
        System.out.println();
    }
}
