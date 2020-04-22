package it.polimi.ingsw.PSP4.controller.turnStates;

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

    /**
     * Constructor of the class SecondMoveState
     * @param player reference to current player
     */
    public SecondMoveState(Player player) { super(player, staticType); }

    @Override
    public synchronized void changeWorker() {
        //TODO: signal not possible
    }

    @Override
    public synchronized State performAction() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getMovePositions(player, 2);
        if(options.size() == 0)
            return new StandardBuildState(player);      //No available positions for this state
        selectOption(options);
        while(!isFinalStep()) {
            try {
                wait();
            } catch (InterruptedException e) {
                //TODO: handle exception
            }
        }
        StateStep step = getStep();
        if(step == StateStep.PERFORM_ACTION) {
            player.getMechanics().move(player, getPosition());
            if (player.getMechanics().checkWinCondition(player)) {
                //TODO: handle game over, win
            }
        }
        return new StandardBuildState(player);      //PERFORM_ACTION || SKIP_STATE
        //CHANGE_WORKER not handled cause impossible to get
    }
}
