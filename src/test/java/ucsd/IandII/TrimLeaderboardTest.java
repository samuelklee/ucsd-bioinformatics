package ucsd.IandII;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TrimLeaderboardTest {
    @Test
    public void test() {
        String result = TrimLeaderboard.doWork("src/test/resources/IandII/sample/TrimLeaderboard.txt");
        String expected = "LAST ALST";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = TrimLeaderboard.doWork("src/test/resources/IandII/sample/TrimLeaderboardExtra.txt");
        String expected = "WASIGAIMRSAKDMYESLEFHKTHCTYFVYMVCKEARPGWTFFIEWV YYGYRQCSWCQRWTVRRMLCWIDVLHKALHWHVCLLFHQALYGFSHE WDTDTFFQKAMLKKDETADQIFNLRPYSLTCHNENILGNDNQEKQAG YDSPTTYLSTHCHRLTNRMVHENPVICPPQDFAKYLIQSGWEFPLVA MAPRDIRMYFDKYHETAALDSQWIIQQIYHLMNVRKLNRTNRFTSVG FEKYHQQQILIDAQRVRLVHTVARAGPGWVQTGGWQQTCPRYKPYAW";
        Assert.assertEquals(result, expected);
    }
}

