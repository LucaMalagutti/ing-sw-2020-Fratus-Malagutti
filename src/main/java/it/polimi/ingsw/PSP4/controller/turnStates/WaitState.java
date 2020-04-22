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
     */
    public WaitState(Player player) { super(player, staticType); }

    @Override
    public synchronized void receiveOption(Position position) {
        //TODO: signal not possible
    }

    @Override
    public synchronized void changeWorker() {
        //TODO: signal not possible
    }

    @Override
    public synchronized void skipState() {
        //TODO: signal not possible
    }

    @Override
    public State performAction() { return this; }
}
