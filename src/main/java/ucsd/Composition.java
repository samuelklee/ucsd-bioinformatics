package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.SortedSet;
import java.util.TreeSet;

public class Composition {
    /**
     * Returns the k-mers that occur in a text in lexicographical order.
     * @param k     length of k-mers to search for
     * @param text  text to search within
     * @return      array of k-mers occurring in text in lexicographical order
     */
    public static SortedSet<String> getComposition(int k, String text) {
        SortedSet<String> composition = new TreeSet<>();

        for (int i = 0; i <= text.length() - k; i++) {
            String pattern = text.substring(i, i + k);
            composition.add(pattern);
        }

        return composition;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        int k = Integer.parseInt(br.readLine());
        String text = br.readLine();

        SortedSet<String> composition = getComposition(k, text);

        for (String pattern : composition) {
            System.out.println(pattern);
        }
    }
}
