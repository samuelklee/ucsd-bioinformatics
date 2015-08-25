package ucsd.IandII;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ReverseComplement {
    public static Map<Character, Character> complementMap = new HashMap<>();
    static {
        complementMap.put('A', 'T');
        complementMap.put('C', 'G');
        complementMap.put('G', 'C');
        complementMap.put('T', 'A');
    }


    /**
     * Returns the complement of a string (i.e., A->T, C->G, G->C, T->A)
     * @param pattern   string
     * @return          reversed string
     */
    public static String getComplement(String pattern) {
        StringBuilder complement = new StringBuilder(pattern);
        for (int i = 0; i < pattern.length(); i++) {
            complement.setCharAt(i, complementMap.get(pattern.charAt(i)));
        }
        return complement.toString();
    }

    /**
     * Returns the reverse complement of a string.
     * @param pattern   string
     * @return          string complement
     */
    public static String getReverseComplement(String pattern) {
        String reverse = new StringBuilder(pattern).reverse().toString();
        return getComplement(reverse);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String pattern = br.readLine();
        String patternReverseComplement = getReverseComplement(pattern);

        System.out.println(patternReverseComplement);
    }
}
