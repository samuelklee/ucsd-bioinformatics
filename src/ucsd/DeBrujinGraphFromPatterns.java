package ucsd;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class DeBrujinGraphFromPatterns {
    /**
     * Return the de Brujin graph for a set of patterns as an adjacency list.  Get OverlapGraph adjacency list and then
     * group by keys.  Return as sorted hash map with nodes as keys and list of outgoing edges as values.
     * @param patterns  set of k-mers
     * @return          adjacency list for nodes in de Brujin graph
     */
    public static Map<Object, List<Object>> getDeBrujinAdjacency(List<String> patterns) {
        Map<Object, List<Object>> deBrujinAdjacency = new TreeMap<>();

        for (String pattern : patterns) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            String suffix = pattern.substring(1);
            Object originNode = prefix;
            Object destinationNode = suffix;
            if (deBrujinAdjacency.containsKey(originNode)) {
                deBrujinAdjacency.get(originNode).add(destinationNode);
            } else {
                deBrujinAdjacency.put(originNode, new ArrayList<>(Arrays.asList(destinationNode)));
            }
        }

        return deBrujinAdjacency;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader((new InputStreamReader(System.in)));

        List<String> patterns = new ArrayList<>();

        String read;
        while ((read = br.readLine()) != null) {
            patterns.add(read);
        }

        Map<Object, List<Object>> deBrujinAdjacency = getDeBrujinAdjacency(patterns);

        for (Map.Entry<Object, List<Object>> nodeEdges : deBrujinAdjacency.entrySet()) {
            System.out.print(nodeEdges.getKey() + " -> ");
            String edges = StringUtils.join(nodeEdges.getValue(), ",");
            System.out.println(edges);
        }
    }
}
