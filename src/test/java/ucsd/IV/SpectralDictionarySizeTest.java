package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SpectralDictionarySizeTest {
//    @Test
//    public void test() {
//        String result = SpectralDictionarySize.doWork("src/test/resources/IV/sample/SpectralDictionarySize.txt");
//        String expected = "3";
//        Assert.assertEquals(result, expected);
//    }

    @Test
    public void testExtra() {
        String result = SpectralDictionarySize.doWork("src/test/resources/IV/sample/SpectralDictionarySizeExtra.txt");
        String expected = "330";
        Assert.assertEquals(result, expected);
    }
}