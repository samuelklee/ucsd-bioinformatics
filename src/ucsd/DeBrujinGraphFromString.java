package ucsd;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class DeBrujinGraphFromString {
    /**
     * Return the de Brujin graph of a text for k-mer length k as an adjacency list.  Cycle through k-1-mers in text
     * and connect with edges, then group by origin nodes.  Return as sorted hash map with nodes as keys and list of
     * outgoing edges as values.
     * @param text  input text
     * @param k     length of k-mers to assign to edges
     * @return      adjacency list for nodes in de Brujin graph
     */
    public static Map<String, List<String>> getDeBrujinAdjacency(String text, int k) {
        Map<String, List<String>> deBrujinAdjacency = new TreeMap<>();

        for (int i = 0; i < text.length() - (k - 1); i++) {
            String originNode = text.substring(i, i + (k - 1));
            String destinationNode = text.substring(i + 1, i + 1 + (k - 1));
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

        int k = Integer.parseInt(br.readLine());
        String text = br.readLine();

        Map<String, List<String>> deBrujinAdjacency = getDeBrujinAdjacency(text, k);

        for (Map.Entry<String, List<String>> nodeEdges : deBrujinAdjacency.entrySet()) {
            System.out.print(nodeEdges.getKey() + " -> ");
            String edges = StringUtils.join(nodeEdges.getValue(), ",");
            System.out.println(edges);
        }
    }
}
