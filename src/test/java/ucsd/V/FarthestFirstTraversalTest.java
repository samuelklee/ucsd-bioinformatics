package ucsd.V;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FarthestFirstTraversalTest {
    @Test
    public void test() {
        String result = FarthestFirstTraversal.doWork("src/test/resources/V/sample/FarthestFirstTraversal.txt");
        String expected = "0.0 0.0\n" +
                        "5.0 5.0\n" +
                        "0.0 5.0";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = FarthestFirstTraversal.doWork("src/test/resources/V/sample/FarthestFirstTraversalExtra.txt");
        String expected = "0.8 12.0 17.5 0.9 7.2\n" +
                        "0.3 16.4 8.9 34.6 24.6\n" +
                        "32.3 1.9 5.1 16.2 8.8\n" +
                        "23.1 31.1 3.6 0.8 0.3";
        Assert.assertEquals(result, expected);
    }
}

