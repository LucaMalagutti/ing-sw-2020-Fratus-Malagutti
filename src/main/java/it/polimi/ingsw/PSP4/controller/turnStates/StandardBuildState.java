package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.PathType;
import it.polimi.ingsw.PSP4.message.Message;
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
    public synchronized void changeWorker() {
        //not possible as checked in RemoteView.update()
    }

    @Override
    public synchronized void skipState() {
        //not possible as checked in RemoteView.update()
    }

    @Override
    public synchronized State performAction() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 1);
        if(options.size() == 0) {
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
        player.getMechanics().build(player, getPosition());
        if(player.getMechanics().getPath() == PathType.DOUBLE_BUILD)
            return new SecondBuildState(player);    //PERFORM_ACTION
        return new WaitState(player);               //PERFORM_ACTION
        //CHANGE_WORKER and SKIP_STATE not handled cause impossible to get
    }
}
