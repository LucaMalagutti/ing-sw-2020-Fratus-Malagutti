package it.polimi.ingsw.PSP4.controller.cardsMechanics;

import it.polimi.ingsw.PSP4.controller.turnStates.StateType;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;

import java.text.MessageFormat;

/**
 * Defines the mechanics of the God card Atlas
 */
public class AtlasGameMechanics extends GodGameMechanics {
    private static final GodType type = GodType.ATLAS;              //type which represents the God

    //getter and setter
    @Override
    public GodType getType() { return type; }

    /**
     * Constructor of the class AtlasGameMechanics
     * @param component reference to the game mechanics to decorate
     */
    public AtlasGameMechanics(GameMechanics component) {
        super(component);
    }

    @Override
    public void build(Player player, Position futurePosition) {
        if(futurePosition == null){
            //TODO: handle exception
        }
        else if(futurePosition.getWorker() != null){
            //TODO: handle exception
        }
        else if(futurePosition.hasDome()){
            //TODO: handle exception
        }
        else {
            player.lockWorker();

            futurePosition.increaseHeight();
            if(player.getState().isConfirmed())
                futurePosition.setDome(true);
        }
    }

    @Override
    public String needsConfirmation(Player player, Position futurePosition) {
        if (player.getState().getType() == StateType.BUILD && futurePosition.getHeight() < 3)
            return Message.ATLAS_BUILD;
        return null;
    }
}
