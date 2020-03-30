package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

/** Defines the mechanics of the God card Atlas
 */
public class AtlasGameMechanics extends GodGameMechanics {
    public AtlasGameMechanics(GameMechanics component) {
        super(component);
    }

    /** WILL also permit the construction of a dome
     */
    @Override
    public ArrayList<Position> getBuildPositions(Worker worker, int callNum) {
        return super.getBuildPositions(worker, callNum);
    }
}
