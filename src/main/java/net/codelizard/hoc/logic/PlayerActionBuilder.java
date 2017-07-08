package net.codelizard.hoc.logic;

/**
 * Builder object for creating {@link PlayerAction} objects.
 * 
 * @author Codelizard
 */
public class PlayerActionBuilder {
    
    private String inputText;
    private String userFirstName;
    private String serviceName;
    private String serviceUserId;
    
    /** Creates a new, blank PlayerActionBuilder. */
    public PlayerActionBuilder() {}

    /**
     * @param inputText The text from the user.
     * @return This builder.
     */
    public PlayerActionBuilder setInputText(String inputText) {
        this.inputText = inputText;
        return this;
    }

    /**
     * @param userFirstName
     * @return 
     */
    public PlayerActionBuilder setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
        return this;
    }

    /**
     * @param serviceName
     * @return 
     */
    public PlayerActionBuilder setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    /**
     * @param serviceUserId
     * @return 
     */
    public PlayerActionBuilder setServiceUserId(String serviceUserId) {
        this.serviceUserId = serviceUserId;
        return this;
    }
    
    /**
     * Instantiates a new {@link PlayerAction} from the specified parameters.
     * @return The created PlayerAction object.
     * @throws IllegalArgumentException If any required parameters are {@code null}.
     */
    public PlayerAction build() {
        if(inputText == null) {
            throw new IllegalArgumentException("inputText is required.");
        } else if(userFirstName == null) {
            throw new IllegalArgumentException("userFirstName is required.");
        } else if(serviceName == null) {
            throw new IllegalArgumentException("serviceName is required.");
        } else if(serviceUserId == null) {
            throw new IllegalArgumentException("serviceUserId is required.");
        }
        return new PlayerAction(inputText, userFirstName, serviceName, serviceUserId);
    }
    
}
