package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Worker;

/** Defines the mechanics of the God card Pan
 */
public class PanGameMechanics extends GodGameMechanics {
    public PanGameMechanics(GameMechanics component) {
        super(component);
    }

    /** Expands the base winning condition allowing the player to win when moving down two or more layers
     */
    @Override
    public boolean checkWinCondition(Worker worker) {
        return super.checkWinCondition(worker) || (worker.getCurrPosition().getHeight() - worker.getPrevPosition().getHeight() >= 2);
    }
}
