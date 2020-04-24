package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.PathType;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when moving for the first time
 */
public class StandardMoveState extends State {
    //Cannot be skipped, can change worker
    private static final StateType staticType = StateType.MOVE;

    @Override
    public boolean canChangeWorker() { return !getPlayer().isWorkerLocked(); }

    /**
     * Constructor of the class StandardMoveState
     * @param player reference to current player
     */
    public StandardMoveState(Player player) { super(player, staticType); }

    @Override
    public synchronized void skipState() {
        //not possible as checked in RemoteView.update()
    }

    @Override
    public synchronized State performAction() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getMovePositions(player, 1);
        if(options.size() == 0 && !canChangeWorker()) {
            GameState.getInstance().playerDefeat(player, Message.NO_OPTIONS);
            return new WaitState(player);
        }
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
            return new StandardMoveState(player);   //CHANGE_WORKER
        player.getMechanics().move(player, getPosition());
        if(player.getMechanics().checkWinCondition(player)) {
            GameState.getInstance().playerVictory(player);
            return new WaitState(player);
        }
        if(player.getMechanics().getPath() == PathType.DOUBLE_MOVE)
            return new SecondMoveState(player);     //PERFORM_ACTION
        return new StandardBuildState(player);      //PERFORM_ACTION
        //SKIP_STATE not handled cause impossible to get
    }
}
