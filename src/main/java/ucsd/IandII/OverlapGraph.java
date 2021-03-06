package ucsd.IandII;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OverlapGraph {
    /**
     * Check to see if two strings overlap by a single character.
     * @param a first string
     * @param b second string
     * @return  boolean giving overlap
     */
    private static boolean isOverlap(String a, String b) {
        return a.substring(1, a.length() - 1).equals(b.substring(0, b.length() - 2));
    }

    /**
     * Returns adjacency list of collection of k-mers that overlap by a single character.
     * @param patterns  collection of k-mers
     * @return          adjacency list for patterns
     */
    public static List<Map.Entry<String, String>> getOverlapAdjacency(List<String> patterns) {
        List<Map.Entry<String, String>> overlapAdjacency = new ArrayList<>();
        for (String pattern1 : patterns) {
            for (String pattern2 : patterns) {
                if (!pattern1.equals(pattern2)) {
                    if (isOverlap(pattern1, pattern2)) {
                        overlapAdjacency.add(new AbstractMap.SimpleEntry<>(pattern1, pattern2));
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

        List<Map.Entry<String, String>> overlapAdjacency = getOverlapAdjacency(patterns);

        for (Map.Entry<String, String> edge : overlapAdjacency) {
            System.out.println(edge.getKey() + " -> " + edge.getValue());
        }
    }
}
