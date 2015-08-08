package ucsd.IandII;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TextFromPath {
    /**
     * Returns text built from path (list of overlapping strings).
     * @param path  list of overlapping strings
     * @return      text constructed from path
     */
    public static String getTextFromPath(List<String> path) {
        StringBuilder text = new StringBuilder();

        text.append(path.get(0));

        for (String pattern : path.subList(1, path.size())) {
            char lastCh = pattern.charAt(pattern.length() - 1);
            text.append(lastCh);
        }
        return text.toString();
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        List<String> path = new ArrayList<>();

        String read;
        while ((read = br.readLine()) != null) {
            path.add(read);
        }

        String text = getTextFromPath(path);

        System.out.println(text);
    }
}
