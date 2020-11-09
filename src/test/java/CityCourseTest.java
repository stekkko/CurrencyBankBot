import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.regex.Pattern;

public class CityCourseTest {

    @Test
    public void CityTest() throws IOException {
        String expected = "--- EUR ---\n" +
                "[а-яА-Яё\\sЁ-]{0,100}: \n" + "- покупка: [0-9]{1,3}.[0-9]{0,2}\n" + "- продажа: [0-9]{1,3}.[0-9]{0,2}\n" + "\n" +
                "[а-яА-Яё\\sЁ-]{0,100}: \n" + "- покупка: [0-9]{1,3}.[0-9]{0,2}\n" + "- продажа: [0-9]{1,3}.[0-9]{0,2}\n" + "\n" +
                "[а-яА-Яё\\sЁ-]{0,100}: \n" + "- покупка: [0-9]{1,3}.[0-9]{0,2}\n" + "- продажа: [0-9]{1,3}.[0-9]{0,2}\n" + "\n" +
                "[а-яА-Яё\\sЁ-]{0,100}: \n" + "- покупка: [0-9]{1,3}.[0-9]{0,2}\n" + "- продажа: [0-9]{1,3}.[0-9]{0,2}\n" + "\n" +
                "[а-яА-Яё\\sЁ-]{0,100}: \n" + "- покупка: [0-9]{1,3}.[0-9]{0,2}\n" + "- продажа: [0-9]{1,3}.[0-9]{0,2}" + "\n\n\n";
        String actual = CityCourse.getCityCourse("moskva", "EUR");
        Assert.assertTrue(Pattern.matches(expected, actual));
    }
}