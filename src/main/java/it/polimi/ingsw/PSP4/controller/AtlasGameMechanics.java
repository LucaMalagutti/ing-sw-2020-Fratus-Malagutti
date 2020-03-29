package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

public class AtlasGameMechanics extends GodGameMechanics {

    public AtlasGameMechanics(GameMechanics component) {
        super(component);
    }

    @Override
    public ArrayList<Position> build(Worker worker, int callNum) {
        return super.build(worker, callNum);
    }
}
