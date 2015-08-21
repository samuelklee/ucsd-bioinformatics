package ucsd.III;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class GreedySortingTest {
    @Test
    public void test() {
        String result = GreedySorting.doWork("src/test/resources/III/sample/GreedySorting.txt");
        String expected = "(-1 -4 +3 +5 -2)\n" +
                "(+1 -4 +3 +5 -2)\n" +
                "(+1 +2 -5 -3 +4)\n" +
                "(+1 +2 +3 +5 +4)\n" +
                "(+1 +2 +3 -4 -5)\n" +
                "(+1 +2 +3 +4 -5)\n" +
                "(+1 +2 +3 +4 +5)";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = GreedySorting.doWork("src/test/resources/III/sample/GreedySortingExtra.txt");
        try {
            String expected = FileUtils.readFileToString(new File("src/test/resources/III/sample/GreedySortingExtraOutput.txt"));
            Assert.assertEquals(result, expected);
        } catch (IOException e) {
            throw new RuntimeException("Error with test file:", e);
        }
    }

    @Test
    public void testQuiz() {
        String result = GreedySorting.doWork("src/test/resources/III/sample/GreedySortingQuiz.txt");
        System.out.println(result);
    }
}

