package net.codelizard.hoc.content;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a numerical value for spending/gaining resources, potentially with optional text descriptions attached.
 * 
 * @author Codelizard
 */
public class ResourceValue {
    
    /** The numerical value of the resource expenditure/gain. */
    private Integer value = 0;
    
    /** Text, if any, associated with the resource expenditure/gain. */
    private List<String> texts = new ArrayList<>();
    
    public ResourceValue() {}

    /**
     * @return The integer value of this ResourceValue.
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value The new value to use.
     */
    public void setValue(final Integer value) {
        this.value = value;
    }

    /**
     * @return All texts, if any, associated with this resource value.
     */
    public List<String> getTexts() {
        return texts;
    }

    /**
     * @param texts The new list of texts to use.
     */
    public void setTexts(final List<String> texts) {
        if(texts == null) {
            this.texts = new ArrayList<>();
        } else {
            this.texts = texts;
        }
    }
    
    /**
     * @return A random text from the texts for this value, or an empty string if there are no texts.
     */
    public String randomText() {
        return texts.size() > 0 ? texts.get((int) (Math.random() * texts.size())) : "";
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
}
