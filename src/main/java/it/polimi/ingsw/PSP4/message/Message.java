package it.polimi.ingsw.PSP4.message;

import java.io.Serializable;

/**
 * Message base class
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = -2598699184641521335L;

    private final String player;        //player receiving the message (use all to broadcast)
    private final String message;       //message string to be displayed
    private static MessageType staticType;

    //getter and setter
    public static MessageType getStaticType() { return staticType; }
    public String getPlayer() { return this.player; }
    public String getMessage() { return this.message; }
    public MessageType getType() { return staticType; }

    public Message(String player, String message) {
        this.player = player;
        this.message = message;
    }

    /**
     * @return a CLI-usable text representation of the message, based on its content
     */
    @Override
    public abstract String toString();
}
