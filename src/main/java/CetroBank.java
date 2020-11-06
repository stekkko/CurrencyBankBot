import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CetroBank {
    private static final String url = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";

    public static String getCourse(String date) throws IOException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd/MM/yyyy");
        Date docDate= format.parse(date);
        Date dateStart = format.parse("01/07/1992");

        if(docDate.compareTo(dateStart) < 0)
            return "Информации по указанной дате нет.\n" + "Введите дату после 01/07/1992";

        Document doc = Jsoup.connect(url + date).get();
        Elements valute = doc.select("Valute");

        StringBuilder result = new StringBuilder();
        for (Element el : valute){
            String currency = el.select("CharCode").text();
            if(Currencies.hasValute(currency)){
                double nominal = Double.valueOf(el.select("Nominal").text());
                double price = Double.valueOf(el.select("Value").text().replace(',','.'));
                result.append(currency).append(" за ").append(nominal).append(" : ").append(price).append("\n");
            }
        }
        return "Курс валют в Центробанке РФ\n" + result;
    }
}
