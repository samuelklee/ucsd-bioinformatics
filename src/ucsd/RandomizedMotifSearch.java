package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class RandomizedMotifSearch {
    private static final int LARGE_NUMBER = 100000;

    /**
     * Returns a list of motifs constructed by taking the profile-most probable patterns from each of a list of given
     * texts.
     * @param profile   profile probability matrix
     * @param texts     list of texts to search within
     * @return
     */
    public static List<String> getProfileProbableMotifs(double[][] profile, List<String> texts) {
        int k = profile[0].length;
        return texts.stream().map(s -> ProfileProbablePattern.getMostProbablePattern(s, k, profile))
                    .collect(Collectors.toList());
    }

    /**
     * Returns the k-mer motifs with the lowest score found by a iterative random search, given a list of texts.
     * @param texts     list of texts to search within
     * @param k         length of k-mers
     * @return          list of k-mer motifs with lowest score
     */
    public static List<String> getMotifs(List<String> texts, int k) {
        int textLength = texts.get(0).length();
        Random random = new Random();
        //get random list of motifs
        List<String> bestMotifs = texts.stream().map(s -> {
            int index = random.nextInt(textLength - k);
            return s.substring(index, index + k);
        }).collect(Collectors.toList());

        while (true) {
            double[][] profile = GreedyMotifSearch.getProfileWithPseudocounts(bestMotifs);
            List<String> motifs = getProfileProbableMotifs(profile, texts);
            if (GreedyMotifSearch.getScore(motifs) < GreedyMotifSearch.getScore(bestMotifs)) {
                bestMotifs = motifs;
            } else {
                return bestMotifs;
            }
        }
    }

    /**
     * Returns the k-mer motifs with the lowest score found by a given number of iterative random searches,
     * given a list of texts.
     * @param texts     list of texts to search within
     * @param k         length of k-mers
     * @param numRuns   number of searches
     * @return          list of k-mer motifs with lowest score over searches
     */
    public static List<String> getMotifsFromMultipleSearches(List<String> texts, int k, int numRuns) {
        int bestScore = LARGE_NUMBER;
        List<String> bestMotifs = getMotifs(texts, k);
        for (int run = 0; run < numRuns - 1; run++) {
            List<String> motifs = getMotifs(texts, k);
            int score = GreedyMotifSearch.getScore(motifs);
            if (score < bestScore) {
                bestMotifs = motifs;
                bestScore = score;
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

        List<String> motifs = getMotifsFromMultipleSearches(texts, k, 1000);

        System.out.println(motifs.stream().collect(Collectors.joining("\n")));

//        List<String> dna = Arrays.asList("AAGCCAAA", "AATCCTGG", "GCTACTTG", "ATGTTTTG");
//        List<String> motifs = Arrays.asList("CCA", "CCT", "CTT", "TTG");
//
//        double[][] profile = GreedyMotifSearch.getProfile(motifs);
//        System.out.println(getProfileProbableMotifs(profile, dna));
    }
}
