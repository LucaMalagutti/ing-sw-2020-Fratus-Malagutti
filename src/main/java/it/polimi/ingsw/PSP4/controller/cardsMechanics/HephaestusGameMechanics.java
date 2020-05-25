package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.controller.turnStates.StateType;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.text.MessageFormat;

/**
 * Defines the mechanics of the God card Hephaestus
 */
public class HephaestusGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.HEPHAESTUS;         //type which represents the God

    /**
     * Constructor of the class HephaestusGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public HephaestusGameMechanics(GameMechanics component) {
        super(type, component);
    }

    @Override
    public void build(Player player, Position futurePosition) {
        getComponent().build(player, futurePosition);
        if(futurePosition.getHeight() < 3 && player.getState().isConfirmed())
            futurePosition.increaseHeight();
    }

    @Override
    public String needsConfirmation(Player player, Position futurePosition) {
        if (player.getState().getType() == StateType.BUILD && futurePosition.getHeight() < 2)
            return Message.HEPHAESTUS_BUILD;
        return getComponent().needsConfirmation(player, futurePosition);
    }
}
