package net.codelizard.hoc;

import net.codelizard.hoc.logic.ActionProcessor;
import net.codelizard.hoc.logic.PlayerAction;
import net.codelizard.hoc.logic.PlayerActionBuilder;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.TelegramApiException;

import java.io.IOException;
import java.util.Properties;

/**
 * Telegram interface to HoC.
 *
 * @author Codelizard
 */
public class TelegramHocBot extends TelegramLongPollingBot {

    /** File in src/main/resources that contains the API token. */
    private static final String TOKEN_FILENAME = "/apitoken.properties";

    /** Full name of the bot. */
    private static final String BOT_USERNAME = "Heroes of Cordan";

    /** Telegram bot API token. */
    private final String apiToken;
    
    /** The action processor to handle user commands. */
    private final ActionProcessor actionProcessor;
    
    public TelegramHocBot(final ActionProcessor actionProcessor) throws IOException, TelegramApiException {
        this.actionProcessor = actionProcessor;
        this.apiToken = loadApiToken();
    }

    @Override
    public String getBotToken() {
        return apiToken;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage()) {

            final Message message = update.getMessage();
            final User sender = message.getFrom();

            if(message.hasText()) {

                final PlayerAction action = new PlayerActionBuilder()
                    .setInputText(message.getText())
                    .setServiceName("Telegram")
                    .setServiceUserId(sender.getId().toString())
                    .setUserFirstName(sender.getFirstName())
                    .build();
                
                GameResponse response = actionProcessor.handleAction(action);
                
                final SendMessage telegramResponse = new SendMessage();
                telegramResponse.setChatId(message.getChatId().toString());
                telegramResponse.setText(response.getText());
                telegramResponse.setReplyMarkup(response.telegramReplyKeyboardResponse());

                try {
                    sendMessage(telegramResponse);
                } catch (TelegramApiException tax) {
                    //TODO: Log it
                    tax.printStackTrace();
                }

            }

        }

    }
    
    /**
     * Retrieves the bot's Telegram API token.
     * @return The API token for Telegram.
     * @throws IOException If there is an error reading the token.
     */
    private static String loadApiToken() throws IOException {

        final Properties tokenProperties = new Properties();
        tokenProperties.load(TelegramHocBot.class.getResourceAsStream(TOKEN_FILENAME));

        return tokenProperties.getProperty("telegram_api_token");
        
    }

}
