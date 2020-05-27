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
        ArrayList<Position> componentValid = getComponent().getMovePositions(player, callNum);

        //It should never be evil, in such case at least it won't change the behaviour
        if(!isEvil() && callNum == 2)
            componentValid.remove(player.getCurrWorker().getPrevPosition()); // Cannot move back
        return componentValid;
    }
}
