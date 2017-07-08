package net.codelizard.hoc.content;

import java.util.List;

/**
 * Represents a single "tier"'s worth of content, which equates to a dungeon floor.
 * 
 * @author Codelizard
 */
public class Tier {
    
    /** Events that occur on this tier. */
    private List<Event> events;
    
    /** Monsters that appear on this tier. */
    private List<Monster> monsters;
    
    /** Equipment that can be found on this tier. */
    private List<Equipment> equipment;
    
    /** Consumables that can be found on this tier. */
    private List<Consumable> consumables;
    
    /** The boss found on this tier. */
    private Monster boss;
    
    public Tier() {}

    /**
     * @return All Events that can happen on this tier.
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * @return All Monsters that can appear on this tier.
     */
    public List<Monster> getMonsters() {
        return monsters;
    }

    /**
     * @return All Equipment that can be found on this tier.
     */
    public List<Equipment> getEquipment() {
        return equipment;
    }

    /**
     * @return All Consumables that can be found on this tier.
     */
    public List<Consumable> getConsumables() {
        return consumables;
    }
    
    /**
     * @return The boss of this tier.
     */
    public Monster getBoss() {
        return boss;
    }

    /**
     * @param events The new event list to use.
     */
    public void setEvents(final List<Event> events) {
        this.events = events;
    }

    /**
     * @param monsters The new monster list to use.
     */
    public void setMonsters(final List<Monster> monsters) {
        this.monsters = monsters;
    }

    /**
     * @param equipment The new equipment list to use.
     */
    public void setEquipment(final List<Equipment> equipment) {
        this.equipment = equipment;
    }

    /**
     * @param consumables The new consumables list to use.
     */
    public void setConsumables(final List<Consumable> consumables) {
        this.consumables = consumables;
    }
    
}
