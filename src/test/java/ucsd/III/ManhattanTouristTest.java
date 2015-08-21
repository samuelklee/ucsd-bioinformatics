package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ManhattanTouristTest {
    @Test
    public void test() {
        String result = ManhattanTourist.doWork("src/test/resources/III/sample/ManhattanTourist.txt");
        String expected = "34";

        System.out.println(result);
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = ManhattanTourist.doWork("src/test/resources/III/sample/ManhattanTouristExtra.txt");
        String expected = "84";

        System.out.println(result);
        Assert.assertEquals(result, expected);
    }
}

