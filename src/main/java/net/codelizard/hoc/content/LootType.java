package net.codelizard.hoc.content;

/**
 * Represents the different types of loot that monsters drop.
 * 
 * @author Codelizard
 */
public enum LootType {
    
    /** A piece of equipment. */
    EQUIPMENT(),
    /** A one-time-use item. */
    CONSUMABLE(),
    /** A party level-up. */
    LEVELUP(),
    /** Win the game by killing this monster. */
    WIN();
    
}
