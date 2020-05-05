package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.PathType;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when moving for the first time
 */
public class StandardMoveState extends State {
    //Cannot be skipped, can change worker
    private static final StateType staticType = StateType.MOVE;

    /**
     * Constructor of the class StandardMoveState
     * @param player reference to current player
     */
    public StandardMoveState(Player player) { super(player, staticType); }

    @Override
    public void runState() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getMovePositions(player, 1);
        selectOption(options);
    }

    @Override
    public State getNextState() {
        Player player = getPlayer();
        if (player.getMechanics().getPath() == PathType.DOUBLE_MOVE)
            return new SecondMoveState(player);
        return new StandardBuildState(player);
    }
}
