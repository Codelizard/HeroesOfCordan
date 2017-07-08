package net.codelizard.hoc.content;

import net.codelizard.hoc.logic.PlayerState;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents events, monsters etc that get in the party's way.
 * 
 * @author Codelizard
 */
public abstract class Obstacle extends ContentObject {
    
    protected abstract int getDiscount(PlayerState currentState, ResourceType resourceType);

    /**
     * Checks whether or not this Obstacle can be beaten by the party based on the resources they have and the resource
     * they are attempting to use.
     * @param resourceType The resource the party is trying to defeat the obstacle with.
     * @param currentState The party's current state.
     * @return {@code true} if the party can defeat this obstacle with the specified resource, {@code false} if not.
     */
    public boolean canDefeat(final ResourceType resourceType, final PlayerState currentState) {
        
        final ResourceValue value = resources.get(resourceType);
        if(value == null || value.getValue() == 0) { //Must have a valid nonzero cost entry
            
            return false;
            
        } else {

            int discount = getDiscount(currentState, resourceType);

            //Discounts can't bring a resource cost below 1
            int cost = Math.max(1, value.getValue() - discount);
            
            return resourceType.canAlwaysSpend
                    || cost <= currentState.getResourceCount(resourceType);
            
        }
        
    }
    
    /**
     * Generates a full listing of all spendable resources that can be used to defeat this Obstacle. Discounts are
     * marked with a * and only costs with entries are listed.
     * @param currentState The player state to use to generate the listing.
     * @return A line-by-line cost listing that can be displayed to the user.
     */
    public String costListing(final PlayerState currentState) {
        
        final StringBuilder output = new StringBuilder();
        
        //Iterate over the ResourceType's values to preserve order of declaration
        for(ResourceType nextType : ResourceType.values()) {

            final ResourceValue value = resources.get(nextType);
            if(value != null && value.getValue() != 0) {

                int discount = getDiscount(currentState, nextType);

                //Discounts can't bring a resource cost below 1
                int cost = Math.max(1, value.getValue() - discount);

                output.append("[")
                      .append(cost);

                if(discount > 0) {
                    output.append("*");
                }

                output.append(" ")
                      .append(nextType.name)
                      .append("]")
                      .append(" ")
                      .append(value.randomText())
                      .append("\n");

            }

        }
        
        return output.toString();
        
    }
    
    /**
     * Returns a list of all the ResourceTypes that the party has enough of to be able to defeat this Obstcale with,
     * assuming it can be defeated with that ResourceType at all.
     * @param currentState The party's current state.
     * @return A list of all the valid ResourceTypes the party can use to defeat this obstacle.
     */
    public List<ResourceType> usableResources(final PlayerState currentState) {
        
        final List<ResourceType> usableTypes = new ArrayList<>();
        
        for(ResourceType nextType : ResourceType.values()) {

            //Always allow spending health/time, even if the party doesn't have enough
            if(canDefeat(nextType, currentState)) {
                usableTypes.add(nextType);
            }

        }
        
        return usableTypes;
        
    }
    
}
