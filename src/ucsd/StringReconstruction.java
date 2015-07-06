package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringReconstruction {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        int k = Integer.parseInt(br.readLine());

        List<String> patterns = new ArrayList<>();

        String read;
        while ((read = br.readLine()) != null) {
            patterns.add(read);
        }

        Map<Object, List<Object>> deBrujinAdjacency = DeBrujinGraphFromPatterns.getDeBrujinAdjacency(patterns);


        LinkedList<Object> nodesVisited = EulerianPath.getNodesVisited(deBrujinAdjacency);
        String text = TextFromPath.getTextFromPath(nodesVisited.stream().map(o -> o.toString()).collect(Collectors.toList()));
        System.out.println(text);
    }
}
