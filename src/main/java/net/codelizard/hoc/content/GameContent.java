package net.codelizard.hoc.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents all the events, monsters etc in the game in deserialized form. This class is designed to be read in from
 * a JSON file.
 * 
 * @author Codelizard
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GameContent {
    
    /** Sets of messages in the game. */
    private Messages messages;
    
    /** Tier-organized game content. */
    private Map<Integer, Tier> tiers;
    
    /** The heroes in the game. */
    private List<Hero> heroes;
    
    public GameContent() {}
    
    /**
     * @return The game messages object.
     */
    public Messages getMessages() {
        return messages;
    }

    /**
     * @return All tiers of content in the game.
     */
    public Map<Integer, Tier> getTiers() {
        return tiers;
    }
    
    /**
     * @return All of the heroes in the game.
     */
    public List<Hero> getHeroes() {
        return heroes;
    }
    
    /**
     * @param messages The new game messages object to use.
     */
    public void setMessages(final Messages messages) {
        this.messages = messages;
    }

    /**
     * @param tiers The new content tiers to use.
     */
    public void setTiers(final Map<Integer, Tier> tiers) {
        this.tiers = tiers;
    }
    
    private void setTiers(final List<? extends ContentObject> objects, final Integer tierNumber) {
        for(ContentObject nextObject : objects) {
            nextObject.setTier(tierNumber);
        }
    }
    
    /**
     * Goes through all the objects in the game and sets the tier value on them.
     */
    public void inferObjectTiers() {
        for(Integer nextTierNumber : tiers.keySet()) {
            final Tier nextTier = tiers.get(nextTierNumber);
            setTiers(nextTier.getConsumables(), nextTierNumber);
            setTiers(nextTier.getEquipment(), nextTierNumber);
            setTiers(nextTier.getEvents(), nextTierNumber);
            setTiers(nextTier.getMonsters(), nextTierNumber);
        }
    }
    
    /**
     * @param heroes The new hero list to use.
     */
    public void setHeroes(final List<Hero> heroes) {
        this.heroes = heroes;
    }
    
    /**
     * @return A list of four randomly-chosen heroes (for Quick Play).
     */
    public List<Hero> fourRandomHeroes() {
        
        List<Hero> copy = new ArrayList<>(heroes);
        Collections.shuffle(copy);
        return copy.subList(0, 4);
        
    }
    
}
