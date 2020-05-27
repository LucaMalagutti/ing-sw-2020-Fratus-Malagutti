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

    /**
     * Builds once or twice in the same place as chosen by the player
     */
    @Override
    public void build(Player player, Position futurePosition) {
        getComponent().build(player, futurePosition);

        //It should never be evil, in such case at least it won't change the behaviour
        if(!isEvil() && futurePosition.getHeight() < 3 && player.getState().isConfirmed())
            futurePosition.increaseHeight();
    }

    /**
     * If not already obvious needs to ask the player if wants to build once or twice
     */
    @Override
    public String needsConfirmation(Player player, Position futurePosition) {
        //It should never be evil, in such case at least it won't change the behaviour
        if (!isEvil() && player.getState().getType() == StateType.BUILD && futurePosition.getHeight() < 2)
            return Message.HEPHAESTUS_BUILD;
        return getComponent().needsConfirmation(player, futurePosition);
    }
}
