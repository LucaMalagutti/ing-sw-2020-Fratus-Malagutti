package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

public class ArtemisGameMechanics extends GodGameMechanics {

    public ArtemisGameMechanics(GameMechanics component) {
        super(component);
    }

    @Override
    public ArrayList<Position> move(Worker worker, int callNum) {
        if(callNum > 2)
            return new ArrayList<Position>(); //Empty Array List

        ArrayList<Position> componentValid = super.getComponent().move(worker, callNum);

        if(callNum == 2)
            componentValid.remove(worker.getPrevPosition()); // Cannot move back
        return componentValid;
    }

}
