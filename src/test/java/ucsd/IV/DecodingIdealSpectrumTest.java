package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DecodingIdealSpectrumTest {
    @Test
    public void test() {
        String result = DecodingIdealSpectrum.doWork("src/test/resources/IV/sample/DecodingIdealSpectrum.txt");
        String expected = "GPFNA";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = DecodingIdealSpectrum.doWork("src/test/resources/IV/sample/DecodingIdealSpectrumExtra.txt");
        String expected = "CRQCSLAMQRASQHYVYVWPQETFGFVCRM";
        Assert.assertEquals(result, expected.replace('L', 'I').replace('Q', 'K'));
    }
}