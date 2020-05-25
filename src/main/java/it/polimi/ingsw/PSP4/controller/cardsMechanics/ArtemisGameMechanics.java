package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the mechanics of the God card Artemis
 */
public class ArtemisGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.ARTEMIS;            //type which represents the God

    /**
     * Constructor of the class ArtemisGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public ArtemisGameMechanics(GameMechanics component) {
        super(type, component);
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
