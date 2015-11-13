package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PeptideSequencingTest {
    @Test
    public void test() {
        String result = PeptideSequencing.doWork("src/test/resources/IV/sample/PeptideSequencing.txt");
        String expected = "XZZXX";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = PeptideSequencing.doWork("src/test/resources/IV/sample/PeptideSequencingExtra.txt");
        String expected = "GGPGGPGGAGG";
        Assert.assertEquals(result, expected);
    }
}