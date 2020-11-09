import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import exceptions.MissingGroupInfoException;
import exceptions.EmptyTextFieldException;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class TextMessageParser {
    private static HashMap<String, String> city;
    private static HashMap<String, String> valute;
    private static LinkedList<String> coursePattern;
    private static String region = "moskva";
    private static final Logger logger = Logger.getGlobal();


    public static void load() throws IOException {
        coursePattern = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader(new File("./src/main/resources/pattern/coursePattern.txt")));

        String line = reader.readLine();
        while (line != null){
            coursePattern.add(line);
            line = reader.readLine();
        }
        reader.close();

        Gson gson = new Gson();
        city = gson.fromJson(new JsonReader(new FileReader
                        ("./src/main/resources/pattern/City.json")),
                new TypeToken<HashMap<String, String>>(){}.getType());
        valute = gson.fromJson(new JsonReader(new FileReader
                        ("./src/main/resources/pattern/valutePattern.json")),
                new TypeToken<HashMap<String, String>>(){}.getType());
    }

    public static String getMessage(Message message) throws IOException, ParseException, MissingGroupInfoException, EmptyTextFieldException {
        if (message.hasText()) {
            String[] words = message.getText().split("\\s+");
            boolean isCourse = false;
            String valute = "";
            String city = "";

            boolean isLogged = false;

            for (int i = 0; i < words.length; i++) {
                String word = words[i];

                if (word.matches("\\d{2}(-|\\/)\\d{2}(-|\\/)\\d{4}")) {
                    return CentroBank.getCourse(word);
                }

                if(city.isEmpty()) city = getCity(word);
                if(valute.isEmpty()) valute = getValute(word);
                if(!isCourse) isCourse = isCourse(word);

                if(isCourse || !valute.isEmpty()){
                    if(!isLogged){
                        logger.log(Level.INFO,"Request: "  + message.getText());
                        isLogged = true;
                    }
                    if(!city.isEmpty())
                        region = city;
                    if(!valute.isEmpty() && !city.isEmpty())
                        return CityCourse.getCityCourse(region, valute);
                    if(i == words.length - 1){
                        if(city.isEmpty() && !valute.isEmpty())
                            return CityCourse.getCityCourse(region, valute);
                        return CityCourse.getByCity(region);
                    }
                }

                switch (word) {
                    case "/start":
                        return "Привет, чем могу помочь?\n" + "Для продолжения введите /help";
                    case "/help":
                        return "Список доступных комманд:\n" +
                                "/info - Получение информации о боте.\n" +
                                "/valute - Получение списка валют.\n" +
                                "/cbank - Получение справки о курсе валют в ЦБ.";
                    case "/info":
                        return "Расскажу немного о себе.\nЯ был создан для того, чтобы было проще узнавать курсы валют в" +
                                "различных регионах России. Я вывожу топ 5 самых популярных банков, в которых можно обменять" +
                                " запрашивашиваемую валюту, а также курс на определенную дату в Центробанке.";
                    case "/cbank":
                        return "Если вы хотите узнать курс валют ЦентроБанка, введите дату в указанном формате.\nНапример: 21/06/2020";
                    case "/valute":
                        return "Вы можете узнать курс валют, таких как:\n" +
                                "1. Доллар (USD)\n2. Евро (EUR)\n3. Фунт стерлингов (GBP)\n4. Юань (CNY)\n5. Йена (JPY)\n";
                    default:
                }
            }
        }
        else if(!message.isGroupMessage())
            throw new EmptyTextFieldException();
        if(message.isGroupMessage())
            throw new MissingGroupInfoException();
        return "Не хватает информации для вывода или сообщение введено не коррентно";
    }
    public static String getCity(String word){
        for (Map.Entry<String, String> item : city.entrySet()){
            if (Pattern.matches(item.getKey(), word.toLowerCase())) return item.getValue();
        }
        return "";
    }
    public static String getValute(String word){
        for (Map.Entry<String, String> item : valute.entrySet()){
            if (Pattern.matches(item.getKey(), word.toLowerCase())) return item.getValue();
        }
        return "";
    }

    public static boolean isCourse(String word){
        for (String pattern: coursePattern){
            if(Pattern.matches(pattern, word.toLowerCase()))
                return true;
        }
        return false;
    }
}
