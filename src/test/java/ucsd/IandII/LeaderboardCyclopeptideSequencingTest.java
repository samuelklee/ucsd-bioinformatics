package ucsd.IandII;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderboardCyclopeptideSequencingTest {
    @Test
    public void test() {
        String resultCyclopeptide = LeaderboardCyclopeptideSequencing.doWork("src/test/resources/IandII/sample/LeaderboardCyclopeptideSequencing.txt").split("\n")[0];
        String expectedCyclopeptide = "113-147-71-129";
        List<Integer> result = Arrays.asList(resultCyclopeptide.split("-")).stream().map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());
        List<Integer> resultReversed = new ArrayList<>(result);
        Collections.reverse(resultReversed);
        List<Integer> doubledExpected = Arrays.asList((expectedCyclopeptide + "-" + expectedCyclopeptide).split("-"))
                .stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        Assert.assertTrue(Collections.indexOfSubList(doubledExpected, result) != -1 ||
                Collections.indexOfSubList(doubledExpected, resultReversed) != -1);
    }

    @Test
    public void testExtra() {
        String resultScore = LeaderboardCyclopeptideSequencing.doWork("src/test/resources/IandII/sample/LeaderboardCyclopeptideSequencingExtra.txt").split("\n")[1];
//        String expectedCyclopeptide = "97-129-97-147-99-71-186-71-113-163-115-71-113-128-103-87-128-101-137-163-114";
        String expectedScore = "421";
        Assert.assertEquals(resultScore, expectedScore);
    }
}

