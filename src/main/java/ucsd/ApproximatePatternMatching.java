package ucsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ApproximatePatternMatching {
    /**
     * Returns list of positions at which pattern appears in text, up to an allowed number of mismatches.
     * @param text      text to search within
     * @param pattern   pattern to search for
     * @param distance  limit for number of mismatches
     * @return          list of 0-indexed positions at which pattern begins in text
     */
    public static List<Integer> getPositions(String text, String pattern, int distance) {
        int patternLength = pattern.length();
        List<Integer> matchPositions = new ArrayList<Integer>();

        for (int i = 0; i <= text.length() - patternLength; i++){
            String testPattern = text.substring(i, i + patternLength);
            if (HammingDistance.getHammingDistance(pattern, testPattern) <= distance) {
                matchPositions.add(i);
            }
        }
        return matchPositions;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String pattern = br.readLine();
        String text = br.readLine();
        int distance = Integer.parseInt(br.readLine());

        List<Integer> patternMatchPositions = getPositions(text, pattern, distance);

        for (int position : patternMatchPositions)
            System.out.print(position + " ");
        System.out.println();
    }
}
