package ucsd.IV;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AdditivePhylogenyTest {
    private static Set<String> outputAsSet(String output) {
        return new HashSet<>(Arrays.asList(output.split("\n")));
    }

    @Test
    public void test() {
        String result = AdditivePhylogeny.doWork("src/test/resources/IV/sample/AdditivePhylogeny.txt");
        String expected =
                "0->4:11\n" +
                "1->4:2\n" +
                "2->5:6\n" +
                "3->5:7\n" +
                "4->0:11\n" +
                "4->1:2\n" +
                "4->5:4\n" +
                "5->4:4\n" +
                "5->3:7\n" +
                "5->2:6";
        Assert.assertEquals(outputAsSet(result), outputAsSet(expected));
    }

    @Test
    public void testExtra() {
        String result = AdditivePhylogeny.doWork("src/test/resources/IV/sample/AdditivePhylogenyExtra.txt");
        String expected =
                "0->55:745\n" +
                "1->48:156\n" +
                "2->52:788\n" +
                "3->54:409\n" +
                "4->53:280\n" +
                "5->49:125\n" +
                "6->51:492\n" +
                "7->50:657\n" +
                "8->31:311\n" +
                "9->41:820\n" +
                "10->47:280\n" +
                "11->46:723\n" +
                "12->45:417\n" +
                "13->44:864\n" +
                "14->43:236\n" +
                "15->42:89\n" +
                "16->33:713\n" +
                "17->40:445\n" +
                "18->39:87\n" +
                "19->38:441\n" +
                "20->37:783\n" +
                "21->36:348\n" +
                "22->35:922\n" +
                "23->34:662\n" +
                "24->33:375\n" +
                "25->32:718\n" +
                "26->29:868\n" +
                "27->30:841\n" +
                "28->29:890\n" +
                "29->28:890\n" +
                "29->31:965\n" +
                "29->26:868\n" +
                "30->27:841\n" +
                "30->34:687\n" +
                "30->47:112\n" +
                "31->8:311\n" +
                "31->40:416\n" +
                "31->29:965\n" +
                "32->41:170\n" +
                "32->25:718\n" +
                "32->35:952\n" +
                "33->24:375\n" +
                "33->16:713\n" +
                "33->36:381\n" +
                "34->52:276\n" +
                "34->23:662\n" +
                "34->30:687\n" +
                "35->32:952\n" +
                "35->22:922\n" +
                "35->36:683\n" +
                "36->33:381\n" +
                "36->21:348\n" +
                "36->35:683\n" +
                "37->20:783\n" +
                "37->53:110\n" +
                "37->50:866\n" +
                "38->19:441\n" +
                "38->44:522\n" +
                "38->49:481\n" +
                "39->18:87\n" +
                "39->51:809\n" +
                "39->55:355\n" +
                "40->17:445\n" +
                "40->31:416\n" +
                "40->49:230\n" +
                "41->32:170\n" +
                "41->42:982\n" +
                "41->9:820\n" +
                "42->41:982\n" +
                "42->15:89\n" +
                "42->48:247\n" +
                "43->53:656\n" +
                "43->54:698\n" +
                "43->14:236\n" +
                "44->38:522\n" +
                "44->13:864\n" +
                "44->47:464\n" +
                "45->12:417\n" +
                "45->54:64\n" +
                "45->55:323\n" +
                "46->11:723\n" +
                "46->51:884\n" +
                "46->48:87\n" +
                "47->10:280\n" +
                "47->30:112\n" +
                "47->44:464\n" +
                "48->42:247\n" +
                "48->46:87\n" +
                "48->1:156\n" +
                "49->5:125\n" +
                "49->38:481\n" +
                "49->40:230\n" +
                "50->52:527\n" +
                "50->37:866\n" +
                "50->7:657\n" +
                "51->46:884\n" +
                "51->6:492\n" +
                "51->39:809\n" +
                "52->2:788\n" +
                "52->50:527\n" +
                "52->34:276\n" +
                "53->43:656\n" +
                "53->37:110\n" +
                "53->4:280\n" +
                "54->3:409\n" +
                "54->45:64\n" +
                "54->43:698\n" +
                "55->0:745\n" +
                "55->39:355\n" +
                "55->45:323";
        //internal nodes labeled differently
        Assert.assertEquals(outputAsSet(result).size(), outputAsSet(expected).size());
    }
}

