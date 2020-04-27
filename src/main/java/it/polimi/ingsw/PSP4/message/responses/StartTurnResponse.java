package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;

public class StartTurnResponse extends Response{
    private static final long serialVersionUID = -9059674143475189296L;
    private static final MessageType staticType = MessageType.START_TURN;

    /**
     * Constructor of the class StartTurnResponse
     * @param player  username of the sender
     */
    public StartTurnResponse(String player) {
        super(player, "", staticType);
    }

    @Override
    public void handle() {
        GameState.getInstance().runTurn();
    }
}
