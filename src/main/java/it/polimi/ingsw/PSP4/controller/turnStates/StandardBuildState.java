package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.PathType;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when building for the first time after moving
 */
public class StandardBuildState extends State {
    /**
     * Constructor of the class StandardBuildState
     * @param player reference to current player
     */
    public StandardBuildState(Player player) { super(player, StateType.BUILD); }

    @Override
    public Position selectOption(ArrayList<Position> options) {
        //To be implemented
        return null; //Cannot be null
    }

    @Override
    public State performAction() {
        Player player = getPlayer();
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 1);
        if(options.size() == 0) {
            //handle game over : loss
        }
        Position position = selectOption(options);
        player.getMechanics().build(player, position);
        if(player.getMechanics().getPath() == PathType.DOUBLE_BUILD)
            return new SecondBuildState(player);
        return new WaitState(player); // End of turn
    }
}
