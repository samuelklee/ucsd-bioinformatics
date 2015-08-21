package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LongestDAGPathTest {
    @Test
    public void test() {
        String result = LongestDAGPath.doWork("src/test/resources/III/sample/LongestDAGPath.txt");
        String expected = "9\n" + "0->2->3->4";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = LongestDAGPath.doWork("src/test/resources/III/sample/LongestDAGPathExtra.txt");
        String expected = "62\n" + "0->14->29->44";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testQuiz() {
        String result = LongestDAGPath.doWork("src/test/resources/III/sample/LongestDAGPathQuiz.txt");
        System.out.println(result);
    }
}

