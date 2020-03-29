package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

abstract public class GameMechanics {

    abstract public ArrayList<Position> move(Worker worker, int callNum);

    abstract public ArrayList<Position> build(Worker worker, int callNum);

    abstract public boolean checkWinCondition(Worker worker);

}
