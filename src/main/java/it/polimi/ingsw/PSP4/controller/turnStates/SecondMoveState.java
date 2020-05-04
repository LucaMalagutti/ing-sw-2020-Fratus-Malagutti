package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when moving for the second time
 */
public class SecondMoveState extends State {
    //can be skipped, cannot change worker
    private static final StateType staticType = StateType.MOVE;

    @Override
    public boolean canBeSkipped() { return true; }

    @Override
    public State getNextState() { return new StandardBuildState(getPlayer()); }

    /**
     * Constructor of the class SecondMoveState
     * @param player reference to current player
     */
    public SecondMoveState(Player player) { super(player, staticType); }

    @Override
    public void runState() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getMovePositions(player, 2);
        selectOption(options);
    }
}
