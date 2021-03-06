package ucsd.III;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TwoBreakOnGenomeGraphTest {
    @Test
    public void test() {
        String result = TwoBreakOnGenomeGraph.doWork("src/test/resources/III/sample/TwoBreakOnGenomeGraph.txt");
        String expected = "(2, 4), (3, 1), (7, 5), (6, 8)";
        Set<Set<Integer>> resultSet = Arrays.stream(result.split("\\), "))
                .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split(", "))
                        .map(Integer::valueOf)
                        .collect(Collectors.toSet()))
                .collect(Collectors.toSet());
        Set<Set<Integer>> expectedSet = Arrays.stream(expected.split("\\), "))
                .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split(", "))
                        .map(Integer::valueOf)
                        .collect(Collectors.toSet()))
                .collect(Collectors.toSet());
        Assert.assertEquals(resultSet, expectedSet);
    }

    @Test
    public void testExtra() {
        String result = TwoBreakOnGenomeGraph.doWork("src/test/resources/III/sample/TwoBreakOnGenomeGraphExtra.txt");
        String expected = "(2, 4), (3, 5), (6, 8), (7, 10), (9, 12), (11, 13), (14, 15), (16, 17), (18, 19), (20, 21), (22, 24), (23, 25), (26, 28), (27, 30), (29, 32), (31, 33), (34, 35), (36, 37), (38, 40), (39, 42), (41, 44), (43, 45), (46, 47), (48, 49), (50, 51), (52, 53), (54, 56), (55, 58), (57, 59), (60, 61), (62, 64), (63, 66), (65, 68), (67, 69), (70, 72), (71, 73), (76, 77), (78, 80), (79, 81), (82, 83), (84, 86), (85, 88), (89, 92), (91, 94), (93, 96), (95, 98), (97, 99), (100, 102), (101, 104), (103, 106), (105, 107), (108, 109), (110, 111), (112, 113), (114, 116), (115, 117), (118, 120), (119, 121), (122, 124), (123, 126), (125, 128), (127, 129), (130, 132), (131, 134), (133, 136), (135, 1), (87, 74), (90, 75)";
        Set<Set<Integer>> resultSet = Arrays.stream(result.split("\\), "))
                .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split(", "))
                        .map(Integer::valueOf)
                        .collect(Collectors.toSet()))
                .collect(Collectors.toSet());
        Set<Set<Integer>> expectedSet = Arrays.stream(expected.split("\\), "))
                .map(s -> Arrays.stream(s.replaceAll("\\(|\\)", "").split(", "))
                        .map(Integer::valueOf)
                        .collect(Collectors.toSet()))
                .collect(Collectors.toSet());
        Assert.assertEquals(resultSet, expectedSet);
    }
}

