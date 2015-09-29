package ucsd.IandII;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ConvolutionCyclopeptideSequencingTest {
    @Test
    public void test() {
        String result = ConvolutionCyclopeptideSequencing.doWork("src/test/resources/IandII/sample/ConvolutionCyclopeptideSequencing.txt");
        String expected = "99-71-137-57-72-57";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = ConvolutionCyclopeptideSequencing.doWork("src/test/resources/IandII/sample/ConvolutionCyclopeptideSequencingExtra.txt");
        String expected = "113-115-114-128-97-163-131-129-129-147-57-57-129";
        Assert.assertEquals(result, expected);
    }
}

