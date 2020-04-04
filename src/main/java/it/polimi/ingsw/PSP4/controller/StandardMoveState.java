package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when moving for the first time
 */
public class StandardMoveState extends State {
    private boolean change;     //true if the player wants to change worker

    //getters and setters
    private boolean isWorkerChanged() { return change; }
    private void changeWorker() { this.change = true; }

    /**
     * Constructor of the class StandardMoveState
     */
    public StandardMoveState() {
        super(StateType.MOVE);
        this.change = false;
    }

    @Override
    public Position selectOption(ArrayList<Position> options) {
        //To be implemented
        //changeWorker() and return null if the player chooses to go back
        return null; //null only if the player wants to change worker
    }

    @Override
    public State performAction(Player player) {
        ArrayList<Position> options = player.getMechanics().getMovePositions(player, 1);
        Position position = selectOption(options);
        if(position == null && isWorkerChanged())              //Player wants to change worker
            return new StandardMoveState();
        player.getMechanics().move(player, position);   //Player wants to build
        if(player.getMechanics().checkWinCondition(player)) {
            //handle game over : win
        }
        if(player.getMechanics().getPath() == PathType.DOUBLE_MOVE)
            return new SecondMoveState();
        return new StandardBuildState();
    }
}
