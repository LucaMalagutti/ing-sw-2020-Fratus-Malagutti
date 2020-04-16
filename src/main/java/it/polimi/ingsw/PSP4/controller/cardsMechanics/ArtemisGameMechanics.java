package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.controller.turnStates.PathType;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the mechanics of the God card Artemis
 */
public class ArtemisGameMechanics extends GodGameMechanics {
    /**
     * Constructor of the class ArtemisGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public ArtemisGameMechanics(GameMechanics component) {
        super(component, "Artemis", PathType.DOUBLE_MOVE);
    }

    /**
     * Allows the player to move twice, without allowing a second movement to the starting position
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        ArrayList<Position> componentValid = super.getComponent().getMovePositions(player, callNum);

        if(callNum == 2)
            componentValid.remove(player.getCurrWorker().getPrevPosition()); // Cannot move back
        return componentValid;
    }
}
