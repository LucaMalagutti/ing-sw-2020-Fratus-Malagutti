package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.PathType;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when building for the first time after moving
 */
public class StandardBuildState extends State {
    //Cannot be skipped, cannot change worker
    private static final StateType staticType = StateType.BUILD;

    /**
     * Constructor of the class StandardBuildState
     * @param player reference to current player
     */
    public StandardBuildState(Player player) { super(player, staticType); }

    @Override
    public void runState() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 1);
        selectOption(options);
    }

    @Override
    public State getNextState() {
        Player player = getPlayer();
        if(player.getMechanics().getPath() == PathType.DOUBLE_BUILD)
            return new SecondBuildState(player);
        return new WaitState(player);
    }
}
