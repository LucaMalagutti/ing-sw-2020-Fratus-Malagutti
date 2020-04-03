package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the actions to perform when moving for the second time
 */
public class SecondMoveState extends State {
    /**
     * Constructor of the class SecondMoveState
     */
    public SecondMoveState() { super(StateType.MOVE); }

    @Override
    public Position selectOption(ArrayList<Position> options) {
        //To be implemented
        return null; //Null if the player wants to skip this state
    }

    @Override
    public State performAction(Player player) {
        ArrayList<Position> options = player.getMechanics().getMovePositions(player, 2);
        Position position = selectOption(options);
        if(position != null) {  //Player wants to move
            player.getMechanics().move(player, position);
            if(player.getMechanics().checkWinCondition(player)){
                //handle game over : win
            }
        }
        return new StandardBuildState();
    }
}
