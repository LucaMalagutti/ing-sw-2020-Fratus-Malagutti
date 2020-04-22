package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;

/**
 * Request base class
 */
public abstract class Request extends Message {
    private static final long serialVersionUID = 5570259665531932485L;

    /**
     * Constructor of the class Request
     * @param player username of the receiver
     * @param message text message from the sender
     * @param type type of the message
     */
    public Request(String player, String message, MessageType type) { super(player, message, type); }

    /**
     * @return a CLI-usable text representation of the message, based on its content
     */
    @Override
    public abstract String toString();
}
