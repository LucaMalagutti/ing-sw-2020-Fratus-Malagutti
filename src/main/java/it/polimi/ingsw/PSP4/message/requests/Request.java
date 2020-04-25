package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.Response;

/**
 * Request base class
 */
public abstract class Request extends Message {
    private static final long serialVersionUID = 5570259665531932485L;

    /**
     * @return true if the Request must receive a Response
     */
    public boolean needsResponse() { return true; }

    /**
     * Constructor of the class Request
     * @param player username of the receiver
     * @param message text message from the sender
     * @param type type of the message
     */
    public Request(String player, String message, MessageType type) { super(player, message, type); }

    /**
     * Validates the string as a response to the request
     * @param stringMessage message to validate
     * @return stringMessage parsed as a Response (if stringMessage is a valid response) or an ErrorMessage
     */
    public abstract Message validateResponse(String stringMessage);

    /**
     * @return a CLI-usable text representation of the message, based on its content
     */
    @Override
    public abstract String toString();
}
