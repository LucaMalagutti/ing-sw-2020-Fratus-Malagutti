package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when building before moving
 */
public class EarlyBuildState extends State {
    //Can be skipped, can change worker
    private static final StateType staticType = StateType.BUILD;

    @Override
    public boolean canBeSkipped() { return true; }

    @Override
    public State getNextState() { return new StandardMoveState(getPlayer()); }

    /**
     * Constructor of the class EarlyBuildState
     * @param player reference to current player
     */
    public EarlyBuildState(Player player) { super(player, staticType); }

    @Override
    public void runState() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 0);
        selectOption(options);
    }
}
