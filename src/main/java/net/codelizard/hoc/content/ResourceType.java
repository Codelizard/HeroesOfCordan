package net.codelizard.hoc.content;

import java.util.Arrays;

/**
 * Represents the different types of resources at the party's disposal.
 * 
 * @author Codelizard
 */
public enum ResourceType {
    
    PHYSICAL("Physical"),
    ARCANE("Arcane"),
    DIVINE("Divine"),
    STEALTH("Stealth"),
    MECHANICAL("Mechanical"),
    HEALTH("Health", true),
    TIME("Time", true);
    
    /** Resources restored during a short rest. */
    public static final Iterable<ResourceType> SHORT_REST_RESOURCES = Arrays.asList(
        PHYSICAL, ARCANE, DIVINE, STEALTH, MECHANICAL
    );
    
    /** Resources restored during an extended rest. */
    public static final Iterable<ResourceType> EXTENDED_REST_RESOURCES = Arrays.asList(
        PHYSICAL, ARCANE, DIVINE, STEALTH, MECHANICAL, HEALTH
    );
    
    /** The user-visible name of this ResourceType. */
    public final String name;
    
    /** Whether or not the party can spend this resource even if doing so will put them into negatives. */
    public final boolean canAlwaysSpend;
    
    ResourceType(final String name) {
        this(name, false);
    }
    
    ResourceType(final String name, boolean canAlwaysSpend) {
        this.name = name;
        this.canAlwaysSpend = canAlwaysSpend;
    }
    
}
