package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.PingResponse;
import it.polimi.ingsw.PSP4.model.serializable.SerializableGameState;

public class PingRequest extends Request {
    private static final long serialVersionUID = -8340420256470011983L;
    private final static MessageType staticType = MessageType.PING;
    private final long timestamp;

    public long getTimestamp() {return timestamp; }

    public PingRequest(String player, SerializableGameState board, long timeStamp) {
        super(player, board, "Ping", staticType);
        this.timestamp = timeStamp;
    }


    @Override
    public Message validateResponse(String stringMessage) {
        if (stringMessage.toUpperCase().equals("PONG")) {
            return new PingResponse(getPlayer(), timestamp);
        }
        return new ErrorMessage(getPlayer(), "Invalid PONG");
    }

    @Override
    public String toString() {
        return "Ping timestamp is: " + timestamp;
    }
}
