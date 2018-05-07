package net.codelizard.hoc;

import net.codelizard.hoc.content.DynamicMessages;
import net.codelizard.hoc.content.GameContent;
import net.codelizard.hoc.logic.ActionProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.TelegramApiException;

import java.io.IOException;

/**
 * Entry point of the program, which launches all supported platform bots.
 * 
 * @author Codelizard
 */
public class HeroesOfCordan {
    
    /** File in src/main/resources that contains the game data. */
    private static final String CONTENT_FILENAME = "/heroes_of_cordan.json";
    
    /** The action processor to handle user commands. */
    private static ActionProcessor actionProcessor;
    
    /** The game content to read events, monsters etc from. */
    private static GameContent gameContent;
    
    /**
     * @param args the command line arguments
     * @todo Use a logging service instead of throwing raw exceptions and printing to stdout.
     * @throws Exception If there is an error during startup.
     */
    public static void main(String... args) throws Exception {
        
        gameContent = loadGameContent();
        
        actionProcessor = new ActionProcessor();
        
        startTelegramBot();
        
        System.out.println("HoC is ready.");
        
    }
    
    /**
     * Loads the game's content from disk and returns it.
     * @return The game content in native form.
     * @throws IOException If there is an error reading the game content.
     */
    private static GameContent loadGameContent() throws IOException {
        
        final ObjectMapper jsonMapper = new ObjectMapper();
        final GameContent content = jsonMapper.readValue(
            TelegramHocBot.class.getResourceAsStream(CONTENT_FILENAME), GameContent.class
        );
        content.inferObjectTiers();
        return content;
        
    }
    
    /**
     * Initializes and starts the Telegram interface to HoC.
     */
    private static void startTelegramBot() throws IOException, TelegramApiException {
        
        System.out.print("Starting Telegram bot... ");
        
        final TelegramBotsApi botApi = new TelegramBotsApi();
        botApi.registerBot(new TelegramHocBot(actionProcessor));
        
        System.out.println("Started.");
        
    }
    
    /**
     * @return The game's content in native object form.
     */
    public static GameContent getContent() {
        return gameContent;
    }
    
    /**
     * @return The loaded list of dynamic messages.
     */
    public static DynamicMessages getDynamicMessages() {
        return gameContent.getDynamicMessages();
    }
    
    /**
     * @param identifier The identifier of a static message to look up.
     * @return The loaded static message.
     */
    public static String getStaticMessage(String identifier) {
        return gameContent.getStaticMessage(identifier);
    }
    
}
