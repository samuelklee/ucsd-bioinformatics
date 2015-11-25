package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;

public class PeptideIdentificationTest {
//    @Test
//    public void test() {
//        String result = PeptideIdentification.doWork("src/test/resources/IV/sample/PeptideIdentification.txt");
//        String expected = "ZXZXX";
//        Assert.assertEquals(result, expected);
//    }

    @Test
    public void testExtra() {
        String result = PeptideIdentification.doWork("src/test/resources/IV/sample/PeptideIdentificationExtra.txt");
        String expected = "KLEAARSCFSTRNE";
        Assert.assertEquals(result, expected);
    }
}