package ucsd;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContigGeneration {
    public static String sortString(String string) {
        char[] chars = string.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        List<String> patterns = new ArrayList<>();

        String read;
        while ((read = br.readLine()) != null) {
            patterns.add(read);
        }

        Map<Object, List<Object>> graph = DeBrujinGraphFromPatterns.getDeBrujinAdjacency(patterns);

        List<List<Object>> paths = MaximalNonBranchingPaths.getPaths(graph);

        for (List<Object> path : paths) {
            String contig = TextFromPath.getTextFromPath(path.stream().map(o -> o.toString()).collect(Collectors.toList()));
            System.out.println(contig);
        }
    }
}
