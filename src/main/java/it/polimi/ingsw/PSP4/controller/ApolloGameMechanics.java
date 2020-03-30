package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/** Defines the mechanics of the God card Apollo
 */
public class ApolloGameMechanics extends GodGameMechanics {
    public ApolloGameMechanics(GameMechanics component){
        super(component);
    }

    /** Allows the player to move in neighboring cells occupied by another player's worker
     */
    @Override
    public ArrayList<Position> getMovePositions(Worker worker, int callNum) {
        if(callNum > 1) {
            return new ArrayList<Position>();   //Empty ArrayList
        }
        ArrayList<Position> componentValid = super.getComponent().getMovePositions(worker, callNum);

        Position currPosition = worker.getCurrPosition();
        ArrayList<Position> lower = currPosition.getReachableHeight();
        ArrayList<Position> occupied = currPosition.getOccupied();
        ArrayList<Position> valid = lower.stream().filter(occupied::contains).collect(Collectors.toCollection(ArrayList::new));

        valid.addAll(componentValid);
        return valid;
    }

}
