package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when waiting
 */
public class WaitState extends State {
    public static final StateType staticType = StateType.WAIT;

    /**
     * Constructor of the class WaitState
     */
    public WaitState(Player player) { super(player, staticType); }

    @Override
    public void runState() {
        performAction();
    }

    @Override
    public State getNextState() {
        return this;
    }

    @Override
    public void receiveOption(Position position) {
        //TODO: signal not possible
    }
}
