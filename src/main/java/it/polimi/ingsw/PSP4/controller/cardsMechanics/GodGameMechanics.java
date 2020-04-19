package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines basic god Card component structure
 */
abstract public class GodGameMechanics extends GameMechanics {
    private GameMechanics component;    //reference to game mechanics that is being decorated

    //getters and setters
    @Override
    public GameMechanics getComponent() { return component; }

    /**
     * Constructor of the class GodGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    protected GodGameMechanics(GameMechanics component) { this.component = component; }

    @Override
    public ArrayList<Position> getMovePositions(Player player, int callNum) {
        return component.getMovePositions(player, callNum);
    }

    @Override
    public ArrayList<Position> getBuildPositions(Player player, int callNum) {
        return component.getBuildPositions(player, callNum);
    }

    @Override
    public boolean checkWinCondition(Player player) {
        return component.checkWinCondition(player);
    }

    @Override
    public void move(Player player, Position futurePosition) {
        component.move(player, futurePosition);
    }

    @Override
    public void build(Player player, Position futurePosition) {
        component.build(player, futurePosition);
    }
}
