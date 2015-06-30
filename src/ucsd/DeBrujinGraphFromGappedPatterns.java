package ucsd;


import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DeBrujinGraphFromGappedPatterns {
    /**
     * Return the de Brujin graph for a set of gapped patterns as an adjacency list.  Get OverlapGraph adjacency list and then
     * group by keys.  Return as sorted hash map with nodes as keys and list of outgoing edges as values.
     * @param gappedPatterns    set of (k,d)-mers
     * @return                  adjacency list for nodes in de Brujin graph
     */
    public static Map<Object, List<Object>> getDeBrujinAdjacency(List<GappedPattern> gappedPatterns) {
        Map<Object, List<Object>> deBrujinAdjacency = new TreeMap<>();

        for (GappedPattern gappedPattern : gappedPatterns) {
            String pattern1 = gappedPattern.getPattern1();
            String pattern2 = gappedPattern.getPattern2();
            String prefix1 = pattern1.substring(0, pattern1.length() - 1);
            String prefix2 = pattern2.substring(0, pattern2.length() - 1);
            String suffix1 = pattern1.substring(1, pattern1.length());
            String suffix2 = pattern2.substring(1, pattern2.length());

            GappedPattern prefix = new GappedPattern(prefix1, prefix2);
            GappedPattern suffix = new GappedPattern(suffix1, suffix2);
            Object originNode = prefix;
            Object destinationNode = suffix;
            if (deBrujinAdjacency.containsKey(originNode)) {
                deBrujinAdjacency.get(originNode).add(destinationNode);
            } else {
                List<Object> edgeList = new ArrayList<>();
                edgeList.add(destinationNode);
                deBrujinAdjacency.put(originNode, edgeList);
            }
        }

        return deBrujinAdjacency;
    }
}
