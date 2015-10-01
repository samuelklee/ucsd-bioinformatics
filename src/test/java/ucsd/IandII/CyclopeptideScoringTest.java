package ucsd.IandII;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CyclopeptideScoringTest {
    @Test
    public void test() {
        String result = CyclopeptideScoring.doWork("src/test/resources/IandII/sample/CyclopeptideScoring.txt");
        String expected = "11";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = CyclopeptideScoring.doWork("src/test/resources/IandII/sample/CyclopeptideScoringExtra.txt");
        String expected = "521";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testQuiz() {
        String result = CyclopeptideScoring.doWork("src/test/resources/IandII/sample/CyclopeptideScoringQuiz.txt");
        System.out.println(result);
    }
}

