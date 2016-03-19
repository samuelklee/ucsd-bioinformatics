package ucsd.V;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LloydAlgorithmTest {
    @Test
    public void test() {
        String result = LloydAlgorithm.doWork("src/test/resources/V/sample/LloydAlgorithm.txt");
        String expected = "1.800 2.867\n" +
                "1.060 1.140";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = LloydAlgorithm.doWork("src/test/resources/V/sample/LloydAlgorithmExtra.txt");
        String expected = "7.561 6.167 16.568 6.078 7.096\n" +
                "18.232 6.147 5.468 6.578 6.053\n" +
                "7.037 17.299 6.927 5.495 7.028\n" +
                "7.712 7.233 6.916 18.717 6.838\n" +
                "6.042 6.279 5.708 7.014 17.408\n" +
                "5.158 4.559 5.113 5.144 4.719";
        Assert.assertEquals(result, expected);
    }
}

