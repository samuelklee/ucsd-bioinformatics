package ucsd.IV;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmallParsimonyTest {
    private static List<String> outputAsList(String output) {
        return Arrays.asList(output.split("\n"));
    }

    @Test
    public void test() {
        String result = SmallParsimony.doWork("src/test/resources/IV/sample/SmallParsimony.txt");
        String expected =
                "16\n" +
                "ATTGCGAC->ATAGCCAC:2\n" +
                "ATAGACAA->ATAGCCAC:2\n" +
                "ATAGACAA->ATGGACTA:2\n" +
                "ATGGACGA->ATGGACTA:1\n" +
                "CTGCGCTG->ATGGACTA:4\n" +
                "ATGGACTA->CTGCGCTG:4\n" +
                "ATGGACTA->ATGGACGA:1\n" +
                "ATGGACTA->ATAGACAA:2\n" +
                "ATAGCCAC->CAAATCCC:5\n" +
                "ATAGCCAC->ATTGCGAC:2\n" +
                "ATAGCCAC->ATAGACAA:2\n" +
                "CAAATCCC->ATAGCCAC:5";
        Assert.assertEquals(outputAsList(result).get(0), outputAsList(expected).get(0));
        Assert.assertEquals(outputAsList(result).size(), outputAsList(expected).size());
    }

    @Test
    public void testExtra() throws IOException {
        String result = SmallParsimony.doWork("src/test/resources/IV/sample/SmallParsimonyExtra.txt");
        String expected = FileUtils.readFileToString(new File("src/test/resources/IV/sample/SmallParsimonyExtraOutput.txt"));
        Assert.assertEquals(outputAsList(result).get(0), outputAsList(expected).get(0));
        Assert.assertEquals(outputAsList(result).size(), outputAsList(expected).size());
    }
}

