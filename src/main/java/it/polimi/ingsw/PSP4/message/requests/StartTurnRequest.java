package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.StartTurnResponse;
import it.polimi.ingsw.PSP4.model.GameState;

public class StartTurnRequest extends Request {
    private static final long serialVersionUID = -6087141863619366410L;
    private static final MessageType staticType = MessageType.START_TURN;

    /**
     * Constructor of the class StartTurnRequest
     * @param player username of the receiver
     */
    public StartTurnRequest(String player) {
        super(player, GameState.getSerializedInstance(), Message.YOUR_TURN, staticType);
    }

    @Override
    public Message validateResponse(String stringMessage) {
        return new StartTurnResponse(getPlayer());
    }

    @Override
    public String toString() { return getMessage(); }
}
