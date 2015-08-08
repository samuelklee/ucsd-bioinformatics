package ucsd.IandII;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Neighbors {
    /**
     * Returns set of neighboring strings within Hamming distance of a pattern.
     * @param pattern   the pattern to find the neighborhood of
     * @param d         Hamming distance
     * @return          set of neighboring strings
     */
    public static Set<String> getNeighbors(String pattern, int d) {
        if (d == 0) {
            return new HashSet<>(Arrays.asList(pattern));
        }
        if (pattern.length() == 1) {
            return new HashSet<>(Arrays.asList("A", "C", "G", "T"));
        }
        Set<String> neighbors = new HashSet<>();
        String suffix = pattern.substring(1);
        Set<String> suffixNeighbors = getNeighbors(suffix, d);
        for (String text : suffixNeighbors) {
            if (HammingDistance.getHammingDistance(suffix, text) < d) {
                for (String base : new HashSet<>(Arrays.asList("A", "C", "G", "T"))) {
                    neighbors.add(base + text);
                }
            } else {
                neighbors.add(pattern.substring(0, 1) + text);
            }
        }
        return neighbors;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String pattern = br.readLine();

        int distance = Integer.parseInt(br.readLine());

        Set<String> neighbors = getNeighbors(pattern, distance);

        for (String neighbor : neighbors) {
            System.out.println(neighbor);
        }
    }
}
