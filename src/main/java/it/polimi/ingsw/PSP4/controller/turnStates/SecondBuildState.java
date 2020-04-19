package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when building for the second time after moving
 */
public class SecondBuildState extends State {
    /**
     * Constructor of the class SecondBuildState
     * @param player reference to current player
     */
    public SecondBuildState(Player player) { super(player, StateType.BUILD); }

    @Override
    public Position selectOption(ArrayList<Position> options) {
        //To be implemented
        return null; //Null if the player wants to skip this state
    }

    @Override
    public State performAction() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 2);
        if(options.size() != 0) {
            Position position = selectOption(options);
            if (position != null)       //Player wants to build
                player.getMechanics().build(player, position);
        }
        return new WaitState(player);         //End of turn
    }
}
