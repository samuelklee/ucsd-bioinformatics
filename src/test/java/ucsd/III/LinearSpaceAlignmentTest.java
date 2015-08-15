package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LinearSpaceAlignmentTest {
    @Test
    public void test() {
        String result = LinearSpaceAlignment.doWork("src/test/resources/III/sample/LinearSpaceAlignment.txt");
        String expected = "8\n" +
                "PLEASANTLY\n" +
                "-MEA--N-LY";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void test2() {
        String result = LinearSpaceAlignment.doWork("src/test/resources/III/sample/LinearSpaceAlignment2.txt");
        String expected = "2\n" +
                "PLEA---LY\n" +
                "-MEASSSLY";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = LinearSpaceAlignment.doWork("src/test/resources/III/sample/LinearSpaceAlignmentExtra.txt").split("\n")[0];
        String expected = "1387";
        //alignments may not match
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testGlobalAlignmentExtra() {
        String result = LinearSpaceAlignment.doWork("src/test/resources/III/sample/GlobalAlignmentExtra.txt").split("\n")[0];
        String expected = "1555";
        //alignments may not match
        Assert.assertEquals(result, expected);
    }
}

