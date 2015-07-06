package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProfileProbablePattern {
    /**
     * Returns probability of a k-mer given a probability profile matrix.
     * @param pattern   k-mer to consider
     * @param profile   probability profile matrix
     * @return          probability of pattern
     */
    public static double getPatternProbability(String pattern, double[][] profile) {
        double probability = 1.0;
        for (int i = 0; i < pattern.length(); i++) {
            char base = pattern.charAt(i);
            probability *= profile[(int) PatternToIndex.getIndexFromSymbol(base)][i];
        }
        return probability;
    }

    /**
     * Returns most probable k-mer in a text given a probability profile matrix.
     * @param text      text to consider
     * @param k         length of k-mer
     * @param profile   probability profile matrix
     * @return          most probable k-mer
     */
    public static String getMostProbablePattern(String text, int k, double[][] profile) {
        double maxProbability = 0.0;
        String mostProbablePattern = text.substring(0, k);
        for (int i = 0; i <= text.length() - k; i++) {
            String pattern = text.substring(i, i + k);
            double probability = getPatternProbability(pattern, profile);
            if (probability > maxProbability) {
                maxProbability = probability;
                mostProbablePattern = pattern;
            }
        }
        return mostProbablePattern;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String text = br.readLine();
        int k = Integer.parseInt(br.readLine());

        double[][] profile = new double[4][k];
        for (int base = 0; base < 4; base++) {
            String[] profileRowString = br.readLine().split("\\s+");
            for(int j = 0; j < k; j++) {
                profile[base][j] = Double.parseDouble(profileRowString[j]);
            }
        }

        String mostProbablePattern = getMostProbablePattern(text, k, profile);
        System.out.println(mostProbablePattern);
    }
}
