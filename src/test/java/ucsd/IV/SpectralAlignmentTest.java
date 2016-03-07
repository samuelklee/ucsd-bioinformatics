package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;
import ucsd.DataTableUtils;

import java.util.Map;

public class SpectralAlignmentTest {
    @Test
    public void test() {
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTableXZ();
        String result = SpectralAlignment.doWork("src/test/resources/IV/sample/SpectralAlignment.txt", aminoMassTable);
        String expected = "XX(-1)Z(+2)";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        Map<String, Integer> aminoMassTable = DataTableUtils.getAminoMassTable();
        String result = SpectralAlignment.doWork("src/test/resources/IV/sample/SpectralAlignmentExtra.txt", aminoMassTable);
        String expected = "L(+2)V(+9)WSTE(-12)";
        Assert.assertEquals(result, expected);
    }
}