package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;

public class InfoRequest extends Request {
    private static final long serialVersionUID = 6230159695297818099L;
    private final static MessageType staticType = MessageType.INFO;

    public InfoRequest(String player, String message) {
        super(player, null, message, staticType);
    }

    @Override
    public Message validateResponse(String stringMessage) {
        return null;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
