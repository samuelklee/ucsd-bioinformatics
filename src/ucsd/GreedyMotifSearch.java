package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class GreedyMotifSearch {
    /**
     * Returns probability profile matrix, given a list of motifs.  This is a 4 x (motif length) matrix that gives
     * the normalized distribution of base-pair counts at each position over all motifs.
     * @param motifs    motifs to consider
     * @return          probability profile matrix
     */
    public static double[][] getProfile(List<String> motifs) {
        int numMotifs = motifs.size();
        int motifLength = motifs.get(0).length();
        double[][] profile = new double[4][motifLength];
        for (String motif : motifs) {
            for (int j = 0; j < motifLength; j++) {
                char base = motif.charAt(j);
                int baseIndex = (int) PatternToIndex.getIndexFromSymbol(base);
                profile[baseIndex][j] += 1.0 / numMotifs;
            }
        }

        return profile;
    }

    /**
     * Returns probability profile matrix with pseudocounts (i.e., one artificial count is added to each entry in the
     * unnormalized matrix, so that probabilities for patterns are non-vanishing), given a list of motifs.
     * @param motifs    motifs to consider
     * @return          probability profile matrix with pseudocounts
     */
    public static double[][] getProfileWithPseudocounts(List<String> motifs) {
        int numMotifs = motifs.size();
        int motifLength = motifs.get(0).length();
        double[][] profile = new double[4][motifLength];

        for (int baseIndex = 0; baseIndex <= 3; baseIndex++) {
            for (int j = 0; j < motifLength; j++) {
                profile[baseIndex][j] += 1.0 / (numMotifs + 4.0);
            }
        }

        for (String motif : motifs) {
            for (int j = 0; j < motifLength; j++) {
                char base = motif.charAt(j);
                int baseIndex = (int) PatternToIndex.getIndexFromSymbol(base);
                profile[baseIndex][j] += 1.0 / (numMotifs + 4.0);
            }
        }

        return profile;
    }

    /**
     * Returns score of motifs.  This is given by the total number of mismatches from the consensus string
     * (which is constructed by taking the most common base at each position) over all motifs.
     * @param motifs    motifs to consider
     * @return          score
     */
    public static int getScore(List<String> motifs) {
        int score = 0;
        int numMotifs = motifs.size();
        double[][] profile = getProfile(motifs);
        for (int j = 0; j < profile[0].length; j++) {
            List<Double> normalizedBaseCounts = new ArrayList<>(Arrays.asList(profile[0][j],
                    profile[1][j], profile[2][j], profile[3][j]));
            score += numMotifs * (1.0 - Collections.max(normalizedBaseCounts));
        }
        return score;
    }

    /**
     * Returns the k-mer motifs with the lowest score, given a list of texts.
     * @param texts     list of texts to search within
     * @param k         length of k-mers
     * @param numTexts  number of texts to search within
     * @return          list of k-mer motifs with lowest score
     */
    public static List<String> getMotifs(List<String> texts, int k, int numTexts) {
        List<String> bestMotifs = texts.stream().map(s -> s.substring(0, k)).collect(Collectors.toList());

        String firstText = texts.get(0);
        for (int i = 0; i <= firstText.length() - k; i++) {
            String firstMotif = firstText.substring(i, i + k);
            List<String> motifs = new ArrayList<>(Arrays.asList(firstMotif));
            for (int j = 1; j < numTexts; j++) {
                double[][] profile = getProfile(motifs);
                String nextText = texts.get(j);
                String nextMotif = ProfileProbablePattern.getMostProbablePattern(nextText, k, profile);
                motifs.add(nextMotif);
            }
            if (getScore(motifs) < getScore(bestMotifs)) {
                bestMotifs = motifs;
            }
        }

        return bestMotifs;
    }

    /**
     * Returns the k-mer motifs with the lowest score, given a list of texts.  Pseudocounts are included.
     * @param texts     list of texts to search within
     * @param k         length of k-mers
     * @param numTexts  number of texts to search within
     * @return          list of k-mer motifs with lowest score
     */
    public static List<String> getMotifsWithPseudocounts(List<String> texts, int k, int numTexts) {
        List<String> bestMotifs = texts.stream().map(s -> s.substring(0, k)).collect(Collectors.toList());

        String firstText = texts.get(0);
        for (int i = 0; i <= firstText.length() - k; i++) {
            String firstMotif = firstText.substring(i, i + k);
            List<String> motifs = new ArrayList<>(Arrays.asList(firstMotif));
            for (int j = 1; j < numTexts; j++) {
                double[][] profile = getProfileWithPseudocounts(motifs);
                String nextText = texts.get(j);
                String nextMotif = ProfileProbablePattern.getMostProbablePattern(nextText, k, profile);
                motifs.add(nextMotif);
            }
            if (getScore(motifs) < getScore(bestMotifs)) {
                bestMotifs = motifs;
            }
        }

        return bestMotifs;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String[] inputIntegerStrings = br.readLine().split("\\s+");

        int k = Integer.parseInt(inputIntegerStrings[0]);
        int t = Integer.parseInt(inputIntegerStrings[1]);

        List<String> texts = new ArrayList<>();
        String text;
        while ((text = br.readLine()) != null) {
            texts.add(text);
        }

//        List<String> motifs = getMotifs(tezxts, k, t);
        List<String> motifs = getMotifsWithPseudocounts(texts, k, t);

        System.out.println(motifs.stream().collect(Collectors.joining("\n")));
    }
}
