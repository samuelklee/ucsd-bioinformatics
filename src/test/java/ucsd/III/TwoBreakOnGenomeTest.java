package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TwoBreakOnGenomeTest {
    @Test
    public void test() {
        String result = TwoBreakOnGenome.doWork("src/test/resources/III/sample/TwoBreakOnGenome.txt");
        String expected = "(+1 -2)(-3 +4)";
        Set<Integer> resultSet = Arrays.stream(result.split("\\)\\("))
                .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split(" "))
                        .map(Integer::valueOf)
                        .mapToInt(Math::abs)
                        .sum())
                .collect(Collectors.toSet());
        Set<Integer> expectedSet = Arrays.stream(expected.split("\\)\\("))
                .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split(" "))
                        .map(Integer::valueOf)
                        .mapToInt(Math::abs)
                        .sum())
                .collect(Collectors.toSet());
        Assert.assertEquals(resultSet, expectedSet);
    }

    @Test
    public void testExtra() {
        String result = TwoBreakOnGenome.doWork("src/test/resources/III/sample/TwoBreakOnGenomeExtra.txt");
        String expected = "(-67 -1 +2 -3 -4 -5 -6 -7 +8 +9 +10 +11 +12 +13 +14 +15 -16 -17 -18 +19 -20 +21 +22 -23 -24 +25 -26 -27 +28 -29 -30 +31 +32 +33 -34 -35 +36 -59 -60 -61 +62 -63 +64 +65 -66)(+58 +37 +38 +39 -40 +41 +42 -43 +44 +45 +46 -47 +48 +49 +50 -51 +52 +53 +54 -55 -56 -57)";
        Set<Integer> resultSet = Arrays.stream(result.split("\\)\\("))
                .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split(" "))
                        .map(Integer::valueOf)
                        .mapToInt(Math::abs)
                        .sum())
                .collect(Collectors.toSet());
        Set<Integer> expectedSet = Arrays.stream(expected.split("\\)\\("))
                .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split(" "))
                        .map(Integer::valueOf)
                        .mapToInt(Math::abs)
                        .sum())
                .collect(Collectors.toSet());
        Assert.assertEquals(resultSet, expectedSet);
    }
}

