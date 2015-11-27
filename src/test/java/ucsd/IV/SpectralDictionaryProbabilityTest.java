package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SpectralDictionaryProbabilityTest {
    @Test
    public void test() {
        String result = SpectralDictionaryProbability.doWork("src/test/resources/IV/sample/SpectralDictionaryProbability.txt");
        String expected = "0.375";
        Assert.assertEquals(Double.parseDouble(result), Double.parseDouble(expected), 0.00001);
    }

    @Test
    public void testExtra() {
        String result = SpectralDictionaryProbability.doWork("src/test/resources/IV/sample/SpectralDictionaryProbabilityExtra.txt");
        String expected = "0.00132187890625";
        Assert.assertEquals(Double.parseDouble(result), Double.parseDouble(expected), 0.00001);
    }
}