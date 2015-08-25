package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SharedkMersTest {
    @Test
    public void test() {
        int result = SharedkMers.doWork("src/test/resources/III/sample/SharedkMers.txt").split("\n").length;
        int expected = 4;
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        int result = SharedkMers.doWork("src/test/resources/III/sample/SharedkMersExtra.txt").split("\n").length;
        int expected = 323;
        Assert.assertEquals(result, expected);
    }
}

