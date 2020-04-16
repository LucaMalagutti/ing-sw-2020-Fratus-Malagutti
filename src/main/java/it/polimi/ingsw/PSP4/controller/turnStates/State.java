package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Basic class for the state of the turn
 */
abstract public class State {
    private final StateType type;       //type of the actual state

    //getters and setters
    public StateType getType() { return type; }

    /**
     * Constructor of the class State
     * @param type type of the actual state
     */
    protected State(StateType type) { this.type = type; }

    /** Gives the player a set of positions based on the card and the current state
     * @param options ArrayList of positions in which the action defined by the current state can be performed
     * @return the position selected by the player
     */
    abstract public Position selectOption(ArrayList<Position> options);

    /** Performs the action defined by the current state
     * @param player selected worker for the action
     * @return next State of the game based on the card path
     */
    abstract public State performAction(Player player);
}
