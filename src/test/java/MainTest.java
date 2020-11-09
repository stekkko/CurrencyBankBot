import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void courseTest() throws IOException {
        TextMessageParser.load();
        boolean actual = TextMessageParser.isCourse("курсы");
        Assert.assertTrue(actual);
    }

    @Test
    public void valuteTest() throws IOException {
        TextMessageParser.load();
        boolean actual = TextMessageParser.isCourse("валюта");
        Assert.assertTrue(actual);
    }

    @Test
    public void UsdTest() throws IOException {
        TextMessageParser.load();
        String expected = "USD";
        String actual = TextMessageParser.getValute("бакс");
        assertEquals(expected, actual);
    }

    @Test
    public void EurTest() throws IOException {
        TextMessageParser.load();
        String expected = "EUR";
        String actual = TextMessageParser.getValute("евро");
        assertEquals(expected, actual);
    }

    @Test
    public void CityTest1() throws IOException {
        TextMessageParser.load();
        String expected = "ekaterinburg";
        String actual = TextMessageParser.getCity("екб");
        assertEquals(expected, actual);
    }

    @Test
    public void CityTest2() throws IOException {
        TextMessageParser.load();
        String expected = "ulyanovsk";
        String actual = TextMessageParser.getCity("ульяновск");
        assertEquals(expected, actual);
    }

}