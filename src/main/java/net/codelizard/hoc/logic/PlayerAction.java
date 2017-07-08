package net.codelizard.hoc.logic;

import java.util.Objects;

/**
 * <p>Represents an action a player is taking. The bot interface should use this
 * to pass back to the main part of the code, separating the interface from the
 * game logic.</p>
 * 
 * <p>PlayerAction objects are immutable.</p>
 * 
 * @author Codelizard
 */
public final class PlayerAction {
    
    private final String inputText;
    private final String userFirstName;
    private final String serviceName;
    private final String serviceUserId;
    
    /**
     * Creates a new PlayerAction object with the given parameters.
     * @param inputText The user's message.
     * @param userFirstName The first name of the user.
     * @param serviceName The name of the messaging platform the command originates from.
     * @param serviceUserId The user's ID on the specified service.
     */
    PlayerAction(String inputText, String userFirstName, String serviceName, String serviceUserId) {
        this.inputText = inputText;
        this.userFirstName = userFirstName;
        this.serviceName = serviceName;
        this.serviceUserId = serviceUserId;
    }

    /**
     * @return The text from the user.
     */
    public String getInputText() {
        return inputText;
    }

    /**
     * @return The first name of the user (for personalized messages).
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * @return The name of the messaging platform the command originates from.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @return The user's service-specific ID.
     */
    public String getServiceUserId() {
        return serviceUserId;
    }
    
    /**
     * Returns a brief description of this object for debugging purposes.
     * @return A one-line description of this PlayerAction object.
     */
    @Override
    public String toString() {
        return String.format("%s (%s/%s): %s", userFirstName, serviceName, serviceUserId, inputText);
    }

    @Override
    public int hashCode() {
        //Auto-generated
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.inputText);
        hash = 37 * hash + Objects.hashCode(this.serviceName);
        hash = 37 * hash + Objects.hashCode(this.serviceUserId);
        return hash;
    }

    /**
     * Compares this PlayerAction against another object. Two PlayerActions are considered equal if their input text,
     * service name, and service user IDs are the same. (The user's first name doesn't matter)
     * @param obj The object to compare to.
     * @return {@code true} if the the given object is equal to this one, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        final PlayerAction other = (PlayerAction) obj;
        return Objects.equals(this.inputText, other.inputText)
            && Objects.equals(this.serviceName, other.serviceName)
            && Objects.equals(this.serviceUserId, other.serviceUserId);
        
    }
    
}
