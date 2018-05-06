package net.codelizard.hoc;

import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a generic, platform-agnostic response from the game intended to be sent back to the player, with some
 * helper methods for conversion to different services' formats. Once created, GameResponses are immutable.
 * 
 * @author Codelizard
 */
public final class GameResponse {
    
    /** The text of the message being sent back to the user. */
    private final String text;
    
    /** A list of responses for the user to pick from. If empty, the user is allowed to free-type anything they want. */
    private final List<String> responses;
    
    /** The number of columns to segment responses into when sending keyboardButtons to the user. */
    private int columns = 1;
    
    /**
     * Creates a new GameResponse that does not expect the user to reply with anything in particular.
     * @param text The text to show the user.
     */
    public GameResponse(final String text) {
        this(text, (List<String>) null);
    }
    
    /**
     * Creates a new GameResponse that expects the user to reply with one of the given responses.
     * @param text The text to show the user.
     * @param responses The responses the user should pick from. If {@code null}, an empty list is used.
     */
    public GameResponse(final String text, final List<String> responses) {
        this.text = text;
        this.responses = responses == null ? new ArrayList<>() : responses;
    }
    
    public GameResponse(final String text, final String... responses) {
        this.text = text;
        this.responses = new ArrayList<>();
        this.responses.addAll(Arrays.asList(responses));
    }

    /**
     * @return The text that should be shown to the user.
     */
    public String getText() {
        return text;
    }

    /**
     * @return The responses the user should pick from. If empty, the user is allowed to freely enter any text.
     */
    public List<String> getResponses() {
        return responses;
    }
    
    /**
     * Sets the number of columns of buttons when using {@link #telegramReplyKeyboardResponse()}.
     * @param columns The new number of columns to use when listing buttons.
     */
    public void setColumns(final int columns) {
        this.columns = Math.max(1, columns);
    }
    
    /**
     * Converts the responses provided into a ReplyKeyboard to be sent back to the user. (When no responses were given,
     * returns {@code null} so that their default keyboard will be used) Only usable with the Telegram API.
     * @return A ReplyKeyboard appropriate to the responses outlined at object creation.
     */
    public ReplyKeyboard telegramReplyKeyboardResponse() {
        
        if(responses.isEmpty()) {
            
            return null;
            
        } else {
            
            final List<KeyboardButton> keyboardButtons = new ArrayList<>();
            final List<KeyboardRow> keyboardRows = new ArrayList<>();
            
            for(String nextResponse : responses) {
                keyboardButtons.add(new KeyboardButton().setText(nextResponse));
            }
            
            while(!keyboardButtons.isEmpty()) {
                final KeyboardRow nextRow = new KeyboardRow();
                for(int column = 0; column < columns && !keyboardButtons.isEmpty(); column++) {
                    nextRow.add(keyboardButtons.remove(0));
                }
                keyboardRows.add(nextRow);
            }
            
            final ReplyKeyboardMarkup customKeyboard = new ReplyKeyboardMarkup();
            customKeyboard.setOneTimeKeyboad(true); //All HoC keyboards are only useful for one message.
            customKeyboard.setResizeKeyboard(true);
            customKeyboard.setKeyboard(keyboardRows);
            
            return customKeyboard;
            
        }
        
    }
    
}
