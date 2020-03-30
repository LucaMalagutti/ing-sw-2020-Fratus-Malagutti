package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/** Defines the mechanics of the God card Athena, when it affects an enemy
 */
public class AthenaEnemyGameMechanics extends GodGameMechanics {
    public AthenaEnemyGameMechanics(GameMechanics component) {
        super(component);
    }

    /** Removes the possibility of moving up
     */
    @Override
    public ArrayList<Position> getMovePositions(Worker worker, int callNum) {
        ArrayList<Position> componentValid = super.getComponent().getMovePositions(worker, callNum);
        return componentValid.stream().filter(position -> position.getHeight() <= worker.getCurrPosition().getHeight()).collect(Collectors.toCollection(ArrayList::new));
    }
}
