package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

/**
 * Defines the mechanics of the God card Hephaestus
 */
public class HephaestusGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.HEPHAESTUS;         //type which represents the God

    //getters and setters
    @Override
    public GodType getType() { return type; }

    /**
     * Constructor of the class HephaestusGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public HephaestusGameMechanics(GameMechanics component) {
        super(component);
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
