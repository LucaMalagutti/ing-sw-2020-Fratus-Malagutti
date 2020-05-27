package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Defines the mechanics of the God card Apollo
 */
public class ApolloGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.APOLLO;           //type which represents the God

    /**
     * Constructor of the class ApolloGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public ApolloGameMechanics(GameMechanics component){ super(type, component); }

    /**
     * Allows the player to move in neighboring cells occupied by another player's worker
     */
    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        ArrayList<Position> componentValid = getComponent().getMovePositions(player, callNum);

        //It should never be evil, in such case at least it won't change the behaviour
        if(isEvil())
            return componentValid;

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
        //It should never be evil, in such case at least it won't change the behaviour
        if(isEvil()) {
            getComponent().move(player, futurePosition);
            return;
        }

        player.lockWorker();

        Worker currWorker = player.getCurrWorker();
        Worker enemyWorker = futurePosition.getWorker();
        Position currentPosition = currWorker.getCurrPosition();

        futurePosition.setWorker(currWorker);
        currentPosition.setWorker(enemyWorker);

        currWorker.setPrevPosition(currentPosition);
        currWorker.setCurrPosition(futurePosition);

        if (enemyWorker != null) {
            enemyWorker.setPrevPosition(futurePosition);
            enemyWorker.setCurrPosition(currentPosition);
        }
    }
}
