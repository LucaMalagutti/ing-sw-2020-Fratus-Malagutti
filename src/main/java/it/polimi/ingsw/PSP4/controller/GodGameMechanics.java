package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

abstract public class GodGameMechanics extends GameMechanics {
    private GameMechanics component;

    public GodGameMechanics(GameMechanics component) {
        this.component = component;
    }

    public GameMechanics getComponent() { return component; }

    @Override
    public ArrayList<Position> move(Worker worker, int callNum) {
        return component.move(worker, callNum);
    }

    @Override
    public ArrayList<Position> build(Worker worker, int callNum) {
        return component.build(worker, callNum);
    }

    @Override
    public boolean checkWinCondition(Worker worker) {
        return component.checkWinCondition(worker);
    }

}
