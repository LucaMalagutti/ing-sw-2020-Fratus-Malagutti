package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Apollo
 */
public class ApolloGameMechanics extends GodGameMechanics {
    /**
     * Constructor of the class ApolloGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public ApolloGameMechanics(GameMechanics component){ super(component, "Apollo", PathType.DEFAULT); }

    /**
     * Allows the player to move in neighboring cells occupied by another player's worker
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        if(callNum > 1) {
            return new ArrayList<>();   //Empty ArrayList
        }
        ArrayList<Position> componentValid = super.getComponent().getMovePositions(player, callNum);

        Position currPosition = player.getCurrWorker().getCurrPosition();
        ArrayList<Position> reachable = currPosition.getReachableHeight();
        ArrayList<Position> occupied = currPosition.getOccupied(player);
        ArrayList<Position> valid = reachable.stream().filter(occupied::contains).collect(Collectors.toCollection(ArrayList::new));

        valid.addAll(componentValid);
        return valid;
    }

    /**
     * If futurePosition is not free swaps the worker with player's current worker
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

        futurePosition.setWorker(currWorker);
        currentPosition.setWorker(enemyWorker);

        currWorker.setPrevPosition(currentPosition);
        currWorker.setCurrPosition(futurePosition);

        if(enemyWorker != null){
            enemyWorker.setPrevPosition(futurePosition);
            enemyWorker.setCurrPosition(currentPosition);
        }
    }
}
