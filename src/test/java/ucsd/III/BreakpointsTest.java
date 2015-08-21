package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BreakpointsTest {
    @Test
    public void test() {
        String result = Breakpoints.doWork("src/test/resources/III/sample/Breakpoints.txt");
        String expected = "8";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = Breakpoints.doWork("src/test/resources/III/sample/BreakpointsExtra.txt");
        String expected = "178";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testQuiz() {
        String result = Breakpoints.doWork("src/test/resources/III/sample/BreakpointsQuiz.txt");
        System.out.println(result);
    }
}

