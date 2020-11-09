import exceptions.MissingGroupInfoException;
import exceptions.EmptyTextFieldException;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrencyBankBot extends TelegramLongPollingBot {

    private static final Logger logger = Logger.getLogger(CurrencyBankBot.class.getName());

    private static String BOT_NAME = "CurrencyBankBot";
    private static String BOT_TOKEN = "1489030428:AAE9LpzaQWoGPo2OVCxzABBe-7AZabq0R6k";

    private static String PROXY_HOST = "54.38.195.161" /* proxy host */;
    private static Integer PROXY_PORT = 35997 /* proxy port */;

    protected CurrencyBankBot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    public static void main(String[] args) {
        try {
            TextMessageParser.load();
        } catch (IOException e) {
            logger.log(Level.WARNING,"Some files are missing", e);
        }

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            DefaultBotOptions botOptions = new DefaultBotOptions();

//            botOptions.setProxyHost(PROXY_HOST);
//            botOptions.setProxyPort(PROXY_PORT);
//            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

            botsApi.registerBot(new CurrencyBankBot(botOptions));
        } catch (TelegramApiException e) {
            logger.log(Level.WARNING, "Error registering Bot", e);
        }
    }

    public synchronized void sendMsg(Message msg, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(msg.getChatId().toString());
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.log(Level.WARNING, "Cannot send message");
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null) {
            try {
                sendMsg(message, TextMessageParser.getMessage(message));
            } catch (IOException | ParseException e) {
                logger.log(Level.WARNING, "Cannot parse your input");
            } catch (EmptyTextFieldException e) {
                sendMsg(message, "Напишите другое сообщение");
            } catch (MissingGroupInfoException ignored) {}
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
