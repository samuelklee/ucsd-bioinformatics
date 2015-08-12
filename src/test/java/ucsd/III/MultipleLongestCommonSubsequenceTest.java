package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MultipleLongestCommonSubsequenceTest {
    @Test
    public void test() {
        String result = MultipleLongestCommonSubsequence.doWork("src/test/resources/III/sample/MultipleLongestCommonSubsequence.txt").split("\n")[0];
        String expected = "3";
        //alignments may not match
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = MultipleLongestCommonSubsequence.doWork("src/test/resources/III/sample/MultipleLongestCommonSubsequenceExtra.txt").split("\n")[0];
        String expected = "11";
        //alignments may not match
        Assert.assertEquals(result, expected);
    }
}

