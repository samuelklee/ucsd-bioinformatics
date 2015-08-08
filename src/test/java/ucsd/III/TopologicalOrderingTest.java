package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;
import ucsd.III.TopologicalOrdering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TopologicalOrderingTest {
    private static boolean isOrderingTopological(List<Object> ordering, Map<Object, List<Object>> graph) {
        for (Object node : ordering) {
            for (Object child : graph.getOrDefault(node, new ArrayList<>())) {
                if (ordering.indexOf(child) == -1) {
                    System.out.println(child + " is missing.");
                    return false;
                }
                if (ordering.indexOf(child) < ordering.indexOf(node)) {
                    System.out.println(child + " came before " + node + ".");
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void test() {
        String dataFileName = "src/test/resources/III/sample/TopologicalOrdering.txt";
        String result = TopologicalOrdering.doWork(dataFileName);
        System.out.println(result);

        Map<Object, List<Object>> graph = TopologicalOrdering.readGraph(dataFileName);
        List<Object> ordering = Arrays.asList(result.split(", "));

        Assert.assertTrue(isOrderingTopological(ordering, graph));
    }

    @Test
    public void testExtra() {
        String dataFileName = "src/test/resources/III/sample/TopologicalOrderingExtra.txt";
        String result = TopologicalOrdering.doWork(dataFileName);
        System.out.println(result);

        Map<Object, List<Object>> graph = TopologicalOrdering.readGraph(dataFileName);
        List<Object> ordering = Arrays.asList(result.split(", "));

        Assert.assertTrue(isOrderingTopological(ordering, graph));
    }
}

