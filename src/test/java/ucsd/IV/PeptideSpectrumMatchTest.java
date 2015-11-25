package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PeptideSpectrumMatchTest {
    private static Set<String> outputAsSet(String output) {
        return new HashSet<>(Arrays.asList(output.split("\n")));
    }

    @Test
    public void test() {
        String result = PeptideSpectrumMatch.doWork("src/test/resources/IV/sample/PeptideSpectrumMatch.txt");
        String expected = "XZXZ";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = PeptideSpectrumMatch.doWork("src/test/resources/IV/sample/PeptideSpectrumMatchExtra.txt");
        String expected =
                "QQCGVHEYFWVSKK\n" +
                "HTNGPDCSQYQLLK\n" +
                "VIAAGAHPADGQGVRGP\n" +
                "NGMPFCCMCWDVVM\n" +
                "AAPVCLQQMQPKAVL\n" +
                "SIAQIMVEYTVHGH\n" +
                "KMARKRHIHKFLSP\n" +
                "NRAEQFDMTKYCV\n" +
                "ADMCRPCQACTGKAFG\n" +
                "CKFADFDSKTMGVITQ\n" +
                "DETTVPHLVCPWHD\n" +
                "IFWVHEMMYHCE\n" +
                "GWKRGTYEIIFCPP\n" +
                "DGQGVRGPHQIILMVR\n" +
                "TCFAAGAHVMRKGCH\n" +
                "DCQNYMLMHMVETG\n" +
                "CYCMFHTNTARGERK";
        Assert.assertEquals(outputAsSet(result), outputAsSet(expected));
    }
}