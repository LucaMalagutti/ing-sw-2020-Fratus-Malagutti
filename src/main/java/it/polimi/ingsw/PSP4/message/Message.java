package it.polimi.ingsw.PSP4.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private static final long serialVersionUID = -2598699184641521335L;

    private final String player;
    private final String message;
    private final MessageType type;

    //getter and setter
    public String getPlayer() { return this.player; }
    public String getMessage() { return this.message; }
    public MessageType getType() { return this.type; }

    public Message(String player, String message, MessageType type) {
        this.player = player;
        this.message = message;
        this.type = type;
    }
}
