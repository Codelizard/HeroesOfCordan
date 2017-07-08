package net.codelizard.hoc.content;

/**
 * Enumerates the different types of discounts heroes can offer.
 * 
 * @author Codelizard
 */
public enum DiscountType {
    
    EVENT("Events"),
    MONSTER("Monsters");
    
    public final String name;
    
    DiscountType(final String name) {
        this.name = name;
    }
    
}
