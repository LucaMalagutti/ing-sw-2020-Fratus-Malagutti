package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Minotaur
 */
public class MinotaurGameMechanics extends GodGameMechanics {
    /**
     * Constructor of the class MinotaurGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public MinotaurGameMechanics(GameMechanics component) {
        super(component, "Minotaur", PathType.DEFAULT);
    }

    /**
     * Allows the player to move into a cell occupied by an enemy, if it is possible to push him backwards
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        if (callNum > 1) {
            return new ArrayList<>();
        }
        ArrayList<Position> componentValid = super.getMovePositions(player, callNum);
        Position currPosition = player.getCurrWorker().getCurrPosition();
        ArrayList<Position> lower = currPosition.getReachableHeight();
        ArrayList<Position> occupied  = lower.stream().filter(currPosition.getOccupied(player)::contains).collect(Collectors.toCollection(ArrayList::new));
        for (Position enemyPosition: occupied) {
            if (freeSpaceBehindEnemy(currPosition, enemyPosition)) {
                componentValid.add(enemyPosition);
            }
        }
        return componentValid;
    }

    /**
     * Returns a boolean indicating if the space directly behind the enemy worker, in line with the player's worker, is free
     */
    private boolean freeSpaceBehindEnemy(Position yourPosition, Position enemyPosition) {
        Position behindPosition = getBehindEnemyPosition(yourPosition, enemyPosition);
        if (behindPosition.getRow() == -1 && behindPosition.getCol() == -1) {
            return false;
        }
        ArrayList<Position> enemyNeighborFree = enemyPosition.getFree();
        for (Position enemyNeighbor: enemyNeighborFree) {
            if (enemyNeighbor.getRow() == behindPosition.getRow() && enemyNeighbor.getCol() == behindPosition.getCol()) {
                return true;
            }
        }
        return false;
    }


    //WARNING: Should return the actual reference to the board position
    /**
     * @return Position indicating the coordinates of the cell behind the enemy worker. The position has coordinates (-1, -1) if occupied
     */
    private Position getBehindEnemyPosition(Position yourPosition, Position enemyPosition) {
        int behindRow = enemyPosition.getRow() + (enemyPosition.getRow() - yourPosition.getRow());
        int behindCol = enemyPosition.getCol() + (enemyPosition.getCol() - yourPosition.getCol());
        if (behindRow < 5 && behindRow >= 0 && behindCol < 5 && behindCol >= 0) {
            return new Position(behindRow, behindCol, null);
        }
        else {
            return new Position(-1, -1, null);
        }
    }

    /**
     * If futurePosition is not free punches the worker to the position behind
     */
    @Override
    public void move(Player player, Position futurePosition) {
        if(futurePosition == null){
            //exception
        }
        if(futurePosition.hasDome()){
            //exception
        }
        player.lockWorker();

        Worker currWorker = player.getCurrWorker();
        Worker enemyWorker = futurePosition.getWorker();
        Position currentPosition = currWorker.getCurrPosition();
        Position behindPosition = getBehindEnemyPosition(currentPosition, futurePosition);

        futurePosition.setWorker(currWorker);
        currentPosition.setWorker(null);

        currWorker.setPrevPosition(currentPosition);
        currWorker.setCurrPosition(futurePosition);

        if(enemyWorker != null){
            behindPosition.setWorker(enemyWorker);

            enemyWorker.setPrevPosition(futurePosition);
            enemyWorker.setCurrPosition(behindPosition);
        }
    }
}
