package ucsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * Created by slee on 26/06/15.
 */
public class Neighbors {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String text = br.readLine();

        int distance = Integer.parseInt(br.readLine());

        int count = 0;
        for (int i = 0; i < Math.pow(4, text.length()); i++)
            if (HammingDistance.getHammingDistance(text, IndexToPattern.getPattern(i, text.length())) <= distance)
                count++;
        System.out.println(count);
    }
}
