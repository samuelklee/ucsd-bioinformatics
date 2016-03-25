package ucsd.V;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HierarchichalClusteringTest {
    private static Set<String> outputAsSet(String output) {
        return new HashSet<>(Arrays.asList(output.split("\n")));
    }

    @Test
    public void test() {
        String result = HierarchichalClustering.doWork("src/test/resources/V/sample/HierarchichalClustering.txt");
        String expected =
                "4 6\n" +
                "5 7\n" +
                "3 4 6\n" +
                "1 2\n" +
                "5 7 3 4 6\n" +
                "1 2 5 7 3 4 6";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = HierarchichalClustering.doWork("src/test/resources/V/sample/HierarchichalClusteringExtra.txt");
        String expected =
                "1 18\n" +
                "3 9\n" +
                "4 6\n" +
                "5 11\n" +
                "15 4 6\n" +
                "2 1 18\n" +
                "16 20\n" +
                "10 14\n" +
                "7 19\n" +
                "13 3 9\n" +
                "12 2 1 18\n" +
                "8 12 2 1 18\n" +
                "17 16 20\n" +
                "7 19 17 16 20\n" +
                "15 4 6 13 3 9\n" +
                "5 11 10 14\n" +
                "8 12 2 1 18 15 4 6 13 3 9\n" +
                "7 19 17 16 20 8 12 2 1 18 15 4 6 13 3 9\n" +
                "5 11 10 14 7 19 17 16 20 8 12 2 1 18 15 4 6 13 3 9";
        Assert.assertEquals(result, expected);
    }
}