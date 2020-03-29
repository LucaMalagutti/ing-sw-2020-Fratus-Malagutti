package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DefaultGameMechanics extends GameMechanics {

    @Override
    public ArrayList<Position> move(Worker worker, int callNum) {
        Position currPosition = worker.getCurrPosition();
        ArrayList<Position> lower = currPosition.getLower();
        ArrayList<Position> free = currPosition.getFree();
        return lower.stream().filter(free::contains).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public ArrayList<Position> build(Worker worker, int callNum) {
        return worker.getCurrPosition().getFree();
    }

    @Override
    public boolean checkWinCondition(Worker worker) {
        return worker.getPrevPosition().getHeight() == 2 && worker.getCurrPosition().getHeight() == 3;
    }

}
