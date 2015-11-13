package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PeptideVectorToPeptideTest {
    @Test
    public void testExtra() {
        String result = PeptideVectorToPeptide.doWork("src/test/resources/IV/sample/PeptideVectorToPeptideExtra.txt");
        String expected = "FRVLRCLEDQPLGLMAQPHRTMPDHPQFTGDAHHMYATC";
        Assert.assertEquals(result, expected);
    }
}