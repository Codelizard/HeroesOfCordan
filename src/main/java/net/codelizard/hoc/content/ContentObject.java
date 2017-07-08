package net.codelizard.hoc.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

/**
 * A parent class of other content objects that contains fields common to all of them.
 * 
 * @author Codelizard
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class ContentObject {
    
    /** The internal ID of the object. */
    protected String id;
    
    /** The name of the object. */
    protected String name;

    /** The tier of the dungeon the object belongs to. (Inferred after loading; not stored in the content file) */
    @JsonIgnore
    protected int tier;
    
    /** The resource resources or bonuses related to the object. */
    protected Map<ResourceType, ResourceValue> resources;

    /** The description(s) about the object. */
    protected List<String> descriptions;
    
    /** Flavor text quote(s) about the object from the various characters. */
    protected List<String> flavor;

    /**
     * @return The internally-used ID.
     */
    public String getId() {
        return id;
    }

    /**
     * @return The user-visible name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return The object's tier.
     */
    public int getTier() {
        return tier;
    }

    /**
     * @return The resources related to this object.
     */
    public Map<ResourceType, ResourceValue> getResources() {
        return resources;
    }

    /**
     * @return All long descriptions of this object.
     */
    public List<String> getDescriptions() {
        return descriptions;
    }

    /**
     * @return All flavor text quotes about this object.
     */
    public List<String> getFlavor() {
        return flavor;
    }

    /**
     * @param id The new ID to use.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @param name The new name to use.
     */
    public void setName(final String name) {
        this.name = name;
    }
    
    /**
     * @param tier The new tier value.
     */
    public void setTier(final int tier) {
        this.tier = tier;
    }

    /**
     * @param resources The new resources map to use.
     */
    public void setResources(final Map<ResourceType, ResourceValue> resources) {
        this.resources = resources;
    }

    /**
     * @param descriptions A new list of descriptions to use.
     */
    public void setDescriptions(final List<String> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * @param flavor The new flavor text quotes to use
     */
    public void setFlavor(final List<String> flavor) {
        this.flavor = flavor;
    }
    
    /**
     * @return A randomly-selected description from the list of descriptions of this ContentObject.
     */
    public String randomDescription() {
        return descriptions.get( (int) (Math.random() * descriptions.size()) );
    }
    
    /**
     * @return A randomly-selected flavor text from the list of flavor texts for this ContentObject.
     */
    public String randomFlavor() {
        return flavor.get( (int) (Math.random() * flavor.size()) );
    }
    
    /**
     * @return A full, complete description of this ContentObject.
     */
    public String fullLengthDescription() {
        return "--" + name + "--\n\n" + randomDescription() + "\n\n" + randomFlavor();
    }
    
}
