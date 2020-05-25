package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.util.ArrayList;

/**
 * Defines basic god Card component structure
 */
abstract public class GodGameMechanics extends GameMechanics {
    private GameMechanics component;    //reference to game mechanics that is being decorated

    @Override
    public GameMechanics getComponent() { return component; }

    @Override
    public void setComponent(GameMechanics component) { this.component = component; }

    /**
     * Constructor of the class GodGameMechanics
     * @param type reference to the god type
     * @param component reference to the game mechanics to decorate
     */
    protected GodGameMechanics(GodType type, GameMechanics component) {
        super(type);
        this.component = component;
    }

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

    @Override
    public String needsConfirmation(Player player, Position futurePosition) { return component.needsConfirmation(player, futurePosition); }
}
