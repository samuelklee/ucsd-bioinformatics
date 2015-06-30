package ucsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class FrequentWords {
    /**
     * Returns k-mers in text that occur most frequently.
     * @param text  text to search within
     * @param k     length of k-mers to consider
     * @return      set of k-mers that occur most frequently
     */
    public static Set<String> getWords(String text, int k) {
        Set<String> frequentPatterns = new HashSet<String>();

        int[] count = new int[text.length() - k + 1];

        for (int i = 0; i <= text.length() - k; i++) {
            String pattern = text.substring(i, i + k);
            count[i] = PatternCount.getCount(text, pattern);
        }
        int maxCount = Arrays.stream(count).max().getAsInt();

        for (int i = 0; i <= text.length() - k; i++) {
            if (count[i] == maxCount) {
                String pattern = text.substring(i, i + k);
                frequentPatterns.add(pattern);
            }
        }

        return frequentPatterns;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String text = br.readLine();
        int k = Integer.parseInt(br.readLine());

        Set<String> frequentPatterns = getWords(text, k);

        for (String pattern : frequentPatterns)
            System.out.print(pattern + " ");
        System.out.println();
    }
}
