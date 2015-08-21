package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class DynamicProgrammingChangeTest {
    @Test
    public void test() {
        String result = DynamicProgrammingChange.doWork("src/test/resources/III/sample/DynamicProgrammingChange.txt");
        String expected = "2";
        Assert.assertEquals(result, expected);
    }

    private static int getNumberOfPeptides(int totalMass, List<Integer> aminoMasses) {
        if (totalMass < 0) {
            return 0;
        }
        if (totalMass == 0) {
            return 1;
        }
        int number = 0;
        for (int aminoMass : aminoMasses) {
            number += getNumberOfPeptides(totalMass - aminoMass, aminoMasses);
        }
        return number;
    }

    @Test
    public void testQuiz() {
        System.out.println(getNumberOfPeptides(21, Arrays.asList(2, 3)));
    }
}

