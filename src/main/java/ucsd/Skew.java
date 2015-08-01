package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Skew {
    private static Map<String, Map<Integer, Integer>> skewMapCache = new HashMap<>();

    /**
     * Returns skew (#G-#C) of text at a given position.
     * @param position  position to compute skew at
     * @param text      text to compute skew for
     * @return          skew of text at position
     */
    public static int getSkew(int position, String text) {
        Map<Integer, Integer> skewCache;
        if (skewMapCache.containsKey(text)) {
            skewCache = skewMapCache.get(text);
        } else {
            skewCache = new HashMap<>();
            skewMapCache.put(text, skewCache);
        }

        if (skewCache.containsKey(position)) {
            return skewCache.get(position);
        } else {
            int result;
            if (position == 0) {
                result = 0;
            } else {
                char ch = text.charAt(position - 1);

                if (ch == 'G') {
                    result = getSkew(position - 1, text) + 1;
                } else if (ch == 'C') {
                    result = getSkew(position - 1, text) - 1;
                } else {
                    result = getSkew(position - 1, text);
                }
            }
            skewCache.put(position, result);
            return result;
        }
    }

    private static void fillCache(String text) {
        for (int i = 0; i <= text.length(); i++) {
            getSkew(i, text);
        }
    }

    /**
     * Returns list of positions of minimum skew (#G-#C) in a text.
     * @param text      text to search within
     * @return          list of positions of minimum skew
     */
    public static List<Integer> getMinSkewPositions(String text) {
        List<Integer> minSkewPositions = new ArrayList<>();
        fillCache(text);
        Map<Integer, Integer> skewCache = skewMapCache.get(text);
        int minSkew = Collections.min(skewCache.values());

        for (int position : skewCache.keySet()) {
            if (skewCache.get(position) == minSkew) {
                minSkewPositions.add(position);
            }
        }
        return minSkewPositions;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String text = br.readLine();

//        Exercise break
//        for (int i = 0; i <= text.length(); i++) {
//            System.out.print(getSkew(i, text) + " ");
//        }
//        System.out.println();

        List<Integer> minSkewPositions = getMinSkewPositions(text);
        for (int position : minSkewPositions) {
            System.out.print(position + " ");
        }
        System.out.println();
    }
}
