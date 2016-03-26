package ucsd.V;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SoftKMeansClusteringTest {
    @Test
    public void test() {
        String result = SoftKMeansClustering.doWork("src/test/resources/V/sample/SoftKMeansClustering.txt");
        String expected = "1.662 2.623\n" +
                "1.075 1.148";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = SoftKMeansClustering.doWork("src/test/resources/V/sample/SoftKMeansClusteringExtra.txt");
        String expected = "5.889 16.921 6.873\n" +
                "20.404 8.236 9.055\n" +
                "3.590 4.853 4.970\n" +
                "11.329 5.448 5.319\n" +
                "5.761 6.494 17.227";
        Assert.assertEquals(result, expected);
    }
}

