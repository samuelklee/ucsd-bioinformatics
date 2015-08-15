package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MiddleEdgeTest {
    @Test
    public void test() {
        String result = MiddleEdge.doWork("src/test/resources/III/sample/MiddleEdge.txt");
        String expected = "(4, 3) (5, 4)";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void test2() {
        String result = MiddleEdge.doWork("src/test/resources/III/sample/MiddleEdge2.txt");
        String expected = "(2, 4) (2, 5)";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = MiddleEdge.doWork("src/test/resources/III/sample/MiddleEdgeExtra.txt");
        String expected = "(512, 510) (513, 511)";
        Assert.assertEquals(result, expected);
    }
}

