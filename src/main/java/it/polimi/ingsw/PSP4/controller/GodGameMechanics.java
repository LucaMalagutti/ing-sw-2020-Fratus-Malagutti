package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

/** Defines basic god Card component structure
 */
abstract public class GodGameMechanics extends GameMechanics {
    private GameMechanics component;

    public GodGameMechanics(GameMechanics component) {
        this.component = component;
    }

    public GameMechanics getComponent() { return component; }

    @Override
    public ArrayList<Position> getMovePositions(Worker worker, int callNum) {
        return component.getMovePositions(worker, callNum);
    }

    @Override
    public ArrayList<Position> getBuildPositions(Worker worker, int callNum) {
        return component.getBuildPositions(worker, callNum);
    }

    @Override
    public boolean checkWinCondition(Worker worker) {
        return component.checkWinCondition(worker);
    }

}
