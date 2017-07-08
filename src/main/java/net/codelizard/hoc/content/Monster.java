package net.codelizard.hoc.content;

import net.codelizard.hoc.logic.PlayerState;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a monster that the party can fight.
 * 
 * @author Codelizard
 */
public class Monster extends Obstacle {
    
    /** The type of loot the monster drops. */
    @JsonProperty("loot")
    private LootType lootType;
    
    public Monster() {}

    /**
     * @return The type of loot this Monster drops.
     */
    public LootType getLootType() {
        return lootType;
    }

    /**
     * @param lootType The new loot drop type to use.
     */
    public void setLootType(final LootType lootType) {
        this.lootType = lootType;
    }

    @Override
    protected int getDiscount(PlayerState currentState, ResourceType resourceType) {
        return currentState.getParty().monsterDiscount(resourceType);
    }
    
    /**
     * @return A one-line description of this Monster for debugging purposes.
     */
    @Override
    public String toString() {
        return "Monster: " + name;
    }
    
}
