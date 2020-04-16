package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.controller.turnStates.PathType;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Base class for the God card Decorator
 */
abstract public class GameMechanics {
    private final String name;          //name of the god or "Default"
    private final PathType path;        //path to follow during a turn, fixed for every God

    //getters and setters
    public String getName() { return name; }

    public PathType getPath() { return path; }

    abstract public GameMechanics getComponent();

    /**
     * Constructor of the class GameMechanics
     * @param path type of path of the actual God
     */
    protected GameMechanics(String name, PathType path) {
        this.name = name;
        this.path = path;
    }

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
}
