package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

/** Defines the mechanics of the God card Demeter
 */
public class DemeterGameMechanics extends GodGameMechanics{
    private Position lastPositionBuilt;

    public Position getLastPositionBuilt() {
        return lastPositionBuilt;
    }

    public void setLastPositionBuilt(Position lastPositionBuilt) {
        this.lastPositionBuilt = lastPositionBuilt;
    }

    public DemeterGameMechanics(GameMechanics component) {
        super(component);
        lastPositionBuilt = null;
    }

    /** Adds possibility to build a second time, except when you have just built
     */
    @Override
    public ArrayList<Position> getBuildPositions(Worker worker, int callNum) {
        if (callNum > 2) {
            return new ArrayList<Position>();
        }
        ArrayList<Position> componentValid = super.getComponent().getBuildPositions(worker, callNum);

        if (callNum == 2) {
            componentValid.remove(this.getLastPositionBuilt());
        }
        return componentValid;
    }
}
