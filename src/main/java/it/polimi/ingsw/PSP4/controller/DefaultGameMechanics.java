package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Innermost component of the Decorator for the God card mechanics
 * Defines the behaviours that a user would have if he wasn't assigned any card.
 */
public class DefaultGameMechanics extends GameMechanics {
    //getters and setters
    @Override
    public GameMechanics getComponent() { return null; }

    /**
     * Constructor of the class DefaultGameMechanics
     */
    public DefaultGameMechanics() { super("Default", PathType.DEFAULT); }

    /**
     * Returns ArrayList containing neighboring, reachable and free positions from the selected worker
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        Position currPosition = player.getCurrWorker().getCurrPosition();
        ArrayList<Position> reachable = currPosition.getReachableHeight();
        ArrayList<Position> free = currPosition.getFree();
        return reachable.stream().filter(free::contains).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Returns ArrayList containing free neighboring positions where the player can build
     */
    @Override
    public ArrayList<Position> getBuildPositions(Player player, int callNum) {
        return player.getCurrWorker().getCurrPosition().getFree();
    }

    /**
     * Returns boolean indicating whether the base winning condition has been met by the selected worker
     */
    @Override
    public boolean checkWinCondition(Player player) {
        return player.getCurrWorker().getPrevPosition().getHeight() == 2 && player.getCurrWorker().getCurrPosition().getHeight() == 3;
    }

    @Override
    public void move(Player player, Position futurePosition) {
        if(futurePosition == null){
            //exception
        }
        if(futurePosition.getWorker() != null){
            //exception
        }
        if(futurePosition.hasDome()){
            //exception
        }
        player.lockWorker();

        Worker currWorker = player.getCurrWorker();
        Position currentPosition = currWorker.getCurrPosition();

        futurePosition.setWorker(currWorker);
        currentPosition.setWorker(null);

        currWorker.setPrevPosition(currentPosition);
        currWorker.setCurrPosition(futurePosition);
    }

    @Override
    public void build(Player player, Position futurePosition) {
        if(futurePosition == null){
            //exception
        }
        if(futurePosition.getWorker() != null){
            //exception
        }
        if(futurePosition.hasDome()){
            //exception
        }
        player.lockWorker();
        futurePosition.increaseHeight();
    }
}
