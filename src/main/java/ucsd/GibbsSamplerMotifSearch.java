package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GibbsSamplerMotifSearch {
    private static final int LARGE_NUMBER = 100000;

    /**
     * Returns a k-mer randomly generated from a given profile probability matrix and a text.  The probability for each
     * k-mer in the text is calculated given the profile, and then one of these k-mers is randomly selected according
     * to these probabilities.
     * @param profile   profile probability matrix
     * @param text      text to search within
     * @return          random k-mer generated from profile
     */
    public static String getProfileGeneratedMotif(double[][] profile, String text) {
        int k = profile[0].length;
        List<Double> probabilities = new ArrayList<>();
        for (int i = 0; i < text.length() - k; i++) {
            String pattern = text.substring(i, i + k);
            probabilities.add(ProfileProbablePattern.getPatternProbability(pattern, profile));
        }
        double totalProbability = probabilities.stream().mapToDouble(Double::doubleValue).sum();
        Random random = new Random();
        double randomProbability = random.nextDouble()*totalProbability;
        double runningSum = 0.;
        for (int i = 0; i < text.length() - k; i++) {
            runningSum += probabilities.get(i);
            if (runningSum > randomProbability) {
                return text.substring(i, i + k);
            }
        }
        return text.substring(text.length() - k);
    }

    /**
     * Returns the k-mer motifs with the lowest score found by a Gibbs-sampling random search, given a list of texts.
     * @param texts         list of texts to search within
     * @param k             length of k-mers
     * @param numSamples    number of samples
     * @param numTexts      number of texts to search within
     * @return              list of k-mer motifs with lowest score
     */
    public static List<String> getMotifs(List<String> texts, int k, int numSamples, int numTexts) {
        int textLength = texts.get(0).length();
        Random random = new Random();
        //get random list of motifs
        List<String> motifs = texts.stream().map(s -> {
            int index = random.nextInt(textLength - k);
            return s.substring(index, index + k);
        }).collect(Collectors.toList());
        List<String> bestMotifs = new ArrayList<>(motifs);
        for (int j = 0; j < numSamples; j++) {
            int i = random.nextInt(numTexts);
            List<String> sublistTexts = new ArrayList<>(motifs);
            sublistTexts.remove(i);
            double[][] profile = GreedyMotifSearch.getProfileWithPseudocounts(sublistTexts);
            motifs.set(i, getProfileGeneratedMotif(profile, texts.get(i)));
            if (GreedyMotifSearch.getScore(motifs) < GreedyMotifSearch.getScore(bestMotifs)) {
                bestMotifs = motifs;
            }
        }
        return bestMotifs;
    }

    /**
     * Returns the k-mer motifs with the lowest score found by a given number of Gibbs sampling iterations,
     * given a list of texts.
     * @param texts         list of texts to search within
     * @param k             length of k-mers
     * @param numTexts      number of texts to search within
     * @param numSamples    number of samples
     * @param numRuns       number of searches
     * @return              list of k-mer motifs with lowest score over searches
     */
    public static List<String> getMotifsFromMultipleSearches(List<String> texts, int k, int numTexts,
                                                             int numSamples, int numRuns) {
        int bestScore = LARGE_NUMBER;
        List<String> bestMotifs = getMotifs(texts, k, numSamples, numTexts);
        for (int run = 0; run < numRuns - 1; run++) {
            List<String> motifs = getMotifs(texts, k, numSamples, numTexts);
            int score = GreedyMotifSearch.getScore(motifs);
            if (score < bestScore) {
                bestMotifs = motifs;
                bestScore = score;
            }
        }
        System.out.println(bestScore);
        return bestMotifs;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String[] inputIntegerStrings = br.readLine().split("\\s+");

        int k = Integer.parseInt(inputIntegerStrings[0]);
        int t = Integer.parseInt(inputIntegerStrings[1]);
        int N = Integer.parseInt(inputIntegerStrings[2]);

        List<String> texts = new ArrayList<>();
        String text;
        while ((text = br.readLine()) != null) {
            texts.add(text);
        }

        List<String> motifs = getMotifsFromMultipleSearches(texts, k, t, N, 20);

        System.out.println(motifs.stream().collect(Collectors.joining("\n")));
    }
}
