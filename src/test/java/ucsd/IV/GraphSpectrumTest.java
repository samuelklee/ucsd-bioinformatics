package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GraphSpectrumTest {
    private static Set<String> outputAsSet(String output) {
        return new HashSet<>(Arrays.asList(output.split("\n")));
    }

    @Test
    public void test() {
        String result = GraphSpectrum.doWork("src/test/resources/IV/sample/GraphSpectrum.txt");
        String expected =
                "0->57:G\n" +
                "0->71:A\n" +
                "57->154:P\n" +
                "57->185:K\n" +
                "71->185:N\n" +
                "154->301:F\n" +
                "185->332:F\n" +
                "301->415:N\n" +
                "301->429:K\n" +
                "332->429:P\n" +
                "415->486:A\n" +
                "429->486:G";
        Assert.assertEquals(outputAsSet(result), outputAsSet(expected));
    }

    @Test
    public void testExtra() {
        String result = GraphSpectrum.doWork("src/test/resources/IV/sample/GraphSpectrumExtra.txt");
        String expected =
                "0->113:L\n" +
                "0->137:H\n" +
                "113->184:A\n" +
                "137->268:M\n" +
                "184->297:L\n" +
                "268->381:L\n" +
                "297->425:Q\n" +
                "381->496:D\n" +
                "425->496:A\n" +
                "425->581:R\n" +
                "496->624:Q\n" +
                "581->695:N\n" +
                "624->695:A\n" +
                "624->752:Q\n" +
                "695->752:G\n" +
                "695->832:H\n" +
                "752->889:H\n" +
                "832->889:G\n" +
                "832->988:R\n" +
                "889->988:V\n" +
                "889->1045:R\n" +
                "988->1045:G\n" +
                "988->1102:N\n" +
                "1045->1102:G\n" +
                "1045->1160:D\n" +
                "1102->1215:L\n" +
                "1102->1217:D\n" +
                "1160->1217:G\n" +
                "1215->1314:V\n" +
                "1215->1401:W\n" +
                "1217->1314:P\n" +
                "1217->1373:R\n" +
                "1314->1401:S\n" +
                "1373->1460:S\n" +
                "1373->1559:W\n" +
                "1401->1557:R\n" +
                "1460->1557:P\n" +
                "1460->1559:V\n" +
                "1557->1614:G\n" +
                "1557->1672:D\n" +
                "1559->1672:L\n" +
                "1614->1729:D\n" +
                "1672->1729:G\n" +
                "1672->1786:N\n" +
                "1729->1786:G\n" +
                "1729->1885:R\n" +
                "1786->1885:V\n" +
                "1786->1942:R\n" +
                "1885->1942:G\n" +
                "1885->2022:H\n" +
                "1942->2079:H\n" +
                "2022->2079:G\n" +
                "2022->2150:Q\n" +
                "2079->2150:A\n" +
                "2079->2193:N\n" +
                "2150->2278:Q\n" +
                "2193->2349:R\n" +
                "2278->2349:A\n" +
                "2278->2393:D\n" +
                "2349->2477:Q\n" +
                "2393->2506:L\n" +
                "2477->2590:L\n" +
                "2506->2637:M\n" +
                "2590->2661:A\n" +
                "2637->2774:H\n" +
                "2661->2774:L";
        Assert.assertEquals(outputAsSet(result), outputAsSet(expected.replace('Q', 'K').replace('L', 'I')));
    }
}