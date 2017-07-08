package net.codelizard.hoc.content;

/**
 * Represents a useful (or "useful") object the party can take with them to gain some sort of bonus later on.
 * 
 * @author Codelizard
 */
public abstract class Item extends ContentObject {
    
    /**
     * Generates a description of all the resources this Item grants, and the strength of their effect.
     * @return A one-line list that can be displayed to the user.
     */
    public String benefits() {
        
        StringBuilder output = new StringBuilder();
        
        for(ResourceType nextType : resources.keySet()) {

            final ResourceValue value = resources.get(nextType);
            if(value.getValue() != 0) {
                
                if(output.length() > 0) {
                    output.append(", ");
                }
                
                output.append("+")
                      .append(value.getValue())
                      .append(" ")
                      .append(nextType.name);

            }

        }
        
        return output.toString();
        
    }
    
}
