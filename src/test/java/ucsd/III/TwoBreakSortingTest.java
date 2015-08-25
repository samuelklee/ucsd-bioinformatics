package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TwoBreakSortingTest {
    @Test
    public void test() {
        String result = TwoBreakSorting.doWork("src/test/resources/III/sample/TwoBreakSorting.txt");
        String expected = "(+1 -2 -3 +4)\n" +
                "(+1 -2 -3)(+4)\n" +
                "(+1 -2 -4 -3)\n" +
                "(-3 +1 +2 -4)";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = TwoBreakSorting.doWork("src/test/resources/III/sample/TwoBreakSortingExtra.txt");
        String expected = "(+9 -8 +12 +7 +1 -14 +13 +3 -5 -11 +6 -2 +10 -4)\n" +
                "(+9 -8 +12 -3 -13 +14 -1 -7 -5 -11 +6 -2 +10 -4)\n" +
                "(+9 -8 +12)(+7 +1 -14 +13 +3 +4 -10 +2 -6 +11 +5)\n" +
                "(+9 -8 +12)(+1 -14 +13 +3 +4 -10 +2 -6 +11)(+5 +7)\n" +
                "(+9 -8 +12)(+13 +3 +4 -10 +2 -6 +11 +1 +14)(+5 +7)\n" +
                "(+9 -8 +12)(+13 +3 +4 -10 +2 -6 +11 +1 +14 -7 -5)\n" +
                "(-8 +12 +9 +5 +7 -14 -1 -11 +6 -2 +10 -4 -3 -13)\n" +
                "(-8 +12 +9 +5 +7 -14 -1 -11 +6 -2 +10)(+13 +3 +4)\n" +
                "(+13 +3 +4)(-11 +6 +12 +9 +5 +7 -14 -1)(-2 +10 -8)\n" +
                "(-2 +10 -8)(+5 +7 -14 -1 -11 +3 +4 +13 +6 +12 +9)\n" +
                "(+5 +7 -14 -1 -11 +10 -8 -2 +3 +4 +13 +6 +12 +9)\n" +
                "(+5 +7 -14 -1 -11 +8 -10 -2 +3 +4 +13 +6 +12 +9)";
        Assert.assertEquals(result, expected);
    }
}

