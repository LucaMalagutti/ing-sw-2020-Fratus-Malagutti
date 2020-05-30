package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.controller.turnStates.StateType;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

/**
 * Defines the mechanics of the God card Atlas
 */
public class AtlasGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.ATLAS;              //type which represents the God

    /**
     * Constructor of the class AtlasGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public AtlasGameMechanics(GameMechanics component) {
        super(type, component);
    }

    /**
     * Builds a dome or a regular block as chosen by the player
     */
    @Override
    public void build(Player player, Position futurePosition) {
        //It should never be evil, in such case at least it won't change the behaviour
        if(isEvil()) {
            getComponent().build(player, futurePosition);
            return;
        }
        player.lockWorker();

        if(futurePosition.getHeight() < 3 && player.getState().isConfirmed())
            futurePosition.setDome(true);
        else
            futurePosition.increaseHeight();
    }

    /**
     * If not already obvious needs to ask the player to choose between building a dome or a regular block
     */
    @Override
    public String needsConfirmation(Player player, Position futurePosition) {
        //It should never be evil, in such case at least it won't change the behaviour
        if (!isEvil() && player.getState().getType() == StateType.BUILD && futurePosition.getHeight() < 3)
            return Message.ATLAS_BUILD;
        return getComponent().needsConfirmation(player, futurePosition);
    }
}
