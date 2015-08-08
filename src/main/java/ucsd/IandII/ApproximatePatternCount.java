package ucsd.IandII;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApproximatePatternCount {
    /**
     * Returns the occurrence count of pattern in text, up to an allowed number of mismatches.
     * @param text      text to search within
     * @param pattern   pattern to search for
     * @param distance  limit for number of mismatches
     * @return          occurrence count of pattern in text
     */
    public static int getCount(String text, String pattern, int distance) {
        int patternLength = pattern.length();
        int count = 0;

        for (int i = 0; i <= text.length() - patternLength; i++){
            String testPattern = text.substring(i, i + patternLength);
            if (HammingDistance.getHammingDistance(pattern, testPattern) <= distance) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String text = br.readLine();
        String pattern = br.readLine();
        int distance = Integer.parseInt(br.readLine());

        int count = getCount(text, pattern, distance);

        System.out.println(count);
    }
}
