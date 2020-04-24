package it.polimi.ingsw.PSP4.controller.turnStates;

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
    public boolean canChangeWorker() { return !getPlayer().isWorkerLocked(); }

    /**
     * Constructor of the class EarlyBuildState
     * @param player reference to current player
     */
    public EarlyBuildState(Player player) { super(player, staticType); }

    @Override
    public synchronized State performAction() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 0);
        selectOption(options);
        while(!isFinalStep()) {
            try {
                wait();
            } catch (InterruptedException e) {
                //TODO: handle exception
            }
        }
        StateStep step = getStep();
        if(step == StateStep.CHANGE_WORKER)
            return new EarlyBuildState(player);     //CHANGE_WORKER
        if(step == StateStep.PERFORM_ACTION)
            player.getMechanics().build(player, getPosition());
        return new StandardMoveState(player);       //PERFORM_ACTION || SKIP_STATE
    }
}
