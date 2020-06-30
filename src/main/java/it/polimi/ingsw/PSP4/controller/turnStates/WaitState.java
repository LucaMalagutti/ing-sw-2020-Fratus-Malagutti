package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

/**
 * Defines the actions to perform when waiting
 */
public class WaitState extends State {
    public static final StateType staticType = StateType.WAIT;

    /**
     * Constructor of the class WaitState
     * @param player reference to current player
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
    public void receiveOption(Position position) { }
}
