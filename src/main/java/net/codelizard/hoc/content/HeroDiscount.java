package net.codelizard.hoc.content;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represents a discount that a hero applies to event or monster resource costs.
 * 
 * @author Codelizard
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown=true)
public class HeroDiscount {
    
    private DiscountType type;
    
    private ResourceType resource;
    
    public HeroDiscount() {}

    /**
     * @return The type of discount that is applied.
     */
    public DiscountType getType() {
        return type;
    }

    /**
     * @return The type of resource the discount applies to.
     */
    public ResourceType getResource() {
        return resource;
    }

    /**
     * @param type The new discount type to use.
     */
    public void setType(DiscountType type) {
        this.type = type;
    }

    /**
     * @param resource The new resource type to use.
     */
    public void setResource(ResourceType resource) {
        this.resource = resource;
    }
    
    /**
     * @return A one-line description of this discount for debugging purposes.
     */
    @Override
    public String toString() {
        return type.toString() + "/" + resource.toString();
    }
    
}
