package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ChromosomeToCycleTest {
    @Test
    public void test() {
        String result = ChromosomeToCycle.doWork("src/test/resources/III/sample/ChromosomeToCycle.txt");
        String expected = "(1 2 4 3 6 5 7 8)";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = ChromosomeToCycle.doWork("src/test/resources/III/sample/ChromosomeToCycleExtra.txt");
        String expected = "(1 2 3 4 6 5 8 7 9 10 12 11 13 14 15 16 18 17 19 20 22 21 24 23 25 26 28 27 29 30 32 31 33 34 35 36 37 38 40 39 41 42 44 43 46 45 48 47 49 50 51 52 53 54 56 55 57 58 60 59 62 61 63 64 65 66 67 68 70 69 71 72 73 74 75 76 78 77 79 80 82 81 84 83 85 86 88 87 90 89 92 91 94 93 95 96 97 98 100 99 101 102 104 103 106 105 108 107 109 110 111 112 113 114 116 115 118 117 120 119 121 122 123 124 125 126 128 127 129 130 131 132 133 134 136 135 138 137)";
        Assert.assertEquals(result, expected);
    }
}

