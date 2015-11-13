package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PeptideVectorToPeptideTest {
    @Test
    public void test() {
        String result = PeptideVectorToPeptide.doWork("src/test/resources/IV/sample/PeptideVectorToPeptide.txt");
        String expected = "XZZXX";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = PeptideVectorToPeptide.doWork("src/test/resources/IV/sample/PeptideVectorToPeptideExtra.txt");
        String expected = "FRVLRCLEDQPLGLMAQPHRTMPDHPQFTGDAHHMYATC";
        Assert.assertEquals(result, expected);
    }
}