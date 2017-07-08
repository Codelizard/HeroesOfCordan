package net.codelizard.hoc.content;

import net.codelizard.hoc.logic.PlayerState;

/**
 * Represents an in-game event designed to wear the party down.
 * 
 * @author Codelizard
 */
public class Event extends Obstacle {
    
    public Event() {}

    @Override
    protected int getDiscount(PlayerState currentState, ResourceType resourceType) {
        return currentState.getParty().eventDiscount(resourceType);
    }
        
    /**
     * @return A one-line description of this Event for debugging purposes.
     */
    @Override
    public String toString() {
        return "Event: " + name;
    }
    
}
