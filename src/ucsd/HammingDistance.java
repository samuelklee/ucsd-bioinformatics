package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HammingDistance {
    /**
     * Returns the Hamming distance (number of mismatches) between two strings.
     * @param s first string
     * @param t second string
     * @return  Hamming distance between strings
     */
    public static int getHammingDistance(String s, String t) {
        int hammingDistance = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != t.charAt(i)) {
                hammingDistance++;
            }
        }
        return hammingDistance;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String s = br.readLine();
        String t = br.readLine();

        int hammingDistance = getHammingDistance(s, t);
        System.out.println(hammingDistance);
    }
}
