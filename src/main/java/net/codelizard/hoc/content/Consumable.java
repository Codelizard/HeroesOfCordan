package net.codelizard.hoc.content;

/**
 * A one-time-use item that boosts the party's current values of a resource.
 * 
 * @author Codelizard
 */
public class Consumable extends Item {
    
    public Consumable() {}
    
    /**
     * @return A one-line description of this Consumable for debugging purposes.
     */
    @Override
    public String toString() {
        return "Consumable: " + name;
    }
    
}
