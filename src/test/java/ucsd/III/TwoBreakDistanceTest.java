package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TwoBreakDistanceTest {
    @Test
    public void test() {
        String result = TwoBreakDistance.doWork("src/test/resources/III/sample/TwoBreakDistance.txt");
        String expected = "3";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = TwoBreakDistance.doWork("src/test/resources/III/sample/TwoBreakDistanceExtra.txt");
        String expected = "9671";
        Assert.assertEquals(result, expected);
    }
}

