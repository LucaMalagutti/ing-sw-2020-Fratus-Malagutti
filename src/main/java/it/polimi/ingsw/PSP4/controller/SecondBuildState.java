package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when building for the second time after moving
 */
public class SecondBuildState extends State {
    /**
     * Constructor of the class SecondBuildState
     */
    public SecondBuildState() { super(StateType.BUILD); }

    @Override
    public Position selectOption(ArrayList<Position> options) {
        //To be implemented
        return null; //Null if the player wants to skip this state
    }

    @Override
    public State performAction(Player player) {
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 2);
        Position position = selectOption(options);
        if(position != null)    //Player wants to build
            player.getMechanics().build(player, position);
        return new WaitState(); //End of turn
    }
}
