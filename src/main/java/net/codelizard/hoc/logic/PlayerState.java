package net.codelizard.hoc.logic;

import net.codelizard.hoc.HeroesOfCordan;
import net.codelizard.hoc.content.*;

import java.util.*;

/**
 * POJO representation of the player's current state in the game.
 * 
 * @todo Store this in a database via Hibernate instead of in-memory.
 * @author Codelizard
 */
public class PlayerState {
    
    /** How many kills the player needs (per floor) to fight the boss. */
    private static final int REQUIRED_KILLS = 10;
    
    /** The current state the player is in. */
    private GameState state = GameState.TITLE;
    
    /** The index in the hero list of the hero the player is currently examining (if any). */
    private int heroIndex;
    
    /** The player's active party. */
    private Party party;
    
    /** The floor the party is currently on (1-indexed). */
    private int floorNumber = 0;
    
    /** How many monsters the player has killed on the current floor. */
    private int kills = 0;
    
    /** The player's current resources. */
    private Map<ResourceType, Integer> currentResources = null;
    
    /** The player's maximum resources. */
    private Map<ResourceType, Integer> maxResources = null;
    
    /** The party's current equipment. */
    private final List<Equipment> equipment = new ArrayList<>();
    
    /** The party's current consumables. */
    private final List<Consumable> consumables = new ArrayList<>();
    
    /** The events on the current floor. */
    private final List<Event> floorEvents = new ArrayList<>();
    
    /** The monsters on the current floor. */
    private final List<Monster> floorMonsters = new ArrayList<>();
    
    /** The equipment on the current floor. */
    private final List<Equipment> floorEquipment = new ArrayList<>();
    
    /** The consumables on the current floor. */
    private final List<Consumable> floorConsumables = new ArrayList<>();
    
    /** Which states the player has seen instruction messages for. */
    private final Set<GameState> seenInstructions = EnumSet.noneOf(GameState.class);
    
    /** Temporary variable holding the state to return to after the current one finishes (for states accessible from
     * multiple other states that are then returned to). */
    private GameState returnState;
    
    /** Temporary variable holding the result of the last transmutation performed. */
    private Item transmuteResult;
    
    /** Temporary variable indicating we're fighting a boss rather than the regular monster. */
    private boolean fightingBoss;
    
    /** Temporary variable indicating that loot has been added to the inventory (to prevent duplication). */
    private boolean lootAwarded;
    
    public PlayerState() {}

    /**
     * @return The state the player is currently in.
     */
    public GameState getGameState() {
        return state;
    }
    
    /**
     * @return The player's currently active party.
     */
    public Party getParty() {
        return party;
    }
    
    /**
     * @return The index of the hero the player is currently looking at in a detail view, if any.
     */
    public int getHeroIndex() {
        return heroIndex;
    }
    
    /**
     * @return How many monsters the player has killed on the current floor.
     */
    public int getKills() {
        return kills;
    }
    
    /**
     * Checks whether or not the player has seen instructions on a particular state.
     * @param gameState The state to check instructions for.
     * @return Whether or not the player has seen the instructions.
     */
    public boolean hasSeenInstructions(GameState gameState) {
        return seenInstructions.contains(gameState);
    }
    
    /**
     * @return The state to return to when the current one completes.
     */
    public GameState getReturnState() {
        return returnState;
    }
    
    /**
     * @return The result of the last transmutation by the party.
     */
    public Item getTransmuteResult() {
        return transmuteResult;
    }
    
    /**
     * @return Whether or not the party is fighting the boss in the next fight.
     */
    public boolean isFightingBoss() {
        return fightingBoss;
    }
    
    /**
     * @return Whether or not the loot from a monster has been awarded yet.
     */
    public boolean isLootAwarded() {
        return lootAwarded;
    }

    /**
     * @param state The new state the player is in.
     */
    public void setGameState(final GameState state) {
        this.state = state;
    }
    
    /**
     * @param party The new party the player is using.
     */
    public void setParty(final Party party) {
        this.party = party;
    }
    
    /**
     * @param heroIndex The new hero index of the hero being examined.
     */
    public void setHeroIndex(final int heroIndex) {
        this.heroIndex = heroIndex;
    }
    
    /**
     * @param kills The new number of kills.
     */
    public void setKills(final int kills) {
        this.kills = kills;
    }
    
    /**
     * Marks whether or not the player has seen instructions on a particular state.
     * @param gameState The state to alter the instructions flag for.
     * @param newFlag Whether or not the player has seen the instructions.
     */
    public void setSeenInstructions(final GameState gameState, final boolean newFlag) {
        if(newFlag) {
            seenInstructions.add(gameState);
        } else {
            seenInstructions.remove(gameState);
        }
    }
    
    /**
     * @param returnState The new return state to use.
     */
    public void setReturnState(final GameState returnState) {
        this.returnState = returnState;
    }
    
    /**
     * @param transmuteResult The new transmute result to use.
     */
    public void setTransmuteResult(final Item transmuteResult) {
        this.transmuteResult = transmuteResult;
    }
    
    /**
     * @param fightingBoss Whether or not the next fight is a boss fight.
     */
    public void setFightingBoss(final boolean fightingBoss) {
        this.fightingBoss = fightingBoss;
    }
    
    /**
     * @param lootAwarded The new flag for whether or not loot has been awarded yet.
     */
    public void setLootAwarded(final boolean lootAwarded) {
        this.lootAwarded = lootAwarded;
    }
    
    /**
     * Returns whether or not the party is below the maximum on one of the specified resources.
     * @param resourceTypes The resource types to check.
     * @return {@code true} if any of the given resources is below max, otherwise {@code false}.
     */
    private boolean isMissingResources(Iterable<ResourceType> resourceTypes) {
        for(ResourceType nextType : resourceTypes) {
            if(this.getResourceCount(nextType) < this.getResourceMax(nextType)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @return Whether or not the party can meaningfully short rest.
     */
    public boolean canShortRest() {
        return isMissingResources(ResourceType.SHORT_REST_RESOURCES);
    }
    
    /**
     * @return Whether or not the party can meaningfully extended rest.
     */
    public boolean canExtendedRest() {
        return isMissingResources(ResourceType.EXTENDED_REST_RESOURCES);
    }
    
    /**
     * @return Whether or not the party has fought all the monsters on this floor. (Highly unlikely)
     */
    public boolean outOfMonsters() {
        return floorMonsters.isEmpty();
    }
    
    /**
     * @return Whether or not the party meets the requirements to fight the current boss.
     */
    public boolean canFightBoss() {
        return kills >= REQUIRED_KILLS;
    }
    
    /**
     * Recalculates the current/maximum resource values based off the party members' resources.
     */
    public void calculateResources() {
        
        maxResources = party.getMaxResources();
        if(currentResources == null) {
            currentResources = party.getMaxResources();
        }
        
        for(Equipment nextEquipment : equipment) {
            
            final Map<ResourceType, ResourceValue> equipmentResources = nextEquipment.getResources();
            for(ResourceType nextResource : equipmentResources.keySet()) {
                
                ResourceValue nextValue = equipmentResources.get(nextResource);
                if(nextValue != null && nextValue.getValue() != 0) {
                    Integer newValue = getResourceMax(nextResource) + nextValue.getValue();
                    maxResources.put(nextResource, newValue);
                }
                
            }
            
        }
        
    }
    
    public String statusReport() {
        
        final StringBuilder output = new StringBuilder();
        
        for(ResourceType nextType : ResourceType.values()) {
            if(output.length() > 0) {
                output.append(", ");
            }
            output.append(getResourceCount(nextType))
                  .append("/")
                  .append(getResourceMax(nextType))
                  .append(" ")
                  .append(nextType.name);
        }
        
        output.append(", ")
              .append(kills)
              .append(" ")
              .append(HeroesOfCordan.getStaticMessage("status.kills"))
              .append(".");
        
        if(!consumables.isEmpty()) {

            output.append("\n\n")
                  .append(HeroesOfCordan.getStaticMessage("status.consumables"));
            
            final StringBuilder consumableText = new StringBuilder();
            for(Consumable nextConsumable : consumables) {
                if(consumableText.length() > 0) {
                    consumableText.append(", ");
                }
                consumableText.append(nextConsumable.benefits());
            }
            output.append(consumableText);

        }
        
        if(!equipment.isEmpty()) {
            
            output.append("\n")
                  .append(HeroesOfCordan.getStaticMessage("status.equipment"));
            
            final StringBuilder equipmentText = new StringBuilder();
            for(Equipment nextEquipment : equipment) {
                if(equipmentText.length() > 0) {
                    equipmentText.append(", ");
                }
                equipmentText.append(nextEquipment.benefits());
            }
            output.append(equipmentText);
            
        }
        
        return output.toString();
        
    }
    
    /**
     * Starts the next floor, pulling in the assets for the upcoming floor and shuffling them into a random order, as
     * well as resetting the kill count.
     */
    public void nextFloor() {
        
        floorNumber++;
        final Tier floor = HeroesOfCordan.getContent().getTiers().get(floorNumber);
        
        floorEvents.clear();
        floorEvents.addAll(floor.getEvents());
        Collections.shuffle(floorEvents);
        
        floorConsumables.clear();
        floorConsumables.addAll(floor.getConsumables());
        Collections.shuffle(floorConsumables);
        
        floorEquipment.clear();
        floorEquipment.addAll(floor.getEquipment());
        Collections.shuffle(floorEquipment);
        
        floorMonsters.clear();
        floorMonsters.addAll(floor.getMonsters());
        Collections.shuffle(floorMonsters);
        
        kills = 0;
        
    }
    
    /**
     * @return The Event on top of the Event deck.
     */
    public Event upcomingEvent() { 
        return floorEvents.get(0);
    }
    
    /**
     * @return The Monster on top of the Monster deck.
     */
    public Monster upcomingMonster() {
        if(fightingBoss) {
            return HeroesOfCordan.getContent().getTiers().get(party.getLevel()).getBoss();
        } else {
            return floorMonsters.get(0);
        }
    }
    
    /**
     * @return The Consumable on top of the Consumable deck.
     */
    public Consumable upcomingConsumable() {
        return floorConsumables.get(0);
    }
    
    /**
     * @return The Equipment on top of the Equipment deck.
     */
    public Equipment upcomingEquipment() {
        return floorEquipment.get(0);
    }
    
    /**
     * @param type The type of resource to get the current value of.
     * @return The current value of the given resource.
     */
    public int getResourceCount(final ResourceType type) {
        return currentResources.getOrDefault(type, 0);
    }
    
    /**
     * @param type The type of resource to get the max value of.
     * @return The maximum value of the given resource.
     */
    public int getResourceMax(final ResourceType type) {
        return maxResources.getOrDefault(type, 0);
    }
    
    /**
     * @return The equipment the party has equipped right now.
     */
    public List<Equipment> getEquipment() {
        return equipment;
    }
    
    /**
     * @return The consumables the party has collected.
     */
    public List<Consumable> getConsumables() {
        return consumables;
    }
    
    /**
     * @return All of the equipment and consumables the party has at the moment.
     */
    public List<Item> getItems() {
        final List<Item> allItems = new ArrayList();
        allItems.addAll(equipment);
        allItems.addAll(consumables);
        return allItems;
    }
    
    /**
     * @return Whether or not the party has any items at all.
     */
    public boolean hasItems() {
        return !consumables.isEmpty() || !equipment.isEmpty();
    }
    
    /**
     * @return Whether or not the party is carrying too much equipment.
     */
    public boolean equipmentOverfull() {
        return equipment.size() > Party.FULL_PARTY_SIZE;
    }
    
    /**
     * @return Whether or not the party is carrying too many consumables.
     */
    public boolean consumablesOverfull() {
        return consumables.size() > Party.FULL_PARTY_SIZE;
    }
    
    private List<String> listItems(final List<? extends Item> items, boolean showType) {
        
        final List<String> output = new ArrayList<>();
        for(int index = 0; index < items.size(); index++) {
            final Item nextItem = items.get(index);
            
            output.add(
                (index+1) + ": "
                    + (showType ? "(" + nextItem.getClass().getSimpleName() + ") " : "") //TODO: Use a localizable string
                    + nextItem.getName() 
                    + " [" + nextItem.benefits() + "]"
            );
            
        }
        
        return output;
        
    }
    
    /**
     * @return An indexed list of the party's currently-held equipment.
     */
    public List<String> listEquipment() {
        return listItems(equipment, false);
    }
    
    /**
     * @return An indexed list of the party's currently-held consumables.
     */
    public List<String> listConsumables() {
        return listItems(consumables, false);
    }
    
    /**
     * @return An indexed list of all the party's currently-held itemPool.
     */
    public List<String> listItems() {
        return listItems(getItems(), true);
    }
    
    /**
     * @return All the heroes' names and classes on one line.
     */
    public String listHeroes() {
        return party.listHeroes();
    }
    
    /**
     * Recovers the given amount of the specified type of resource.
     * @param resourceType The resource to gain.
     * @param gain How much to gain of the specified resource.
     * @param overcharge Whether or not the resource gain can go over the maximum.
     */
    public void gainResource(final ResourceType resourceType, final Integer gain, boolean overcharge) {
        final int max = getResourceMax(resourceType);
        final int current = getResourceCount(resourceType);
        final int newValue = overcharge ? current + gain : Math.min(current + gain, max);
        currentResources.put(resourceType, newValue);
    }
    
    /**
     * Spends the given amount of the specified type of resource.
     * @param resourceType The resource used.
     * @param cost How much to spend of the specified resource.
     */
    public void spendResource(final ResourceType resourceType, final Integer cost) {
        currentResources.put(resourceType, getResourceCount(resourceType) - cost);
    }
    
    /**
     * Uses the given consumable, applying its effecst to the party.
     * @param consumable The consumable to use.
     */
    public void useConsumable(final Consumable consumable) {
        Map<ResourceType, ResourceValue> resources = consumable.getResources();
        for(ResourceType nextType : resources.keySet()) {
            gainResource(nextType, resources.get(nextType).getValue(), true);
        }
    }
    
    /**
     * Resets the party's common resources to their maximum values.
     */
    public void refillResources() {
        for(ResourceType nextType : ResourceType.values()) {
            if(!nextType.canAlwaysSpend) {
                currentResources.put(nextType, getResourceMax(nextType));
            }
        }
    }
    
    /**
     * Resets the party's health to its maximum value.
     */
    public void refillHealth() {
        currentResources.put(ResourceType.HEALTH, getResourceMax(ResourceType.HEALTH));
    }
    
    /**
     * Resets the party's common resources and health to their maximum values.
     */
    public void refillAll() {
        refillResources();
        refillHealth();
    }
    
    /**
     * Given a Consumable in the party's inventory, exchanges it for another from the pool for the same type and tier.
     * It can be any Consumable that matches those criteria, even one that has already appeared this game.
     * (Because Magic!) It will never be the original Consumable unless it is the only possibility.
     * @param oldConsumable The Consumable to transmute.
     * @return The resulting Consumable.
     */
    public Consumable transmuteConsumable(final Consumable oldConsumable) {
        
        final Tier itemTier = HeroesOfCordan.getContent().getTiers().get(oldConsumable.getTier());
        final List<Consumable> pool = itemTier.getConsumables();
        
        if(pool.size() == 1) {
            
            //Just in case someone provided a data file with only one item this tier
            return pool.get(0);
            
        } else {
            
            Consumable newConsumable;
            do {
                final int index = (int) Math.round(pool.size() * Math.random());
                newConsumable = pool.get(index);                
            } while(newConsumable.getId().equals(oldConsumable.getId()));
            
            consumables.remove(oldConsumable);
            consumables.add(newConsumable);
            
            return newConsumable;
            
        }
        
    }
    
    /**
     * Given an Equipment in the party's inventory, exchanges it for another from the pool for the same type and tier.
     * It can be any Equipment that matches those criteria, even one that has already appeared this game.
     * (Because Magic!) It will never be the original Equipment unless it is the only possibility.
     * @param oldEquipment The Equipment to transmute.
     * @return The resulting Equipment.
     */
    public Equipment transmuteEquipment(final Equipment oldEquipment) {
        
        final Tier itemTier = HeroesOfCordan.getContent().getTiers().get(oldEquipment.getTier());
        final List<Equipment> pool = itemTier.getEquipment();
        
        if(pool.size() == 1) {
            
            //Just in case someone provided a data file with only one item this tier
            return pool.get(0);
            
        } else {
            
            Equipment newEquipment;
            do {
                final int index = (int) Math.round(pool.size() * Math.random());
                newEquipment = pool.get(index);                
            } while(newEquipment.getId().equals(oldEquipment.getId()));
            
            equipment.remove(oldEquipment);
            equipment.add(newEquipment);
            
            return newEquipment;
            
        }
        
    }
    
    /**
     * Takes the event at the top of the deck and puts it at the bottom, getting a new one in its place.
     */
    public void redrawEvent() {
        if(!floorEvents.isEmpty()) {
            floorEvents.add(floorEvents.remove(0));
        }
    }
    
    /**
     * Takes the monster at the top of the deck and puts it at the bottom, getting a new one in its place.
     */
    public void redrawMonster() {
        if(!floorMonsters.isEmpty()) {
            floorMonsters.add(floorMonsters.remove(0));
        }
    }
    
    /**
     * Removes the current event from the game.
     */
    public void nextEvent() {
        floorEvents.remove(0);
    }
    
    /**
     * Removes the current monster from the game and increments the kill count.
     */
    public void nextMonster() {
        floorMonsters.remove(0);
        kills++;
    }
    
    /**
     * Removes the current consumable from the game.
     */
    public void nextConsumable() {
        floorConsumables.remove(0);
    }
    
    /**
     * Removes the current equipment from the game.
     */
    public void nextEquipment() {
        floorEquipment.remove(0);
    }
    
}
