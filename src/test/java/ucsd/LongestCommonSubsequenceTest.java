package ucsd;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LongestCommonSubsequenceTest {
    @Test
    public void test() {
        String result = LongestCommonSubsequence.doWork("src/test/resources/III/sample/LongestCommonSubsequence.txt");
        String expected = "AACTGG";
        Assert.assertEquals(result.length(), expected.length());
    }

    @Test
    public void testExtra() {
        String result = LongestCommonSubsequence.doWork("src/test/resources/III/sample/LongestCommonSubsequenceExtra.txt");
        String expected = "ACCGCAGCGTCAATTTACAACGCCGCACCGTAAAGATGGTTTTACAACCCCCTCCCTGTCCGGTTTATTTCTCTAGTCAGGACAAATAAAGTGGTGGGAATACTTTCTCGGACCAGACCACTACTGGTGGTTGACAAGAGTCGGCCCGGAGGGAACTGGTTGTGTTAGTTATGGGCCCCGGAAGGAGAGTTGAGATCGAGTCTATTTGAGTCGAATCACGGCTAACCTATGCACCTACTTGCCGATCCAGTGAACGATACTTATACCATCGCGTAAAAAGGCTAGTCGATATCCTCCAGAGTAGTCTTCTGAGCTAAAAATTCGGGAGATCAAAAATATAAACTGACAGGTAAGCCTACGTCATCAACCCCCGCAAAATTGGAGTGTTTTGGCTAGAAAGAGCACCTTGAAACGGGCCTTTGTGGGTCCACACAGTTTCTGTAAGCTGTTCCACTACGGTCTTATGATCATCGGCAAGTTAGCACCACGCGAGGCGGACCCGGTACTACTCCCCCACGCTCGACATCTTGCGGCTCCTGATTAAGTTGTGTGTCGGGTAAAGAAACTGAGAGA";
        Assert.assertEquals(result.length(), expected.length());
    }

    @Test
    public void testQuiz() {
        String result = LongestCommonSubsequence.doWork("src/test/resources/III/sample/LongestCommonSubsequenceQuiz.txt");
        System.out.println(result);
    }
}

