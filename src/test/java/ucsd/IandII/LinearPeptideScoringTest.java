package ucsd.IandII;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LinearPeptideScoringTest {
    @Test
    public void test() {
        String result = LinearPeptideScoring.doWork("src/test/resources/IandII/sample/LinearPeptideScoring.txt");
        String expected = "8";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = LinearPeptideScoring.doWork("src/test/resources/IandII/sample/LinearPeptideScoringExtra.txt");
        String expected = "274";
        Assert.assertEquals(result, expected);
    }
}

