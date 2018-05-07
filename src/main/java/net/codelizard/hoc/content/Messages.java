package net.codelizard.hoc.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents collections of messages shown at various points in the game. This class is designed to be read in from a
 * JSON file.
 * 
 * @author Codelizard
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Messages {
    
    /** Messages shown when entering the dungeon. */
    @JsonProperty("enter_dungeon_messages")
    private List<String> enterDungeonMessages;
    
    /** Messages shown when entering the dungeon after the main enter dungeon message. */
    @JsonProperty("enter_dungeon_closing_messages")
    private List<String> enterDungeonClosingMessages;
    
    /** Messages shown when restarting the game. */
    @JsonProperty("restart_messages")
    private List<String> restartMessages;
    
    /** Messages shown when taking a short rest. */
    @JsonProperty("short_rest_messages")
    private List<String> shortRestMessages;
    
    /** Messages shown when taking a long rest. */
    @JsonProperty("long_rest_messages")
    private List<String> longRestMessages;
    
    /** Messages shown when doing a Cure. */
    @JsonProperty("cure_messages")
    private List<String> cureMessages;
    
    /** Messages shown when doing a Mass Cure. */
    @JsonProperty("mass_cure_messages")
    private List<String> massCureMessages;
    
    /** Messages shown when the player runs out of time. */
    @JsonProperty("out_of_time_messages")
    private List<String> outOfTimeMessages;
    
    /** Messages shown when the player runs out of health. */
    @JsonProperty("out_of_health_messages")
    private List<String> outOfHealthMessages;
    
    /**
     * Constructs a new, empty Messages object.
     */
    public Messages() {}
    
    private String randomMessage(List<String> list, String defaultMessage) {
        if(list.size() > 0) {
            return list.get((int) Math.floor(Math.random() * list.size()));
        } else {
            return defaultMessage;
        }
    }
    
    /**
     * @return A random message from the list of restart messages.
     */
    public String randomRestartMessage() {
        return randomMessage(restartMessages, "Restarting...");
    }
    
    /**
     * @return A random message from the list of enter dungeon messages.
     */
    public String randomEnterDungeonMessage() {
        return randomMessage(enterDungeonMessages, "Your party assembled, you descend into the dungeon...");
    }
    
    /**
     * @return A random message from the list of enter dungeon closing messages.
     */
    public String randomEnterDungeonClosingMessage() {
        return randomMessage(enterDungeonClosingMessages, "Will they become the Heroes of Cordan?");
    }
    
    /**
     * @return A random message from the list of short rest messages.
     */
    public String randomShortRestMessage() {
        return randomMessage(shortRestMessages, "You take a short break to recover your strength.");
    }
    
    /**
     * @return A random message from the list of long rest messages.
     */
    public String randomLongRestMessage() {
        return randomMessage(longRestMessages, "You camp out for a while to recover your strength and tend to your injuries.");
    }
    
    /**
     * @return A random message from the list of cure messages.
     */
    public String randomCureMessage() {
        return randomMessage(cureMessages, "You channel divine magic to heal a wound.");
    }
    
    /**
     * @return A random message from the list of mass cure messages.
     */
    public String randomMassCureMessage() {
        return randomMessage(massCureMessages, "You channel divine magic to heal your party's wounds.");
    }
    
    /**
     * @return A random message from the list of out-of-time messages.
     */
    public String randomOutOfTimeMessage() {
        return randomMessage(outOfTimeMessages, "You have run out of time; you cannot hope to complete your quest now.");
    }
    
    /**
     * @return A random message from the list of out-of-health messages.
     */
    public String randomOutOfHealthMessage() {
        return randomMessage(outOfHealthMessages, "Your party is forced to retreat and hide to heal their injuries.");
    }
    
}
