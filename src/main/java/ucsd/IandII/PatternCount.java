package ucsd.IandII;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PatternCount {
    /**
     * Returns the occurrence count of pattern in text.
     * @param text      text to search within
     * @param pattern   pattern to search for
     * @return          occurrence count of pattern in text
     */
    public static int getCount(String text, String pattern) {
        int patternLength = pattern.length();
        int count = 0;

        for (int i = 0; i <= text.length() - patternLength; i++){
            if (text.regionMatches(i, pattern, 0, patternLength)) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String text = br.readLine();
        String pattern = br.readLine();

        int count = getCount(text, pattern);

        System.out.println(count);
    }
}
