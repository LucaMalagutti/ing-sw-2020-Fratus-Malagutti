package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when building before moving
 */
public class EarlyBuildState extends State {
    private boolean change;     //true if the player wants to change worker

    //getters and setters
    private boolean isChange() { return change; }
    private void changeWorker() { this.change = true; }

    /**
     * Constructor of the class EarlyBuildState
     */
    public EarlyBuildState() {
        super(StateType.BUILD);
        this.change = false;
    }

    @Override
    public Position selectOption(ArrayList<Position> options) {
        //To be implemented
        //changeWorker() and return null if the player chooses to go back
        return null; //if the player wants to skip this state
    }

    @Override
    public State performAction(Player player) {
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 0);
        Position position = selectOption(options);
        if(position != null)            //Player wants to build
            player.getMechanics().build(player, position);
        else if(isChange())             //Player wants to change worker
            return new EarlyBuildState();
        return new StandardMoveState(); //Player wants to skip this state
    }
}
