package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class OverlapAlignmentTest {
    @Test
    public void test() {
        String result = OverlapAlignment.doWork("src/test/resources/III/sample/OverlapAlignment.txt");
        String expected = "1\n" +
                "HEAE\n" +
                "HEAG";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = OverlapAlignment.doWork("src/test/resources/III/sample/OverlapAlignmentExtra.txt");
        String expected = "13\n" +
                "TACCTG-TCGTAACTTGTGC-CA-TTCT-AGG-CCCGTTTTCAC-TT-GCGCTT-ATGATCATGGTTCCGCTGATCTATATGGGCCGGGTAGGGCACTCC-CAGATGAAGGGGAGTAATG--GTAGCCGGATCCAAGTGACGCGC-CCTAGCGGCTCC-GGAGTTTGATAGACGTCGTG---C-TAT--GGAGCGTTGGAGCGACAAC--GCGCTCGTGCTCTGGAAGG-TCGC-TGCT-GATCCGT-AA\n" +
                "TAC-TGGTCCTGACCCAC-CTCACTT-TGATGTCCCCTTTTCTCGTTTGCGCATCAAGATC-TGGC-CCGCA-A-CTAT-TGG-CCGTGAAAGGCACTCATCA-ATAAAGAC-AGTACTCACGCGGTCGGATCCAAATG-CGCGCACCGAGCGGC-CCAGGAGTT-GATAG-CGTCGAGTAACCTATTAGGA-C-TCG-AG-G-CAACTCGCGCTC-T-CTCAGGA-GGCTCGCCTGCTAG-TCCGTGAA";
        //alignments might not match
        Assert.assertEquals(result, expected);
    }
}

