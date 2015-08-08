package ucsd.IandII;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PatternToIndex {
    /**
     * Returns lexicographical index corresponding to base.
     * @param symbol    nucleotide base
     * @return          lexicographical index corresponding to symbol
     */
    public static long getIndexFromSymbol(char symbol) {
        switch (symbol) {
            case 'A': return 0;
            case 'C': return 1;
            case 'G': return 2;
            case 'T': return 3;
        }
        return -1;
    }

    /**
     * Returns lexicographical index corresponding to pattern.
     * @param pattern   k-mer
     * @return          lexicographical index (e.g., 0 = AA, 1 = AC, 2 = AG, etc.)
     */
    public static long getIndex(String pattern) {
        int patternLength = pattern.length();

        if (patternLength == 0) {
            return 0;
        }

        char lastSymbol = pattern.charAt(patternLength - 1);
        String prefixPattern = pattern.substring(0, patternLength - 1);

        return 4* getIndex(prefixPattern) + getIndexFromSymbol(lastSymbol);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String pattern = br.readLine();

        long index = getIndex(pattern);

        System.out.println(index);
    }
}
