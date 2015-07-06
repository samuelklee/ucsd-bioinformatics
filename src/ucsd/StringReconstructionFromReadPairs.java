package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringReconstructionFromReadPairs {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        String[] inputIntegerStrings = br.readLine().split("\\s+");

        int k = Integer.parseInt(inputIntegerStrings[0]);
        int d = Integer.parseInt(inputIntegerStrings[1]);

        List<GappedPattern> gappedPatterns = new ArrayList<>();
        String gappedPatternString;
        while ((gappedPatternString = br.readLine()) != null) {
            String[] patterns = gappedPatternString.split("\\|");
            gappedPatterns.add(new GappedPattern(patterns[0], patterns[1]));
        }

        Map<Object, List<Object>> deBrujinAdjacency = DeBrujinGraphFromGappedPatterns.getDeBrujinAdjacency(gappedPatterns);

        LinkedList<Object> nodesVisited = EulerianPath.getNodesVisited(deBrujinAdjacency);
        String text = StringSpelledByGappedPatterns.getString(k, d, nodesVisited.stream().map(o -> (GappedPattern) o).collect(Collectors.toList()));
        System.out.println(text);
    }
}
