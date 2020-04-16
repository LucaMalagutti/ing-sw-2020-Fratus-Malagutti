package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.controller.turnStates.PathType;
import it.polimi.ingsw.PSP4.model.Player;

/**
 * Defines the mechanics of the God card Pan
 */
public class PanGameMechanics extends GodGameMechanics {
    /**
     * Constructor of the class PanGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public PanGameMechanics(GameMechanics component) { super(component, "Pan", PathType.DEFAULT); }

    /**
     * Expands the base winning condition allowing the player to win when moving down two or more layers
     */
    @Override
    public boolean checkWinCondition(Player player) {
        return super.checkWinCondition(player) || (player.getCurrWorker().getCurrPosition().getHeight() - player.getCurrWorker().getPrevPosition().getHeight() >= 2);
    }
}
