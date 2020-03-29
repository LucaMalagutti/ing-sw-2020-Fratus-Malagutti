package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AthenaGameMechanics extends GodGameMechanics {

    public AthenaGameMechanics(GameMechanics component) {
        super(component);
    }

    @Override
    //Affects opponent
    public ArrayList<Position> move(Worker worker, int callNum) {
        ArrayList<Position> componentValid = super.getComponent().move(worker, callNum);
        return componentValid.stream().filter(position -> position.getHeight() <= worker.getCurrPosition().getHeight()).collect(Collectors.toCollection(ArrayList::new));
    }
}
