package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

/** Defines the mechanics of the God card Artemis
 */
public class ArtemisGameMechanics extends GodGameMechanics {
    public ArtemisGameMechanics(GameMechanics component) {
        super(component);
    }

    /** Allows the player to move twice, without allowing a second movement to the starting position
     */
    @Override
    public ArrayList<Position> getMovePositions(Worker worker, int callNum) {
        if(callNum > 2)
            return new ArrayList<Position>(); //Empty Array List

        ArrayList<Position> componentValid = super.getComponent().getMovePositions(worker, callNum);

        if(callNum == 2)
            componentValid.remove(worker.getPrevPosition()); // Cannot move back
        return componentValid;
    }

}
