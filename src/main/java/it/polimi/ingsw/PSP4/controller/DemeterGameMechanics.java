package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines the mechanics of the God card Demeter
 */
public class DemeterGameMechanics extends GodGameMechanics{
    private Position lastPositionBuilt;     //reference to the last position in which the player has built

    //getters and setters
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
        super(component, "Demeter", PathType.DOUBLE_BUILD);
        lastPositionBuilt = null;
    }

    /**
     * Adds possibility to build a second time, except where you have just built
     */
    @Override
    public ArrayList<Position> getBuildPositions(Player player, int callNum) {
        ArrayList<Position> componentValid = super.getComponent().getBuildPositions(player, callNum);

        //resets lastPositionBuilt to null at the beginning of StandardBuiltState
        if (callNum == 1) {
            setLastPositionBuilt(null);
        }
        if (callNum == 2) {
            componentValid.remove(this.getLastPositionBuilt());
        }
        return componentValid;
    }

    @Override
    public void build(Player player, Position futurePosition) {
        super.build(player, futurePosition);

        //lastPositionBuilt is null at the beginning StandardBuildState
        if (getLastPositionBuilt() == null) {
            setLastPositionBuilt(futurePosition);
        }
    }
}
