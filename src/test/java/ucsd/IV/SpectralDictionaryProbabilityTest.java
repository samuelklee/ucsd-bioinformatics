package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;
import ucsd.DataTableUtils;

import java.util.ArrayList;
import java.util.List;

public class SpectralDictionaryProbabilityTest {
    @Test
    public void test() {
        double aminoProbability = 1. / 2.;
        List<Integer> aminoMasses = new ArrayList<>(DataTableUtils.getAminoMassTableXZ().values());

        String result = SpectralDictionaryProbability.doWork("src/test/resources/IV/sample/SpectralDictionaryProbability.txt", aminoProbability, aminoMasses);
        String expected = "0.375";
        Assert.assertEquals(Double.parseDouble(result), Double.parseDouble(expected), 0.00001);
    }

    @Test
    public void testExtra() {
        double aminoProbability = 1. / 20.;
        List<Integer> aminoMasses = new ArrayList<>(DataTableUtils.getAminoMassTable().values());
        String result = SpectralDictionaryProbability.doWork("src/test/resources/IV/sample/SpectralDictionaryProbabilityExtra.txt", aminoProbability, aminoMasses);
        String expected = "0.00132187890625";
        Assert.assertEquals(Double.parseDouble(result), Double.parseDouble(expected), 0.00001);
    }
}