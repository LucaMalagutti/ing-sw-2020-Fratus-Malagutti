package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when building for the second time after moving
 */
public class SecondBuildState extends State {
    //Can be skipped, cannot change worker
    private static final StateType staticType = StateType.BUILD;

    @Override
    public boolean canBeSkipped() { return true; }

    /**
     * Constructor of the class SecondBuildState
     * @param player reference to current player
     */
    public SecondBuildState(Player player) { super(player, staticType); }

    @Override
    public synchronized void changeWorker() {
        //not possible as checked in RemoteView.update()
    }

    @Override
    public synchronized State performAction() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 2);
        selectOption(options);
        while(!isFinalStep()) {
            try {
                wait();
            } catch (InterruptedException e) {
                //TODO: handle exception
            }
        }
        StateStep step = getStep();
        if(step == StateStep.PERFORM_ACTION)
            player.getMechanics().build(player, getPosition());
        return new WaitState(player);               //PERFORM_ACTION || SKIP_STATE
        //CHANGE_WORKER not handled cause impossible to get
    }
}
