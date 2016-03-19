package ucsd.V;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SquaredErrorDistortionTest {
    @Test
    public void test() {
        String result = SquaredErrorDistortion.doWork("src/test/resources/V/sample/SquaredErrorDistortion.txt");
        String expected = "18.246";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testExtra() {
        String result = SquaredErrorDistortion.doWork("src/test/resources/V/sample/SquaredErrorDistortionExtra.txt");
        String expected = "36.763";
        Assert.assertEquals(result, expected);
    }
}

