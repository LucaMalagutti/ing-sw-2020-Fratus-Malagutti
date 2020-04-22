package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;

/**
 * Response base class
 */
public abstract class Response extends Message {
    private static final long serialVersionUID = 2371559516164151753L;

    /**
     * Constructor of the class Response
     * @param player username of the sender
     * @param message text message from the sender
     * @param type type of the message
     */
    public Response(String player, String message, MessageType type) { super(player, message, type); }

    /**
     * Handles the message coming from the player
     */
    public abstract void handle();
}
