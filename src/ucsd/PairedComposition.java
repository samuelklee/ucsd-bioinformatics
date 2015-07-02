package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PairedComposition {
    /**
     * Returns the (k,d)-mers that occur in a text in lexicographical order.
     * @param k     length of k-mers to search for
     * @param d     distance between k-mers
     * @param text  text to search within
     * @return      array of (k,d)-mers occurring in text in lexicographical order
     */
    private static List<GappedPattern> getComposition(int k, int d, String text) {
        List<GappedPattern> pairedComposition = new ArrayList<>();

        for (int i = 0; i <= text.length() - (2*k + d); i++) {
            String pattern1 = text.substring(i, i + k);
            String pattern2 = text.substring(i + k + d, i + 2*k + d);
            GappedPattern kdMer = new GappedPattern(pattern1, pattern2);

            pairedComposition.add(kdMer);
        }

        return pairedComposition;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        int k = Integer.parseInt(br.readLine());
        int d = Integer.parseInt(br.readLine());
        String text = br.readLine();

        List<GappedPattern> pairedComposition = getComposition(k, d, text);
        Collections.sort(pairedComposition);

        for (GappedPattern kdMer : pairedComposition) {
            System.out.print("(" + kdMer.getPattern1() + "|" + kdMer.getPattern2() + ") ");
        }
    }
}
