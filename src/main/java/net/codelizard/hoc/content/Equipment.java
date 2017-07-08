package net.codelizard.hoc.content;

/**
 * A piece of equipment the party can carry to give a permanent boost to maximum resources.
 * 
 * @author Codelizard
 */
public class Equipment extends Item {
    
    public Equipment() {}
    
    /**
     * @return A one-line description of this Equipment for debugging purposes.
     */
    @Override
    public String toString() {
        return "Equipment: " + name;
    }
    
}
