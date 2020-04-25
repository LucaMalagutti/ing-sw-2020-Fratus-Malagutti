package it.polimi.ingsw.PSP4.message;

public class ErrorMessage extends Message {
    private static final long serialVersionUID = -7758974085042149131L;
    private static final MessageType staticType = MessageType.ERROR;

    /**
     * Constructor of the class ErrorMessage
     * @param player username of the sender
     * @param message text message from the sender
     */
    public ErrorMessage(String player, String message) {
        super(player, message, staticType);
    }
}
