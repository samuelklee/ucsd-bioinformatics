package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FittingAlignmentTest {
    @Test
    public void test() {
        String result = FittingAlignment.doWork("src/test/resources/III/sample/FittingAlignment.txt").split("\\n")[0];
        String expected = "2";
        //alignments might not match
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = FittingAlignment.doWork("src/test/resources/III/sample/FittingAlignmentExtra.txt").split("\\n")[0];
        String expected = "22";
        //alignments might not match
        Assert.assertEquals(result, expected);
    }

//    @Test
//    public void testQuiz() {
//        String result = FittingAlignment.doWork("src/test/resources/III/sample/FittingAlignmentQuiz.txt");
//        System.out.println(result);
//    }
}

