package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.controller.turnStates.State;
import it.polimi.ingsw.PSP4.controller.turnStates.StateType;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when waiting
 */
public class WaitState extends State {
    /**
     * Constructor of the class WaitState
     */
    public WaitState() { super(StateType.WAIT); }

    @Override
    public Position selectOption(ArrayList<Position> options) { return null; }

    @Override
    public State performAction(Player player) { return this; }
}
