package net.codelizard.hoc.logic;

import net.codelizard.hoc.GameResponse;
import net.codelizard.hoc.HeroesOfCordan;
import net.codelizard.hoc.content.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents all of the mutually exclusive potential states a player can be in at any given time. This is used to
 * simulate a finite state machine.
 * 
 * @author Codelizard
 */
public enum GameState {
    
    /** The player is on the title (such as it is) and is choosing whether to start, read instructions, etc. */
    TITLE() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            return new GameResponse(
                getStaticMessage("opening.text"),
                getStaticMessage("opening.instructions"),
                getStaticMessage("opening.new_game"),
                getStaticMessage("opening.advanced_start")
            );
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            if(getStaticMessage("opening.instructions").equalsIgnoreCase(playerText)) {
                currentState.setParty(new Party());
                currentState.setGameState(INSTRUCTIONS);
            } else if(getStaticMessage("opening.new_game").equalsIgnoreCase(playerText)) {
                currentState.setParty(new Party(HeroesOfCordan.getContent().fourRandomHeroes()));
                currentState.calculateResources();
                currentState.setGameState(ENTER_DUNGEON);
            } else if(getStaticMessage("opening.advanced_start").equalsIgnoreCase(playerText)) {
                currentState.setParty(new Party());
                currentState.setGameState(SELECT_HEROES);
            }
            
            return currentState;
            
        }
        
    },
    
    /** The player just abandoned an in-progress game and is being sent back to the title. */
    RESTARTING() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            return new GameResponse(
                getDynamicMessages().randomRestartMessage(),
                getStaticMessage("global.confirm")
            );
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            currentState.setGameState(TITLE);
            return currentState;
            
        }
        
    },
    
    /** The player is reading the game instructions. */
    INSTRUCTIONS() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            return new GameResponse(
                getStaticMessage("instructions.text"),
                getStaticMessage("global.confirm")
            );
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            //Accept any response even if it's not the exact one expected.
            /* Also, send the party into the Quick Start after reading the instructions;
             * new players shouldn't be dumped into the advanced setup. */
            currentState.setParty(new Party(HeroesOfCordan.getContent().fourRandomHeroes()));
            currentState.calculateResources();
            currentState.setGameState(ENTER_DUNGEON);
            return currentState;
            
        }
        
    },
    
    /** The player is selecting who they want to subject to the dungeon. */
    SELECT_HEROES() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            final StringBuilder message = new StringBuilder();
            message.append(getStaticMessage("select_hero.text")).append("\n");
            
            final List<Hero> heroes = HeroesOfCordan.getContent().getHeroes();
            final List<String> options = new ArrayList<>(); //Button texts given to the user
            
            for(int index = 0; index < heroes.size(); index++) {
                
                final Hero nextHero = heroes.get(index);
                
                if(!currentState.getParty().containsHero(nextHero)) {
                
                    message.append("\n")
                           .append(index)
                           .append(": ")
                           .append(nextHero.getRpgClass())
                           .append(" (")
                           .append(nextHero.getDescription())
                           .append(")");

                    options.add(String.valueOf(index));
                
                }
                
            }
            
            GameResponse response = new GameResponse(message.toString(), options);
            response.setColumns(4);
            
            return response;
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            int index;
            try {
                index = Integer.parseInt(playerText);
            } catch (NumberFormatException nfx) {
                //Invalid input, resend the list.
                return currentState;
            }
            
            final List<Hero> heroes = HeroesOfCordan.getContent().getHeroes();
            if(index >= 0 && index < heroes.size()) {
                
                //Valid index, but make sure the hero isn't in the party already (if they typed a response)
                if(!currentState.getParty().containsHero(heroes.get(index))) {
                    
                    currentState.setHeroIndex(index);
                    currentState.setGameState(HERO_DETAIL);
                    
                }
                
            }
            
            return currentState;
            
        }
        
    },
    
    /** The player is viewing a hero detail screen. */
    HERO_DETAIL() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            final Hero hero = HeroesOfCordan.getContent().getHeroes().get(currentState.getHeroIndex());
            
            final StringBuilder message = new StringBuilder();
            message.append(hero.getRpgClass())
                   .append(": ")
                   .append(hero.getDescription())
                   .append("\n")
                   .append(hero.getInitialResourcesText())
                   .append("\n\n")
                   .append(hero.getFlavor())
                   .append("\n\n")
                   .append(hero.getQuote());
            
            return new GameResponse(
                message.toString(),
                getStaticMessage("hero_detail.select"),
                getStaticMessage("hero_detail.cancel")
            );
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            if(getStaticMessage("hero_detail.select").equalsIgnoreCase(playerText)) {
                
                final Hero hero = HeroesOfCordan.getContent().getHeroes().get(currentState.getHeroIndex());
                currentState.getParty().addHero(hero);
                
                if(currentState.getParty().isFull()) {
                    currentState.calculateResources();
                    currentState.setGameState(ENTER_DUNGEON);
                } else {
                    currentState.setGameState(SELECT_HEROES);
                }
                
            } else if(getStaticMessage("hero_detail.cancel").equalsIgnoreCase(playerText)) {
                
                currentState.setGameState(SELECT_HEROES);
                
            }
            
            return currentState;
            
        }
        
    },
    
    /** The player is entering the dungeon. */
    ENTER_DUNGEON() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            String output = getDynamicMessages().randomEnterDungeonMessage()
                    + "\n\n"
                    + currentState.listHeroes()
                    + "...\n"
                    + getDynamicMessages().randomEnterDungeonClosingMessage();
            
            return new GameResponse(output, getStaticMessage("enter_dungeon.start"));
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            currentState.nextFloor();
            
            //Any response is acceptable.
            currentState.setGameState(EVENT);
            return currentState;
            
        }
        
    },
    
    /** The player is in the game and is encountering an Event. */
    EVENT() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            final Event nextEvent = currentState.upcomingEvent();
            final StringBuilder output = new StringBuilder();
            
            if(!currentState.hasSeenInstructions(EVENT)) {
                output.append(getStaticMessage("event.instructions"));
                output.append("\n\n");
                currentState.setSeenInstructions(EVENT, true);
            }
            
            //General decription text
            output.append(nextEvent.fullLengthDescription())
                  .append("\n\n");
            
            //Defeat costs
            output.append(nextEvent.costListing(currentState));
            output.append("\n");
            final List<String> responses = new ArrayList<>();
            for(ResourceType nextType : nextEvent.usableResources(currentState)) {
                responses.add(nextType.name);
            }
            
            //Remind the player what they have, since they're about to spend some.
            output.append(currentState.statusReport());
            
            //If the player has any consumables, give them the option to use one
            if(!currentState.getConsumables().isEmpty()) {
                responses.add(getStaticMessage("global.use_consumable"));
            }
            
            return new GameResponse(
                output.toString(),
                responses
            );
        }

        /** Player is responding to an event with the resource they wish to spend, or "use consumable". */
        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            final Event nextEvent = currentState.upcomingEvent();
            
            if(getStaticMessage("global.use_consumable").equalsIgnoreCase(playerText)) {
                
                currentState.setReturnState(EVENT);
                currentState.setGameState(USE_CONSUMABLE);
                return currentState;
                
            } else {
                
                ResourceType spentResource;
                try {
                    spentResource = ResourceType.valueOf(playerText.toUpperCase());
                } catch (IllegalArgumentException iax) {
                    //Re-prompt for input
                    return currentState;
                }
                
                final Map<ResourceType, ResourceValue> eventResources = nextEvent.getResources();
                final ResourceValue value = eventResources.get(spentResource);
                if(nextEvent.canDefeat(spentResource, currentState)) {
                    
                    int cost = value.getValue();
                    final int discount = currentState.getParty().eventDiscount(spentResource);
                    
                    //Discounts can't bring a resource cost below 1
                    cost = Math.max(1, cost - discount);
                    
                    currentState.spendResource(spentResource, cost);
                    
                    //Did the party run out of time?
                    if(currentState.getResourceCount(ResourceType.TIME) <= 0) {
                        currentState.setGameState(OUT_OF_TIME);
                        return currentState;
                    }
                    
                    //If they didn't run out of time, did they run out of health?
                    if(currentState.getResourceCount(ResourceType.HEALTH) <= 0) {
                        currentState.setGameState(OUT_OF_HEALTH);
                        return currentState;
                    }
                    
                    //Otherwise, the party wins and can progress to an action.
                    currentState.nextEvent();
                    if(currentState.canFightBoss()) {
                        currentState.setGameState(READY_FOR_BOSS);
                    } else {
                        currentState.setGameState(ACTION);
                    }
                    return currentState;
                    
                } else {
                    
                    //Can't spend that resource - re-prompt for input
                    return currentState;
                    
                }
                
            }
            
        }
        
    },
    
    /** The player is deciding which action to take next. */
    ACTION() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            final StringBuilder message = new StringBuilder();
            
            if(!currentState.hasSeenInstructions(ACTION)) {
                
                //General instructions
                message.append(getStaticMessage("action.instructions"))
                       .append("\n\n");
                
                //Then list all the actions the player can do, even if they don't have the resources.
                message.append(getStaticMessage("instructions.fight_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.short_rest_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.long_rest_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.charge_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.transmute_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.cure_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.mass_cure_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.scout_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.secret_door_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.use_consumable_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.boss_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.boss_charge_text"))
                       .append("\n")
                       .append(getStaticMessage("instructions.repeat_instructions_text"));
                
                //Then prevent the prompt from showing up again unless asked
                currentState.setSeenInstructions(ACTION, true);
                
            }
            
            //Always close with the status report at the bottom for the reader's convenience.
            message.append("\n\n")
                   .append(currentState.statusReport());
            
            //Only add buttons for things the player has enough resources for.
            final List<String> responses = new ArrayList<>();
            if(!currentState.outOfMonsters()) {
                responses.add(getStaticMessage("action.fight"));
            }
            
            if(currentState.canShortRest()) {
                responses.add(getStaticMessage("action.short_rest"));
            }
            
            if(currentState.canExtendedRest()) {
                responses.add(getStaticMessage("action.long_rest"));
            }
            
            if(currentState.getResourceCount(ResourceType.PHYSICAL) > 0
                && !currentState.outOfMonsters()) {
                responses.add(getStaticMessage("action.charge"));
            }
            
            if(currentState.getResourceCount(ResourceType.ARCANE) > 0
                && currentState.hasItems()) { //Only if they actually have something to transmute
                responses.add(getStaticMessage("action.transmute"));
            }
            
            if(currentState.getResourceCount(ResourceType.DIVINE) > 0) {
                responses.add(getStaticMessage("action.cure"));
            }
            
            if(currentState.getResourceCount(ResourceType.DIVINE) >= 3) {
                responses.add(getStaticMessage("action.mass_cure"));
            }
            
            if(currentState.getResourceCount(ResourceType.STEALTH) > 0) {
                responses.add(getStaticMessage("action.scout"));
            }
            
            if(currentState.getResourceCount(ResourceType.MECHANICAL) > 0
                && !currentState.outOfMonsters()) {
                responses.add(getStaticMessage("action.secret_door"));
            }
            
            if(currentState.hasItems()) {
                responses.add(getStaticMessage("action.use_consumable"));
            }
            
            if(currentState.canFightBoss()) {
                responses.add(getStaticMessage("action.boss"));
                if(currentState.getResourceCount(ResourceType.PHYSICAL) > 0) {
                    responses.add(getStaticMessage("action.boss_charge"));
                }
            }
            
            responses.add(getStaticMessage("action.repeat_instructions"));
            
            return new GameResponse(message.toString(), responses);
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            if(getStaticMessage("action.fight").equalsIgnoreCase(playerText)) {
                
                currentState.spendResource(ResourceType.TIME, FIGHT_TIME_COST);
                currentState.setGameState(MONSTER);
                
            } else if(getStaticMessage("action.short_rest").equalsIgnoreCase(playerText)) {
                
                currentState.spendResource(ResourceType.TIME, SHORT_REST_TIME_COST);
                currentState.setGameState(SHORT_REST);
                
            } else if(getStaticMessage("action.long_rest").equalsIgnoreCase(playerText)) {
                
                currentState.spendResource(ResourceType.TIME, LONG_REST_TIME_COST);
                currentState.setGameState(LONG_REST);
                
            } else if(getStaticMessage("action.charge").equalsIgnoreCase(playerText)) {
                
                if(currentState.getResourceCount(ResourceType.PHYSICAL) >= CHARGE_PHYSICAL_COST) {
                    currentState.spendResource(ResourceType.PHYSICAL, CHARGE_PHYSICAL_COST);
                    currentState.spendResource(ResourceType.TIME, CHARGE_TIME_COST);
                    currentState.setGameState(MONSTER);
                }
                
            } else if(getStaticMessage("action.transmute").equalsIgnoreCase(playerText)) {
                
                if(currentState.getResourceCount(ResourceType.ARCANE) >= TRANSMUTE_ARCANE_COST
                    && currentState.hasItems()) {
                    //Don't spend resources yet; they might back out of transmuting
                    currentState.setGameState(TRANSMUTE);
                }
                
            } else if(getStaticMessage("action.cure").equalsIgnoreCase(playerText)) {
                
                if(currentState.getResourceCount(ResourceType.DIVINE) >= CURE_DIVINE_COST) {
                    currentState.spendResource(ResourceType.DIVINE, CURE_DIVINE_COST);
                    currentState.spendResource(ResourceType.TIME, CURE_TIME_COST);
                    currentState.setGameState(CURE);
                }
                
            } else if(getStaticMessage("action.mass_cure").equalsIgnoreCase(playerText)) {
                
                if(currentState.getResourceCount(ResourceType.DIVINE) >= MASS_CURE_DIVINE_COST) {
                    currentState.spendResource(ResourceType.DIVINE, MASS_CURE_DIVINE_COST);
                    currentState.spendResource(ResourceType.TIME, MASS_CURE_TIME_COST);
                    currentState.setGameState(MASS_CURE);
                }
                
            } else if(getStaticMessage("action.scout").equalsIgnoreCase(playerText)) {
                
                if(currentState.getResourceCount(ResourceType.STEALTH) >= SCOUT_STEALTH_COST) {
                    currentState.spendResource(ResourceType.STEALTH, SCOUT_STEALTH_COST);
                    currentState.spendResource(ResourceType.TIME, SCOUT_TIME_COST);
                    currentState.setGameState(SCOUT);
                }
                
            } else if(getStaticMessage("action.secret_door").equalsIgnoreCase(playerText)) {
                
                if(currentState.getResourceCount(ResourceType.MECHANICAL) >= SECRET_DOOR_MECHANICAL_COST) {
                    currentState.spendResource(ResourceType.MECHANICAL, SECRET_DOOR_MECHANICAL_COST);
                    currentState.spendResource(ResourceType.TIME, SECRET_DOOR_TIME_COST);
                    currentState.setGameState(SECRET_DOOR);
                }
                
            } else if(getStaticMessage("action.use_consumable").equalsIgnoreCase(playerText)) {
                
                if(currentState.hasItems()) {
                    currentState.setReturnState(ACTION);
                    currentState.setGameState(USE_CONSUMABLE);
                }
                
            } else if(getStaticMessage("action.boss").equalsIgnoreCase(playerText)) {
                
                if(currentState.canFightBoss()) {
                    currentState.spendResource(ResourceType.TIME, BOSS_TIME_COST);
                    currentState.setFightingBoss(true);
                    currentState.setGameState(MONSTER);
                }
                
            } else if(getStaticMessage("action.boss_charge").equalsIgnoreCase(playerText)) {
                
                if(currentState.canFightBoss()
                    && currentState.getResourceCount(ResourceType.PHYSICAL) > BOSS_CHARGE_PHYSICAL_COST) {
                    currentState.spendResource(ResourceType.PHYSICAL, BOSS_CHARGE_PHYSICAL_COST);
                    currentState.spendResource(ResourceType.TIME, BOSS_CHARGE_TIME_COST);
                    currentState.setFightingBoss(true);
                    currentState.setGameState(MONSTER);
                }
                
            } else if(getStaticMessage("action.repeat_instructions").equalsIgnoreCase(playerText)) {
                
                currentState.setSeenInstructions(ACTION, false);
                
            }

            //Override state change if we ran out of time
            if(currentState.getResourceCount(ResourceType.TIME) <= 0) {
                currentState.setGameState(OUT_OF_TIME);
            }
            
            return currentState;
            
        }
        
    },
    
    /** The player is fighting a monster. */
    MONSTER() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            final Monster nextMonster = currentState.upcomingMonster();
            final StringBuilder output = new StringBuilder();
            
            if(!currentState.hasSeenInstructions(MONSTER)) {
                output.append(getStaticMessage("monster.instructions"));
                output.append("\n\n");
                currentState.setSeenInstructions(MONSTER, true);
            }
            
            //General decription text
            output.append(nextMonster.fullLengthDescription())
                  .append("\n\n");
            
            //Defeat costs
            output.append(nextMonster.costListing(currentState));
            output.append("\n");
            
            //Remind the player what they have, since they're about to spend some.
            output.append(currentState.statusReport());
            
            final List<String> responses = new ArrayList<>();
            for(ResourceType nextType : nextMonster.usableResources(currentState)) {
                responses.add(nextType.name);
            }
            
            //If the player has any consumables, give them the option to use one
            if(!currentState.getConsumables().isEmpty()) {
                responses.add(getStaticMessage("global.use_consumable"));
            }
            
            return new GameResponse(
                output.toString(),
                responses
            );
            
        }

        /** Player is responding to a monster with the resource they wish to spend, or "use consumable". */
        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            final Monster nextMonster = currentState.upcomingMonster();
            
            if(getStaticMessage("global.use_consumable").equalsIgnoreCase(playerText)) {
                
                currentState.setReturnState(MONSTER);
                currentState.setGameState(USE_CONSUMABLE);
                return currentState;
                
            } else {
                
                ResourceType spentResource;
                try {
                    spentResource = ResourceType.valueOf(playerText.toUpperCase());
                } catch (IllegalArgumentException iax) {
                    //Re-prompt for input
                    return currentState;
                }
                
                final Map<ResourceType, ResourceValue> eventResources = nextMonster.getResources();
                final ResourceValue value = eventResources.get(spentResource);
                if(nextMonster.canDefeat(spentResource, currentState)) {
                    
                    //If this was a boss, exit boss-fightin' mode.
                    currentState.setFightingBoss(false);
                    
                    int discount = currentState.getParty().monsterDiscount(spentResource);

                    //Discounts can't bring a resource cost below 1
                    int cost = Math.max(1, value.getValue() - discount);
                    currentState.spendResource(spentResource, cost);
                    
                    //Did the party run out of time?
                    if(currentState.getResourceCount(ResourceType.TIME) <= 0) {
                        currentState.setGameState(OUT_OF_TIME);
                        return currentState;
                    }
                    
                    //If they didn't run out of time, did they run out of health?
                    if(currentState.getResourceCount(ResourceType.HEALTH) <= 0) {
                        currentState.setGameState(OUT_OF_HEALTH);
                        return currentState;
                    }
                    
                    //Otherwise, the party wins and collects their loot.
                    final LootType lootType = nextMonster.getLootType();
                    switch (lootType) {
                        case CONSUMABLE:
                            currentState.setGameState(LOOT_CONSUMABLE);
                            break;
                        case EQUIPMENT:
                            currentState.setGameState(LOOT_EQUIPMENT);
                            break;
                        case LEVELUP:
                            currentState.setGameState(LOOT_LEVELUP);
                            break;
                        case WIN:
                            currentState.setGameState(VICTORY);
                            break;
                        default:
                            //Should never happen. Just in case, send them back to an event.
                            currentState.setGameState(EVENT);
                            throw new RuntimeException("Unrecognized loot type: " + lootType.toString());
                    }
                    currentState.nextMonster();
                    return currentState;
                    
                } else {
                    
                    //Can't spend that resource - re-prompt for input
                    return currentState;
                    
                }
                
            }
            
        }
        
    },
    
    /** The player just reached 10 kills on this floor and is getting a notification that they can go fight the boss. */
    READY_FOR_BOSS() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            return new GameResponse(getStaticMessage("ready_for_boss.message"), getStaticMessage("global.confirm"));
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            //Back to the Action menu regardless of what they responded with.
            currentState.setGameState(ACTION);
            return currentState;
            
        }
        
    },
    
    /** The party is collecting a consumable from a defeated monster. */
    LOOT_CONSUMABLE() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            final Consumable loot = currentState.upcomingConsumable();
            
            // TODO: Probably not the best way to do this, maybe hide list interactions behind methods
            //Just in case they cycle back to this state by botching their input...
            if(!currentState.isLootAwarded()) {
                currentState.getConsumables().add(loot);
                currentState.nextConsumable();
                currentState.setLootAwarded(true);
            }
            
            String output = getStaticMessage("loot.message")
                    + "\n\n"
                    + loot.fullLengthDescription()
                    + "\n\n"
                    + getStaticMessage("loot.consumable_blurb")
                    + "\n"
                    + loot.benefits();
            
            if(currentState.consumablesOverfull()) {
                output += "\n\n" + getStaticMessage("loot.consumables_full");
                return new GameResponse(output, currentState.listConsumables());
            } else {
                return new GameResponse(output, getStaticMessage("global.confirm"));
            }
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            if(currentState.consumablesOverfull()) {
                
                int index = parseIndex(playerText);
                if(index >= 0 && index <= Party.FULL_PARTY_SIZE + 1) {
                    //TODO: Do this better instead of accessing the list directly
                    Consumable usedConsumable = currentState.getConsumables().remove(index);
                    currentState.useConsumable(usedConsumable);
                    currentState.setLootAwarded(false);
                    currentState.setGameState(EVENT);
                }
                
            } else {
                
                //If they don't have too much stuff we don't care what they responded with.
                currentState.setLootAwarded(false);
                currentState.setGameState(EVENT);
                
            }
            
            return currentState;
            
        }
        
    },
    
    /** The party is collecting a piece of equipment from a defeated monster. */
    LOOT_EQUIPMENT() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            final Equipment loot = currentState.upcomingEquipment();
            
            // TODO: Probably not the best way to do this, maybe hide list interactions behind methods
            //Just in case they cycle back to this state by botching their input...
            if(!currentState.isLootAwarded()) {
                currentState.getEquipment().add(loot);
                currentState.nextEquipment();
                currentState.setLootAwarded(true);
            }
            
            String output = getStaticMessage("loot.message")
                    + "\n\n"
                    + loot.fullLengthDescription()
                    + "\n\n"
                    + getStaticMessage("loot.equipment_blurb")
                    + "\n"
                    + loot.benefits();
            
            if(currentState.equipmentOverfull()) {
                output += "\n\n" + getStaticMessage("loot.equipment_full");
                return new GameResponse(output, currentState.listEquipment());
            } else {
                return new GameResponse(output, getStaticMessage("global.confirm"));
            }
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            if(currentState.equipmentOverfull()) {
                
                int index = parseIndex(playerText);
                if(index >= 0 && index <= Party.FULL_PARTY_SIZE + 1) {
                    //TODO: Do this better instead of accessing the list directly
                    currentState.getEquipment().remove(index);
                    currentState.calculateResources();
                    currentState.setLootAwarded(false);
                    currentState.setGameState(EVENT);
                }
                
            } else {
                
                //If they don't have too much stuff we don't care what they responded with.
                currentState.calculateResources();
                currentState.setLootAwarded(false);
                currentState.setGameState(EVENT);
                
            }
            
            return currentState;
            
        }
        
    },
    
    /** The party is levelling up after defeating a boss. */
    LOOT_LEVELUP() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            currentState.getParty().setLevel(currentState.getParty().getLevel() + 1);
            currentState.calculateResources();
            currentState.nextFloor();
            
            String output = getStaticMessage("levelup.opening")
                    + "\n\n"
                    + currentState.statusReport()
                    + "\n\n"
                    + getStaticMessage("levelup.closing");
            
            return new GameResponse(output, getStaticMessage("global.confirm"));
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            //Move them along to the next floor.
            currentState.setGameState(EVENT);
            return currentState;
            
        }
        
    },
    
    /** The player is using a consumable. */
    USE_CONSUMABLE() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            final List<String> options = currentState.listConsumables();
            options.add(getStaticMessage("global.cancel"));
            
            return new GameResponse(getStaticMessage("use_consumable.message"), options);
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            int index = parseIndex(playerText);
            
            if(index >= 0 && index < currentState.getConsumables().size()) {
                Consumable usedConsumable = currentState.getConsumables().remove(index);
                currentState.useConsumable(usedConsumable);
            }
            
            currentState.setGameState(currentState.getReturnState());
            currentState.setReturnState(null);
            return currentState;
            
        }
        
    },
    
    /** The party is briefly resting to recover resources. */
    SHORT_REST() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            currentState.refillResources();
            return new GameResponse(getDynamicMessages().randomShortRestMessage(), getStaticMessage("global.confirm"));
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            currentState.setGameState(ACTION);
            return currentState;
        }
        
    },
    
    /** The party is taking a long rest to recover all their wounds and resources. */
    LONG_REST() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            currentState.refillAll();
            return new GameResponse(getDynamicMessages().randomLongRestMessage(), getStaticMessage("global.confirm"));
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            currentState.setGameState(ACTION);
            return currentState;
        }
        
    },
    
    /** The party is transmuting an item. */
    TRANSMUTE() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            StringBuilder output = new StringBuilder();
            
            if(!currentState.hasSeenInstructions(TRANSMUTE)) {
                output.append(getStaticMessage("transmute.instructions"));
                output.append("\n\n");
                currentState.setSeenInstructions(TRANSMUTE, true);
            }
            
            output.append("transmute.message");
            
            final List<String> options = currentState.listItems();
            options.add(getStaticMessage("global.cancel"));
            
            return new GameResponse(output.toString(), options);
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            int index = parseIndex(playerText);
            
            final List<Item> items = currentState.getItems();
            
            if(index >= 0 && index < items.size()) {
                
                Item selected = items.get(index);
                Item newItem;
                if(selected instanceof Consumable) {
                    newItem = currentState.transmuteConsumable((Consumable) selected);
                } else {
                    newItem = currentState.transmuteEquipment((Equipment) selected);
                }
                
                currentState.setTransmuteResult(newItem);
                
                //Now that the transmutation has been confirmed, spend resources
                currentState.spendResource(ResourceType.ARCANE, TRANSMUTE_ARCANE_COST);
                currentState.spendResource(ResourceType.TIME, TRANSMUTE_TIME_COST);
                
                //And recalculate the new maximums in case we transmuted equipment
                currentState.calculateResources();
                
                if(currentState.getResourceCount(ResourceType.TIME) <= 0) {
                    currentState.setGameState(OUT_OF_TIME);
                } else {
                    currentState.setGameState(TRANSMUTE_RESULT);
                }
                
            } else {
                
                currentState.setGameState(ACTION);
                
            }
            
            return currentState;
            
        }
        
    },
    
    /** The party is being informed of the results of a transmutation. */
    TRANSMUTE_RESULT() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            final Item result = currentState.getTransmuteResult();
            
            final String blurb = result instanceof Consumable ?
                    getStaticMessage("loot.consumable_blurb") :
                    getStaticMessage("loot.equipment_blurb");
            
            final String output = getStaticMessage("transmute.result")
                    + "\n\n"
                    + result.fullLengthDescription()
                    + "\n\n"
                    + blurb
                    + "\n"
                    + result.benefits();
            
            return new GameResponse(output, getStaticMessage("global.confirm"));
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            //Doesn't matter what their response was.
            currentState.setTransmuteResult(null);
            currentState.setGameState(ACTION);
            return currentState;
            
        }
        
    },
    
    /** The party is healing a wound. */
    CURE() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            currentState.gainResource(ResourceType.HEALTH, CURE_HEALTH_GAIN, false);
            return new GameResponse(getDynamicMessages().randomCureMessage(), getStaticMessage("global.confirm"));
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            currentState.setGameState(ACTION);
            return currentState;
        }
        
    },
    
    /** The party is healing several wounds at once. */
    MASS_CURE() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            currentState.gainResource(ResourceType.HEALTH, MASS_CURE_HEALTH_GAIN, false);
            return new GameResponse(getDynamicMessages().randomMassCureMessage(), getStaticMessage("global.confirm"));
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            currentState.setGameState(ACTION);
            return currentState;
        }
        
    },
    
    /** The party is scouting ahead to see what the next event is, potentially discarding it. */
    SCOUT() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            StringBuilder output = new StringBuilder();
            
            if(!currentState.hasSeenInstructions(SCOUT)) {
                output.append(getStaticMessage("scout.instructions"));
                output.append("\n\n");
                currentState.setSeenInstructions(SCOUT, true);
            }
            
            final Event nextEvent = currentState.upcomingEvent();
            
            output.append(getStaticMessage("scout.message"))
                  .append("\n\n")
                  .append(nextEvent.getName()) //Don't show flavor text, just the stats.
                  .append("\n\n")
                  .append(nextEvent.costListing(currentState))
                  .append("\n\n")
                  .append(getStaticMessage("scout.question"));
            
            return new GameResponse(output.toString(), getStaticMessage("global.yes"), getStaticMessage("global.no"));
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            if(getStaticMessage("global.yes").equalsIgnoreCase(playerText)) {
                currentState.redrawEvent();
                currentState.setGameState(ACTION);
            } else if(getStaticMessage("global.no").equalsIgnoreCase(playerText)) {
                currentState.setGameState(ACTION);
            }
            return currentState;
            
        }
        
    },
    
    /** The party is checking to see what the next monster is, potentially discarding it. */
    SECRET_DOOR() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            StringBuilder output = new StringBuilder();
            
            if(!currentState.hasSeenInstructions(SECRET_DOOR)) {
                output.append(getStaticMessage("secret_door.instructions"));
                output.append("\n\n");
                currentState.setSeenInstructions(SECRET_DOOR, true);
            }
            
            final Monster nextMonster = currentState.upcomingMonster();
            
            output.append(getStaticMessage("secret_door.message"))
                  .append("\n\n")
                  .append(nextMonster.getName()) //Don't show flavor text, just the stats.
                  .append("\n\n")
                  .append(nextMonster.costListing(currentState))
                  .append("\n\n")
                  .append(getStaticMessage("secret_door.question"));
            
            return new GameResponse(output.toString(), getStaticMessage("global.yes"), getStaticMessage("global.no"));
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            if(getStaticMessage("global.yes").equalsIgnoreCase(playerText)) {
                currentState.redrawMonster();
                currentState.setGameState(ACTION);
            } else if(getStaticMessage("global.no").equalsIgnoreCase(playerText)) {
                currentState.setGameState(ACTION);
            }
            return currentState;
            
        }
        
    },
    
    /** The party has run out of Health. */
    OUT_OF_HEALTH() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            String message = getDynamicMessages().randomOutOfHealthMessage();
            
            currentState.spendResource(ResourceType.TIME, TPK_TIME_COST);
            
            //Did we run out of time as a result?
            if(currentState.getResourceCount(ResourceType.TIME) <= 0) {
                
                currentState.setGameState(OUT_OF_TIME);
                message += "\n\n" + getDynamicMessages().randomOutOfTimeMessage();
                return new GameResponse(message, getStaticMessage("game_over.restart"));
                
            } else {
                
                currentState.refillAll();
                return new GameResponse(message, getStaticMessage("out_of_health.continue"));
                
            }

        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            //Regardless of what they actually typed, take them back to an Event.
            currentState.setGameState(EVENT);
            return currentState;
            
        }
        
    },
    
    /** The party has run out of Time. */
    OUT_OF_TIME() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            return new GameResponse(
                getDynamicMessages().randomOutOfTimeMessage(),
                getStaticMessage("game_over.restart")
            );
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            //It doesn't actually matter WHAT they typed; send them to the title screen.
            currentState.setGameState(TITLE);
            return currentState;
        }
        
    },
    
    VICTORY() {
        
        @Override
        public GameResponse enterState(PlayerState currentState) {
            
            String output = getStaticMessage("victory.opening")
                    + "\n\n"
                    + getStaticMessage("victory.heroes")
                    + currentState.listHeroes()
                    + "\n"
                    + getStaticMessage("victory.time")
                    + currentState.getResourceCount(ResourceType.TIME)
                    + "\n\n"
                    + getStaticMessage("victory.thanks");
            
            return new GameResponse(output, getStaticMessage("global.confirm"));
            
        }

        @Override
        public PlayerState update(PlayerState currentState, String playerText) {
            
            //Regardless of what they send, back to the title screen.
            currentState.setGameState(TITLE);
            return currentState;
            
        }
        
    };
    
    /** How much Time it costs to use the Back To Adventure action. */
    private static final int FIGHT_TIME_COST = 3;
    
    /** How much Time it costs to use the Short Rest action. */
    private static final int SHORT_REST_TIME_COST = 5;
    
    /** How much Time it costs to use the Long Rest action. */
    private static final int LONG_REST_TIME_COST = 15;
    
    /** How much Physical it costs to use the Charge Ahead action. */
    private static final int CHARGE_PHYSICAL_COST = 1;
    
    /** How much Time it costs to use the Charge Ahead action. */
    private static final int CHARGE_TIME_COST = 2;
    
    /** How much Arcane it costs to use the Transmute action. */
    private static final int TRANSMUTE_ARCANE_COST = 1;
    
    /** How much Time it costs to use the Arcane action. */
    private static final int TRANSMUTE_TIME_COST = 1;
    
    /** How much Divine it costs to use the Cure action. */
    private static final int CURE_DIVINE_COST = 1;
    
    /** How much Time it costs to use the Cure action. */
    private static final int CURE_TIME_COST = 1;
    
    /** How much Health is gained by the Cure action. */
    private static final int CURE_HEALTH_GAIN = 1;
    
    /** How much Divine it costs to use the Mass Cure action. */
    private static final int MASS_CURE_DIVINE_COST = 3;
    
    /** How much Time it costs to use the Mass Cure action. */
    private static final int MASS_CURE_TIME_COST = 1;
    
    /** How much Health is gained by the Mass Cure action. */
    private static final int MASS_CURE_HEALTH_GAIN = 4;
    
    /** How much Stealth it costs to use the Scout Ahead action. */
    private static final int SCOUT_STEALTH_COST = 1;
    
    /** How much Time it costs to use the Scout Ahead action. */
    private static final int SCOUT_TIME_COST = 1;
    
    /** How much Mechanical it costs to use the Secret Door action. */
    private static final int SECRET_DOOR_MECHANICAL_COST = 1;
    
    /** How much Time it costs to use the Secret Door action. */
    private static final int SECRET_DOOR_TIME_COST = 1;
    
    /** How much Time it costs to use the Seal A Portal action. */
    private static final int BOSS_TIME_COST = 3;
    
    /** How much Physical it costs to use the Dramatic Entry action. */
    private static final int BOSS_CHARGE_PHYSICAL_COST = 1;
    
    /** How much Time it costs to use the Dramatic Entry action. */
    private static final int BOSS_CHARGE_TIME_COST = 2;
    
    /** How much Time the party loses if it wipes. */
    private static final int TPK_TIME_COST = 20;
    
    /**
     * Generates a response to be returned to the user after transitioning INTO this GameState, based on the information
     * encapsulated in the provided PlayerState.
     * @param currentState The state to pull information from when generating the response.
     * @return A GameResponse to be passed up to higher layers of the program.
     */
    public abstract GameResponse enterState(PlayerState currentState);
    
    /**
     * Handles the game logic for this state of the game upon receiving input from the user WHILE in this GameState.
     * Returns the state the game will advance to next, which might be the current state for a looping segment of the
     * game, but must never be {@code null}.
     * @param currentState The state of the current player's game. It may be mutated during game logic handling.
     * @param playerText The text the user has entered to the bot.
     * @return The new representation of the player's state, which may be a new object or the mutated input parameter.
     */
    public abstract PlayerState update(PlayerState currentState, String playerText);
    
    /** Helper method to save having to fully qualify getStaticMessage on every use. */
    private static String getStaticMessage(String identifier) {
        return HeroesOfCordan.getStaticMessage(identifier);
    }
    
    /** Helper method to save having to fully qualify getDynamicMessages on every use. */
    private static DynamicMessages getDynamicMessages() {
        return HeroesOfCordan.getDynamicMessages();
    }
    
    /** Helper method: Given player input intended to interact with an indexed list, try to get an index from it. */
    private static int parseIndex(String playerText) {
        
        //Just need to look at the index
        String indexText;
        int colonIndex = playerText.indexOf(":");
        if(colonIndex >= 0) {
            indexText = playerText.substring(0, colonIndex);
        } else {
            //Try the entire input
            indexText = playerText;
        }

        int index;
        try {
            index = Integer.parseInt(indexText);
        } catch (NumberFormatException nfx) {
            //Invalid input, try again.
            return -1;
        }

        //The players provide 1-indexed indexes, not 0-indexed, so:
        index--;
        
        return index;
                
    }
    
}
