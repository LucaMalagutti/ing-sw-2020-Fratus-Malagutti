package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Minotaur
 */
public class MinotaurGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.MINOTAUR;           //type which represents the God

    /**
     * Constructor of the class MinotaurGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public MinotaurGameMechanics(GameMechanics component) {
        super(type, component);
    }

    /**
     * Allows the player to move into a cell occupied by an enemy, if it is possible to push him backwards
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
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
        int[] behindPosition = getBehindEnemyPositionCoordinates(yourPosition, enemyPosition);
        if (behindPosition[0] == -1 && behindPosition[1] == -1) {
            return false;
        }
        ArrayList<Position> enemyNeighborFree = enemyPosition.getFree();
        for (Position enemyNeighbor: enemyNeighborFree) {
            if (enemyNeighbor.getRow() == behindPosition[0] && enemyNeighbor.getCol() == behindPosition[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets coordinates of the position behind the enemy worker. The position has coordinates (-1, -1) if it falls outside of the board
     * @return int array having row and column of behind position as the only two elements
     */
    private int[] getBehindEnemyPositionCoordinates(Position yourPosition, Position enemyPosition) {
        int behindRow = enemyPosition.getRow() + (enemyPosition.getRow() - yourPosition.getRow());
        int behindCol = enemyPosition.getCol() + (enemyPosition.getCol() - yourPosition.getCol());
        if (behindRow < 5 && behindRow >= 0 && behindCol < 5 && behindCol >= 0) {
            return new int[] {behindRow, behindCol};
        }
        else {
            return new int[] {-1, -1};
        }
    }

    /**
     * If futurePosition is not free pushes the enemy worker to the position behind him
     */
    @Override
    public void move(Player player, Position futurePosition) {
        //TODO handle futurePosition null or with dome
        if (futurePosition.getWorker() == null) {
            super.move(player, futurePosition);
        }
        else {
            player.lockWorker();

            Worker currWorker = player.getCurrWorker();
            Worker enemyWorker = futurePosition.getWorker();
            Position currentPosition = currWorker.getCurrPosition();
            Position behindPosition = new Position(-1, -1);
            int[] behindCoordinates = getBehindEnemyPositionCoordinates(currentPosition, futurePosition);
            for (Position x : futurePosition.getFree()) {
                if (x.getRow() == behindCoordinates[0] && x.getCol() == behindCoordinates[1]) {
                    behindPosition = x;
                }
            }
            //This should never happen because behindPosition should always be reassigned in the loop above, since it was
            //previously filtered by getMovePosition as behind and free. Putting (future) exception throw just in case.
            //TODO: handle behindPosition.getCol() or getRow() == -1
            futurePosition.setWorker(currWorker);
            currentPosition.setWorker(null);

            currWorker.setPrevPosition(currentPosition);
            currWorker.setCurrPosition(futurePosition);

            behindPosition.setWorker(enemyWorker);
            //TODO should we add something to indicate that the worker was FORCED to move to this position to avoid checkWinCondition bugs?
            enemyWorker.setPrevPosition(futurePosition);
            enemyWorker.setCurrPosition(behindPosition);
        }
    }
}
