package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when building before moving
 */
public class EarlyBuildState extends State {
    private boolean change;     //true if the player wants to change worker

    //getters and setters
    private boolean isWorkerChanged() { return change; }
    private void changeWorker() { this.change = true; }

    /**
     * Constructor of the class EarlyBuildState
     * @param player reference to current player
     */
    public EarlyBuildState(Player player) {
        super(player, StateType.BUILD);
        this.change = false;
    }

    @Override
    public State performAction() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 0);
        if(options.size() != 0) {
            Position position = selectOption(options);
            if (position != null)            //Player wants to build
                player.getMechanics().build(player, position);
            else if (isWorkerChanged())      //Player wants to change worker
                return new EarlyBuildState(player);
        }
        return new StandardMoveState(player); //Player wants to skip this state or has no place to build
    }

    @Override
    public Position selectOption(ArrayList<Position> options) {
//        GameState gameState = GameState.getInstance();
//        gameState.notifyObservers();
//        Position position =
//        return position;

        //To be implemented
        //changeWorker() and return null if the player chooses to go back
        return null; //if the player wants to skip this state
    }

    public void receivePosition(Position position) {

    }
}
