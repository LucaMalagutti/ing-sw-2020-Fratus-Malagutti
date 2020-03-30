package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

/** Defines the mechanics of the God card Hephaestus
 */
public class HephaestusGameMechanics extends GodGameMechanics {
    private Position lastPositionBuilt;

    public Position getLastPositionBuilt() {
        return lastPositionBuilt;
    }

    public void setLastPositionBuilt(Position lastPositionBuilt) {
        this.lastPositionBuilt = lastPositionBuilt;
    }

    public HephaestusGameMechanics(GameMechanics component) {
        super(component);
    }

    /** Allows the player to build a second time in the position where he had previously build (not a dome)
     */
    @Override
    public ArrayList<Position> getBuildPositions(Worker worker, int callNum) {
        if (callNum > 2) {
            return new ArrayList<>();
        }
        ArrayList<Position> componentValid = super.getBuildPositions(worker, callNum);

        if (callNum == 2) {
            if (this.getLastPositionBuilt().getHeight() >= 3) {
                return new ArrayList<>();
            }
            else {
                componentValid = new ArrayList<>();
                componentValid.add(this.getLastPositionBuilt());
            }
        }
        return componentValid;
    }
}
