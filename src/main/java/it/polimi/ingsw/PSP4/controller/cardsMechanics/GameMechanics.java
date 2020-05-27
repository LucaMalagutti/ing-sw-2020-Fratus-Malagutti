package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for the God card Decorator
 */
abstract public class GameMechanics {
    private final GodType type;
    private boolean evil = false;           //If true the GameMechanics is wrapping an enemy so it can have different behaviours

    public boolean isEvil() { return evil; }
    public void setEvil() { this.evil = true; }

    public GodType getRealType() { return type; }
    public GodType getType() {
        if(isEvil())
            return getComponent().getType();
        return type;
    }
    public PathType getPath() {
        if(isEvil())
            return getComponent().getPath();
        return type.getPathType();
    }

    abstract public GameMechanics getComponent();
    abstract public void setComponent(GameMechanics component);

    /**
     * Constructor of the class GameMechanics
     * @param type reference to the god type
     */
    public GameMechanics(GodType type) {
        this.type = type;
    }

    /**
     * @return a list of evil GodType wrapping the innermost component
     */
    public List<GodType> getEvilList() {
        List<GodType> evilList = getComponent().getEvilList();
        if(isEvil())
            evilList.add(type);
        return evilList;
    }

    /**
     * Setup operations to be performed at the beginning of the game
     * @param player current player
     */
    abstract public void setupMechanics(Player player);

    /**
     * Modifies available movement positions based on the card effect
     * @param player current player
     * @param callNum number of movements during this turn
     * @return ArrayList containing positions where this card allows the player to move the selected worker
     */
    abstract public ArrayList<Position> getMovePositions(Player player, int callNum);

    /**
     * Modifies available building position based on the card effect
     * @param player current player
     * @param callNum number of builds during this turn
     * @return ArrayList containing positions where this card allows the player to move the selected worker
     */
    abstract public ArrayList<Position> getBuildPositions(Player player, int callNum);

    /**
     * Adds a card's special winning condition to the basic one
     * @param player current player
     * @return boolean indicating whether the game has been won
     */
    abstract public boolean checkWinCondition(Player player);

    /**
     * Moves the current worker to the position passed as a parameter
     * @param player current player
     * @param futurePosition position in which move the player's current worker
     */
    abstract public void move(Player player, Position futurePosition);

    /**
     * Builds inside the position passed as a parameter
     * @param player current player
     * @param futurePosition position in which build
     */
    abstract public void build(Player player, Position futurePosition);

    /**
     * Check if the action to perform needs a confirmation from the player
     * @param player current player
     * @param futurePosition position to check
     * @return a confirmation message or null if not required
     */
    abstract public String needsConfirmation(Player player, Position futurePosition);
}
