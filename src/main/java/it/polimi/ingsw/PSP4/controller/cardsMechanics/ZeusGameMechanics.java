package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the mechanics of the God card Zeus
 */
public class ZeusGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.ZEUS;         //type which represents the God

    /**
     * Constructor of the class ZeusGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public ZeusGameMechanics(GameMechanics component) { super(type, component); }

    /**
     * If the height of its current position is less than 3 can build below the worker
     */
    @Override
    public ArrayList<Position> getBuildPositions(Player player, int callNum) {
        ArrayList<Position> componentValid = getComponent().getBuildPositions(player, callNum);

        //It should never be evil, in such case at least it won't change the behaviour
        if(!isEvil()) {
            Position currPosition = player.getCurrWorker().getCurrPosition();
            if (currPosition.getHeight() < 3)
                componentValid.add(currPosition);
        }

        return componentValid;
    }
}
