package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when building for the first time after moving
 */
public class StandardBuildState extends State {
    /**
     * Constructor of the class StandardBuildState
     */
    public StandardBuildState() { super(StateType.BUILD); }

    @Override
    public Position selectOption(ArrayList<Position> options) {
        //To be implemented
        return null; //Cannot be null
    }

    @Override
    public State performAction(Player player) {
        ArrayList<Position> options = player.getMechanics().getBuildPositions(player, 1);
        Position position = selectOption(options);
        player.getMechanics().build(player, position);
        if(player.getMechanics().getPath() == PathType.DOUBLE_BUILD)
            return new SecondBuildState();
        return new WaitState(); // End of turn
    }
}
