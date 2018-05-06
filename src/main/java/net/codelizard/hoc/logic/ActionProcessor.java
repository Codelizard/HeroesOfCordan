package net.codelizard.hoc.logic;

import net.codelizard.hoc.GameResponse;
import net.codelizard.hoc.HeroesOfCordan;

import java.util.HashMap;
import java.util.Map;

/**
 * Top-level class handling the game's logic and state transitions. It should be service-agnostic so that the logic
 * can be re-used between different messaging services.
 * 
 * @author Codelizard
 */
public class ActionProcessor {
    
    //TOOD: Extract this into a database.
    private final Map<String, Map<String, PlayerState>> gameState = new HashMap<>();
    
    public ActionProcessor() {}
    
    /**
     * Retrieves the current state of a player.
     * @param action The PlayerAction object associated with the player being handled.
     * @return The current state of the specified Player.
     */
    private PlayerState getState(final PlayerAction action) {
        if(!gameState.containsKey(action.getServiceName())) {
            gameState.put(action.getServiceName(), new HashMap<>());
        }
        return gameState.get(action.getServiceName()).get(action.getServiceUserId());
    }
    
    /**
     * Records the current state of a player.
     * @param action The PlayerAction object associated with the player being handled.
     * @param newState The new state to associate with the player.
     */
    private void setState(final PlayerAction action, final PlayerState newState) {
        
        if(!gameState.containsKey(action.getServiceName())) {
            gameState.put(action.getServiceName(), new HashMap<>());
        }
        
        gameState.get(action.getServiceName()).put(action.getServiceUserId(), newState);
        
    }
    
    /**
     * Entry point of the game logic. Given an incoming PlayerAction, determines what state they are in, what state
     * they are moving to, and what text should be sent back to the user.
     * @param action The PlayerAction to process.
     * @return A response to be sent back to the user.
     */
    public GameResponse handleAction(final PlayerAction action) {
        
        final PlayerState currentState = getState(action);
        final String playerText = action.getInputText();
        PlayerState newState;
        
        if(currentState == null /* Never seen this user before */
            || "/start".equalsIgnoreCase(playerText) /* Telegram bot initiation command */ ) {
            
            //We've never seen this user before.
            newState = new PlayerState();
            
        } else if (HeroesOfCordan.getMessage("global.restart").equalsIgnoreCase(playerText)
                || "/restart".equalsIgnoreCase(playerText)) {
            
            //User abandoning in-progress game
            newState = new PlayerState();
            newState.setGameState(GameState.RESTARTING);
            
        } else {
            
            //Main game logic
            try {
                newState = currentState.getGameState().update(currentState, playerText);
            } catch (Exception x) {
                x.printStackTrace();
                return new GameResponse(HeroesOfCordan.getMessage("error.update") + x.getMessage());
            }
            
        }
        
        setState(action, newState);
        
        try {
            return newState.getGameState().enterState(newState);
        } catch (Exception x) {
            x.printStackTrace();
            return new GameResponse(HeroesOfCordan.getMessage("error.enter_state") + x.getMessage());
        }
        
    }
    
}
