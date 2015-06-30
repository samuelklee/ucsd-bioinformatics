package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class StringSpelledByGappedPatterns {
    /**
     * Returns a string reconstructed from (k,d)-mers.
     * @param k                 length of k-mers
     * @param d                 distance between k-mers
     * @param gappedPatterns    list of (k,d)-mers
     * @return                  text reconstructed from (k,d)-mers
     */
    private static String getString(int k, int d, List<GappedPattern> gappedPatterns) {
        List<String> firstPatterns = new ArrayList<>();
        List<String> secondPatterns = new ArrayList<>();

        for (GappedPattern gappedPattern : gappedPatterns) {
            firstPatterns.add(gappedPattern.getPattern1());
            secondPatterns.add(gappedPattern.getPattern2());
        }

        String prefix = TextFromPath.getTextFromPath(firstPatterns);
        String suffix = TextFromPath.getTextFromPath(secondPatterns);

        for (int i = k + d + 1; i < prefix.length(); i++) {
            if (prefix.charAt(i) != suffix.charAt(i - (k + d))) {
                throw new RuntimeException("There is no possible reconstruction of the gapped patterns.");
            }
        }

        return prefix + suffix.substring(suffix.length() - (k + d), suffix.length());
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String[] inputIntegerStrings = br.readLine().split("\\s+");

        int k = Integer.parseInt(inputIntegerStrings[0]);
        int d = Integer.parseInt(inputIntegerStrings[1]);

        List<GappedPattern> gappedPatterns = new ArrayList<>();
        String gappedPatternString;
        while ((gappedPatternString = br.readLine()) != null) {
            String[] patterns = gappedPatternString.split("\\|");
            gappedPatterns.add(new GappedPattern(patterns[0], patterns[1]));
        }

        String text = getString(k, d, gappedPatterns);
        System.out.println(text);
    }
}
