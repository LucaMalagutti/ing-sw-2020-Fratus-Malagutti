package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.PathType;
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
    public State getNextState() {
        Player player = getPlayer();
        PathType path = player.getMechanics().getPath();
        if(path == PathType.INFINITE_MOVE)
            return new SecondMoveState(player);
        return new StandardBuildState(player);
    }

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
