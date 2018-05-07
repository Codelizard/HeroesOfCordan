package net.codelizard.hoc.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    
    /** Sets of messages in the game, where one is chosen randomly each time the set is displayed. */
    @JsonProperty("dynamic_messages")
    private DynamicMessages dynamicMessages;
    
    /** Static messages that, unlike the DynamicMessages above, are always the same. */
    @JsonProperty("static_messages")
    private Map<String, String> staticMessages;
    
    /** Tier-organized game content. */
    private Map<Integer, Tier> tiers;
    
    /** The heroes in the game. */
    private List<Hero> heroes;
    
    public GameContent() {}
    
    /**
     * @return The dynamic messages object.
     */
    public DynamicMessages getDynamicMessages() {
        return dynamicMessages;
    }
    
    /**
     * @return The static messages map.
     */
    public Map<String, String> getStaticMessages() {
        return staticMessages;
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
     * @param dynamicMessages The new dyanmic messages object to use.
     */
    public void setDynamicMessages(final DynamicMessages dynamicMessages) {
        this.dynamicMessages = dynamicMessages;
    }
    
    /**
     * @param staticMessages The new map of static messages to use.
     */
    public void setStaticMessages(final Map<String, String> staticMessages) {
        this.staticMessages = staticMessages;
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
    
    /**
     * @param identifier The identifier of the static message to look up.
     * @return The static message with the given identifier.
     */
    public String getStaticMessage(String identifier) {
        return staticMessages.getOrDefault(identifier, "((Error: Missing message \"" + identifier + "\".))");
    }
    
}
