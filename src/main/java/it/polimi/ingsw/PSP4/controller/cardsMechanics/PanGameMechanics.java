package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;

/**
 * Defines the mechanics of the God card Pan
 */
public class PanGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.PAN;                //type which represents the God

    /**
     * Constructor of the class PanGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public PanGameMechanics(GameMechanics component) { super(type, component); }

    /**
     * Expands the base winning condition allowing the player to win when moving down two or more layers
     */
    @Override
    public boolean checkWinCondition(Player player) {
        return super.checkWinCondition(player) || (player.getCurrWorker().getPrevPosition().getHeight() - player.getCurrWorker().getCurrPosition().getHeight() >= 2);
    }
}
