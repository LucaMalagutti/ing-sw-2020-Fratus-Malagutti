package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

/**
 * Defines the mechanics of the God card Hephaestus
 */
public class HephaestusGameMechanics extends GodGameMechanics {
    private Position lastPositionBuilt;     //reference to the last position in which the player has built

    //getters and setters
    public Position getLastPositionBuilt() {
        return lastPositionBuilt;
    }
    public void setLastPositionBuilt(Position lastPositionBuilt) {
        this.lastPositionBuilt = lastPositionBuilt;
    }

    /**
     * Constructor of the class HephaestusGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public HephaestusGameMechanics(GameMechanics component) {
        super(component, "Hephaestus", PathType.DEFAULT);
    }

    /**
     * Ask the player if want to build a second time in the same position (not a dome)
     * @param position position in which has to build
     * @return false if height is 3 (no dome) or the player doesn't want to build a second block
     */
    public boolean doubleBuild(Position position) {
        if(position.getHeight() >= 3)
            return false;
        //To be implemented
        return true;
    }

    @Override
    public void build(Player player, Position futurePosition) {
        getComponent().build(player, futurePosition);
        if(doubleBuild(futurePosition))
            futurePosition.increaseHeight();
    }
}
