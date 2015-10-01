package ucsd.IandII;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConvolutionCyclopeptideSequencingTest {
    @Test
    public void test() {
        String result = ConvolutionCyclopeptideSequencing.doWork("src/test/resources/IandII/sample/ConvolutionCyclopeptideSequencing.txt").split("\n")[1];
//        String expected = "99-71-137-57-72-57";
        String expected = "21";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String resultCyclopeptide = ConvolutionCyclopeptideSequencing.doWork("src/test/resources/IandII/sample/ConvolutionCyclopeptideSequencingExtra.txt").split("\n")[0];
        String expectedCyclopeptide = "113-115-114-128-97-163-131-129-129-147-57-57-129";
        List<Integer> result = Arrays.asList(resultCyclopeptide.split("-")).stream().map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());
        List<Integer> resultReversed = new ArrayList<>(result);
        Collections.reverse(resultReversed);
        List<Integer> doubledExpected = Arrays.asList((expectedCyclopeptide + "-" + expectedCyclopeptide).split("-"))
                .stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        Assert.assertTrue(Collections.indexOfSubList(doubledExpected, result) != -1 ||
                Collections.indexOfSubList(doubledExpected, resultReversed) != -1);
    }
}

