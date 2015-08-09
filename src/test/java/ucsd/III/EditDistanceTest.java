package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class EditDistanceTest {
    @Test
    public void test() {
        String result = EditDistance.doWork("src/test/resources/III/sample/EditDistance.txt");
        String expected = "5";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = EditDistance.doWork("src/test/resources/III/sample/EditDistanceExtra.txt");
        String expected = "400";
        Assert.assertEquals(result, expected);
    }
}

