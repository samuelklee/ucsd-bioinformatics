package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AffineAlignmentTest {
    @Test
    public void test() {
        String result = AffineAlignment.doWork("src/test/resources/III/sample/AffineAlignment.txt");
        String expected = "8\n" +
                "PRT---EINS\n" +
                "PRTWPSEIN-";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = AffineAlignment.doWork("src/test/resources/III/sample/AffineAlignmentExtra.txt").split("\n")[0];
        String expected = "144";
        //alignments might not match
        Assert.assertEquals(result, expected);
    }
}

