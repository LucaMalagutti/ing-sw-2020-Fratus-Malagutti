package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.ArrayList;

/**
 * Defines the mechanics of the God card Athena
 */
public class AthenaGameMechanics extends GodGameMechanics {
    private final ArrayList<Player> players;

    //getters and setters
    private ArrayList<Player> getPlayers() { return players; }

    /**
     * Constructor of the class AthenaGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    protected AthenaGameMechanics(GameMechanics component, ArrayList<Player> players) {
        super(component, "Athena", PathType.DEFAULT);
        this.players = players;
    }

    /**
     * Wraps each enemy's mechanics with an AthenaEnemyGameMechanics (if not already)
     * @param player current player
     */
    private void wrapEnemies(Player player) {
        for(Player enemy : getPlayers()){
            GameMechanics mechanics = enemy.getMechanics();
            if(enemy != player && !mechanics.getName().equals("Athena_Enemy"))
                enemy.setMechanics(new AthenaEnemyGameMechanics(mechanics));
        }
    }

    /**
     * Unwraps each enemy's mechanics removing the AthenaEnemyGameMechanics (if present)
     * @param player current player
     */
    private void unwrapEnemies(Player player) {
        for(Player enemy : getPlayers()){
            GameMechanics mechanics = enemy.getMechanics();
            if (enemy != player && mechanics.getName().equals("Athena_Enemy"))
                enemy.setMechanics(mechanics.getComponent());
        }
    }

    /**
     * If the current worker moves up decorates enemy's mechanics
     * Else removes the decorator (if present)
     */
    @Override
    public void move(Player player, Position futurePosition) {
        getComponent().move(player, futurePosition);
        Worker currWorker = player.getCurrWorker();
        if(currWorker.getCurrPosition().getHeight() > currWorker.getPrevPosition().getHeight()){
            wrapEnemies(player);
        } else {
            unwrapEnemies(player);
        }
    }
}
