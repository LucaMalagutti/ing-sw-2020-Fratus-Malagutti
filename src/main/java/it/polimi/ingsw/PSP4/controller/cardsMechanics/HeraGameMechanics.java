package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;

/**
 * Defines the mechanics of the God card Hera
 */
public class HeraGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.HERA;             //type which represents the God

    /**
     * Constructor of the class HeraGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public HeraGameMechanics(GameMechanics component) { super(type, component); }

    /**
     * Wraps each enemy at the beginning of the game
     */
    @Override
    public void setupMechanics(Player player) {
        if(!isEvil())
            GameState.getInstance().wrapPlayers(player);
        getComponent().setupMechanics(player);
    }

    /**
     * If it is evil the player cannot win moving on the perimeter
     */
    @Override
    public boolean checkWinCondition(Player player) {
        if(isEvil() && player.getCurrWorker().getCurrPosition().isPerimeter())
            return false;
        return getComponent().checkWinCondition(player);
    }
}
