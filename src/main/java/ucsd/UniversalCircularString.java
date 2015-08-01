package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UniversalCircularString {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        int k = Integer.parseInt(br.readLine());



        int numberOfKmers = (int) Math.pow(2, k);
        List<String> patterns = new ArrayList<>();

        for (int i = 0; i < numberOfKmers; i++) {
            patterns.add(String.format("%" + k + "s", Integer.toBinaryString(i)).replace(" ", "0"));
        }

        Map<Object, List<Object>> deBrujinAdjacency = DeBrujinGraphFromPatterns.getDeBrujinAdjacency(patterns);

        EulerianCycle cycle = new EulerianCycle(deBrujinAdjacency);
        LinkedList<Object> nodesVisited = cycle.getNodesVisited(patterns.get(0).substring(0, k - 1));
        String text = TextFromPath.getTextFromPath(nodesVisited.stream().map(o -> o.toString()).collect(Collectors.toList()));

        int lastZeroIndex = text.length();
        for (int i = 0; i < k; i++) {
            if (text.charAt(text.length() - i - 1) != '0') {
                lastZeroIndex = text.length() - i;
                break;
            }
        }
        System.out.println(text.substring(0, lastZeroIndex));
    }
}
