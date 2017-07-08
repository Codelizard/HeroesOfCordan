package net.codelizard.hoc.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Represents one of the titular Heroes of Cordan.
 * 
 * @author Codelizard
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Hero {
    
    /** An internal-use ID. */
    private String id;
    
    /** The hero's default name if the player doesn't supply one. */
    private String name;
    
    /** The hero's RPG class (fighter, rogue, etc). */
    @JsonProperty("class")
    private String rpgClass;
    
    /** A short description of the hero, such as "Physical Specialist" or "Arcane/Divine Hybrid". */
    private String description;
    
    /** A flavor text description about the (default) hero. */
    private String flavor;
    
    /** A quote from the hero. */
    private String quote;
    
    /** The discount the hero applies. */
    private HeroDiscount discount;
    
    /** The resources the character contributes at each level. */
    private Map<Integer, Map<ResourceType, Integer>> resources;

    /**
     * @return An internal-use ID.
     */
    public String getId() {
        return id;
    }

    /**
     * @return The hero's name, which can be overridden by the player.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The hero's RPG class (fighter, rogue, etc).
     */
    public String getRpgClass() {
        return rpgClass;
    }
    
    /**
     * @return The hero's one-line description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return A description of the character.
     */
    public String getFlavor() {
        return flavor;
    }

    /**
     * @return A quote from the default character.
     */
    public String getQuote() {
        return quote;
    }

    /**
     * @return The discount the hero applies.
     */
    public HeroDiscount getDiscount() {
        return discount;
    }

    /**
     * @return The resources the hero contributes to the party total each level.
     */
    public Map<Integer, Map<ResourceType, Integer>> getResources() {
        return resources;
    }

    /**
     * @param id The new ID to use.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @param name The new name to use.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @param rpgClass The new RPG class to use.
     */
    public void setRpgClass(final String rpgClass) {
        this.rpgClass = rpgClass;
    }
    
    /**
     * @param description The new description to use.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @param flavor The new flavor text description to use.
     */
    public void setFlavor(final String flavor) {
        this.flavor = flavor;
    }

    /**
     * @param quote The new character quote to use.
     */
    public void setQuote(final String quote) {
        this.quote = quote;
    }

    /**
     * @param discount The new character discount to use.
     */
    public void setDiscount(final HeroDiscount discount) {
        this.discount = discount;
    }

    /**
     * @param resources The new resource map to use.
     */
    public void setResources(final Map<Integer, Map<ResourceType, Integer>> resources) {
        this.resources = resources;
    }
    
    public String getInitialResourcesText() {
        
        final StringBuilder output = new StringBuilder();
        final Map<ResourceType, Integer> levelOneResources = resources.get(1);
        
        for(ResourceType nextResource : levelOneResources.keySet()) {
            if(output.length() > 0) {
                output.append(" | ");
            }
            output.append(nextResource.name)
                  .append(": ")
                  .append(levelOneResources.get(nextResource));
        }
        
        return output.toString();
        
    }
    
    /**
     * @return A one-line description of this Hero for debugging purposes.
     */
    @Override
    public String toString() {
        return name + " (" + rpgClass + ")";
    }
    
}
