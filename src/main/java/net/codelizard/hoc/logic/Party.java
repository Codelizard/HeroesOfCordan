package net.codelizard.hoc.logic;

import net.codelizard.hoc.content.DiscountType;
import net.codelizard.hoc.content.Hero;
import net.codelizard.hoc.content.HeroDiscount;
import net.codelizard.hoc.content.ResourceType;

import java.util.*;

/**
 * Represents the active party and provides helper methods for interacting with the party as a whole.
 * 
 * @author Codelizard
 */
public class Party {
    
    /** How big a party has to be to be considered "full". */
    public static final int FULL_PARTY_SIZE = 4;
    
    /** Default amount of time the party has to beat the game. */
    private static final int TIME_LIMIT = 250;
    
    /** The base health of the party. The party gets 1 more each level they gain past 1. */
    private static final int HEALTH_BASE = 10;
    
    /** The heroes making up the party. */
    private final List<Hero> heroes = new ArrayList<>();
    
    /** The party's current level. */
    private int level = 1;
    
    /**
     * Creates a new, empty Party.
     */
    public Party() {}
    
    /**
     * Creates a new Party consisting of the given heroes.
     * @param heroes The heroes to start the party with.
     */
    public Party(final Collection<Hero> heroes) {
        this.heroes.addAll(heroes);
    }
    
    /**
     * @return The party's current level.
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * @param level The new party level.
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Adds a new hero the party.
     * @param hero The hero to add.
     */
    public void addHero(Hero hero) {
        heroes.add(hero);
    }
    
    /**
     * Removes a hero from the party.
     * @param hero The hero to remove.
     * @return {@code true} if the hero was removed, {@code false} if they were not in the party.
     */
    public boolean removeHero(Hero hero) {
        return heroes.remove(hero);
    }
    
    /**
     * @param hero The hero to check for.
     * @return {@code true} if the party contains the Hero, {@code false} if not.
     */
    public boolean containsHero(Hero hero) {
        return heroes.contains(hero);
    }
    
    /**
     * @return The number of heroes in the party.
     */
    public int partySize() {
        return heroes.size();
    }
    
    /**
     * @return Whether or not the party is considered full.
     */
    public boolean isFull() {
        return heroes.size() >= FULL_PARTY_SIZE;
    }

    /**
     * @return A mapping representing the party's resource pool maximum counts.
     */
    public Map<ResourceType, Integer> getMaxResources() {
        
        final Map<ResourceType, Integer> maxResources = new HashMap<>();
        maxResources.put(ResourceType.TIME, TIME_LIMIT);
        maxResources.put(ResourceType.HEALTH, HEALTH_BASE + (level - 1));
        
        for(Hero nextHero : heroes) {
            final Map<ResourceType, Integer> heroResources = nextHero.getResources().get(level);
            for(ResourceType nextResource : heroResources.keySet()) {
                maxResources.put(
                    nextResource,
                    maxResources.getOrDefault(nextResource, 0) + heroResources.get(nextResource)
                );
            }
        }
        
        return maxResources;
    }
    
    public Integer eventDiscount(final ResourceType resourceType) {
        return calculateDiscount(DiscountType.EVENT, resourceType);
    }
    
    public Integer monsterDiscount(final ResourceType resourceType) {
        return calculateDiscount(DiscountType.MONSTER, resourceType);
    }
    
    private Integer calculateDiscount(final DiscountType discountType, final ResourceType resourceType) {
        
        int totalDiscount = 0;
        
        for(Hero nextHero : heroes) {
            final HeroDiscount discount = nextHero.getDiscount();
            if(discountType.equals(discount.getType()) && resourceType.equals(discount.getResource())) {
                totalDiscount++;
            }
        }
        
        return totalDiscount;
        
    }
    
    public String listHeroes() {
        
        final StringBuilder output = new StringBuilder();
        
        for(Hero nextHero : heroes) {
            if(output.length() > 0) {
                output.append(", ");
            }
            output.append(nextHero.getName())
                  .append(" the ") //TODO: Un-hardcode this
                  .append(nextHero.getRpgClass());
        }
        
        return output.toString();
        
    }
    
    /**
     * @return A one-line description of this Party for debugging purposes.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[ ");
        for(Hero nextHero : heroes) {
            builder.append(nextHero.toString());
            builder.append(" ");
        }
        builder.append("]");
        return builder.toString();
    }
    
}
