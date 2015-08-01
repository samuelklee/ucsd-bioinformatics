package ucsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IndexToPattern {
    /**
     * Returns base corresponding to an index.
     * @param index     base index (0-3)
     * @return          nucleotide base corresponding to index
     */
    public static char getSymbolFromIndex(long index) {
        switch ((int) index) {
            case 0: return 'A';
            case 1: return 'C';
            case 2: return 'G';
            case 3: return 'T';
        }
        return 'X';
    }

    /**
     * Returns pattern corresponding to lexicographical index.
     * @param index     lexicographical index (e.g., 0 = AA, 1 = AC, 2 = AG, etc.)
     * @param k         length of k-mers to consider
     * @return          k-mer corresponding to index
     */
    public static String getPattern(long index, int k) {
        if (k == 1) {
            return Character.toString(getSymbolFromIndex(index));
        }

        long prefixIndex = index / 4;
        long lastSymbolIndex = index % 4;

        String prefixPattern = getPattern(prefixIndex, k - 1);
        char lastSymbol = getSymbolFromIndex(lastSymbolIndex);

        return prefixPattern + lastSymbol;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        long index = Long.parseLong(br.readLine());
        int k = Integer.parseInt(br.readLine());

        String pattern = getPattern(index, k);

        System.out.println(pattern);
    }
}
