package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the mechanics of the God card Demeter
 */
public class DemeterGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.DEMETER;            //type which represents the God
    private Position lastPositionBuilt;                             //reference to the last position in which the player has built

    public Position getLastPositionBuilt() {
        return lastPositionBuilt;
    }
    public void setLastPositionBuilt(Position lastPositionBuilt) {
        this.lastPositionBuilt = lastPositionBuilt;
    }

    /**
     * Constructor of the class DemeterGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public DemeterGameMechanics(GameMechanics component) {
        super(type, component);
        lastPositionBuilt = null;
    }

    /**
     * Adds possibility to build a second time, except where you have just built
     */
    @Override
    public ArrayList<Position> getBuildPositions(Player player, int callNum) {
        ArrayList<Position> componentValid = super.getComponent().getBuildPositions(player, callNum);

        //It should never be evil, in such case at least it won't change the behaviour
        if(isEvil())
            return componentValid;

        //resets lastPositionBuilt to null at the beginning of StandardBuiltState
        if (callNum == 1) {
            setLastPositionBuilt(null);
        }
        if (callNum == 2) {
            componentValid.remove(this.getLastPositionBuilt());
        }
        return componentValid;
    }

    /**
     * Sets futurePosition as lastPositionBuilt
     */
    @Override
    public void build(Player player, Position futurePosition) {
        getComponent().build(player, futurePosition);

        //lastPositionBuilt is null at the beginning StandardBuildState
        //It should never be evil, in such case at least it won't change the behaviour
        if (!isEvil() && getLastPositionBuilt() == null) {
            setLastPositionBuilt(futurePosition);
        }
    }
}
