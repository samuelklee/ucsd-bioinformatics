package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;
import ucsd.DataTableUtils;

import java.util.ArrayList;
import java.util.List;

public class SpectralDictionarySizeTest {
    @Test
    public void test() {
        List<Integer> aminoMasses = new ArrayList<>(DataTableUtils.getAminoMassTableXZ().values());
        String result = SpectralDictionarySize.doWork("src/test/resources/IV/sample/SpectralDictionarySize.txt", aminoMasses);
        String expected = "3";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        List<Integer> aminoMasses = new ArrayList<>(DataTableUtils.getAminoMassTable().values());
        String result = SpectralDictionarySize.doWork("src/test/resources/IV/sample/SpectralDictionarySizeExtra.txt", aminoMasses);
        String expected = "330";
        Assert.assertEquals(result, expected);
    }
}