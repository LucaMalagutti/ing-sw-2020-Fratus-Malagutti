package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/** Innermost component of the Decorator for the God card mechanics
 *  Defines the behaviours that a user would have if he wasn't assigned any card.
 */
public class DefaultGameMechanics extends GameMechanics {
    /** Returns ArrayList containing neighboring, reachable and free positions from the selected worker
     */
    @Override
    public ArrayList<Position> getMovePositions(Worker worker, int callNum) {
        Position currPosition = worker.getCurrPosition();
        ArrayList<Position> lower = currPosition.getReachableHeight();
        ArrayList<Position> free = currPosition.getFree();
        return lower.stream().filter(free::contains).collect(Collectors.toCollection(ArrayList::new));
    }

    /** Returns ArrayList containing free neighboring positions where the player can build
     */
    @Override
    public ArrayList<Position> getBuildPositions(Worker worker, int callNum) {
        return worker.getCurrPosition().getFree();
    }

    /** Returns boolean indicating whether the base winning condition has been met by the selected worker
     */
    @Override
    public boolean checkWinCondition(Worker worker) {
        return worker.getPrevPosition().getHeight() == 2 && worker.getCurrPosition().getHeight() == 3;
    }

}
