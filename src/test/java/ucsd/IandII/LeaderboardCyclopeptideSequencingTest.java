package ucsd.IandII;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LeaderboardCyclopeptideSequencingTest {
    @Test
    public void test() {
        String result = LeaderboardCyclopeptideSequencing.doWork("src/test/resources/IandII/sample/LeaderboardCyclopeptideSequencing.txt");
        String expected = "113-147-71-129";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = LeaderboardCyclopeptideSequencing.doWork("src/test/resources/IandII/sample/LeaderboardCyclopeptideSequencingExtra.txt");
        String expected = "97-129-97-147-99-71-186-71-113-163-115-71-113-128-103-87-128-101-137-163-114";
        Assert.assertEquals(result, expected);
    }
}

