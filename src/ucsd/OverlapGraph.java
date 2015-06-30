package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class OverlapGraph {
    /**
     * Check to see if two strings overlap.
     * @param a first string
     * @param b second string
     * @return  boolean giving overlap
     */
    private static boolean isOverlap(String a, String b) {
        return a.substring(1, a.length() - 1).equals(b.substring(0, b.length() - 2));
    }

    /**
     * Returns adjacency list of collection of k-mers that overlap.
     * @param patterns  collection of k-mers
     * @return          adjacency list for patterns
     */
    public static List<AbstractMap.SimpleEntry<String, String>> getOverlapAdjacency(List<String> patterns) {
        List<AbstractMap.SimpleEntry<String, String>> overlapAdjacency = new ArrayList<>();
        for (int i = 0; i < patterns.size(); i++) {
            for (int j = 0; j < patterns.size(); j++) {
                if (i != j) {
                    String a = patterns.get(i);
                    String b = patterns.get(j);
                    if (isOverlap(a, b)) {
                        overlapAdjacency.add(new AbstractMap.SimpleEntry<>(a, b));
                    }

                }
            }
        }
        return overlapAdjacency;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        List<String> patterns = new ArrayList<>();

        String read;
        while ((read = br.readLine()) != null) {
            patterns.add(read);
        }

        List<AbstractMap.SimpleEntry<String, String>> overlapAdjacency = getOverlapAdjacency(patterns);

        for (AbstractMap.SimpleEntry<String, String> edge : overlapAdjacency) {
            System.out.println(edge.getKey() + " -> " + edge.getValue());
        }
    }
}
