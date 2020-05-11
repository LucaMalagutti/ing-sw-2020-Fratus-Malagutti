package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;

public class PingResponse extends Response {
    private static final long serialVersionUID = -5125597734950004916L;
    private final static MessageType staticType = MessageType.PING;

    private final long timestamp;

    public long getTimestamp() { return timestamp; }

    public PingResponse(String player, long timestamp) {
        super(player, "PONG", staticType);
        this.timestamp = timestamp;
    }

    @Override
    public void handle() {
        System.err.println("A pong should never be handled by the controller");
    }
}
