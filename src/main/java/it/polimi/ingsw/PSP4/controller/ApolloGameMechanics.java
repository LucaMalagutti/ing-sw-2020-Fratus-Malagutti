package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ApolloGameMechanics extends GodGameMechanics {

    public ApolloGameMechanics(GameMechanics component){
        super(component);
    }

    @Override
    public ArrayList<Position> move(Worker worker, int callNum) {
        if(callNum > 1)
            return new ArrayList<Position>();   //Empty ArrayList

        ArrayList<Position> componentValid = super.getComponent().move(worker, callNum);

        Position currPosition = worker.getCurrPosition();
        ArrayList<Position> lower = currPosition.getLower();
        ArrayList<Position> occupied = currPosition.getOccupied();
        ArrayList<Position> valid = lower.stream().filter(occupied::contains).collect(Collectors.toCollection(ArrayList::new));

        valid.addAll(componentValid);
        return valid;
    }

}
