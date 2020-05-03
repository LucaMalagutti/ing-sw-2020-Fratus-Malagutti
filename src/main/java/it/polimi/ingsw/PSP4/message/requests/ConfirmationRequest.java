package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.ConfirmationResponse;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

import java.text.MessageFormat;

/**
 * Message used to confirm an action
 */
public class ConfirmationRequest extends Request {
    private static final long serialVersionUID = -7735099842572405309L;
    private static final MessageType staticType = MessageType.CONFIRMATION;

    private final SerializablePosition position;

    public SerializablePosition getPosition() { return position; }

    /**
     * Constructor of the class ConfirmationRequest
     * @param player username of the receiver
     * @param message text message from the sender
     * @param position position in which the action will occur
     */
    public ConfirmationRequest(String player, String message, SerializablePosition position) {
        super(player, null, message, staticType);
        this.position = position;
    }

    @Override
    public Message validateResponse(String stringMessage) {
        stringMessage = stringMessage.toUpperCase().replaceAll("\\s","");
        if(stringMessage.equals("Y"))
            return new ConfirmationResponse(getPlayer(), getPosition(), true);
        if(stringMessage.equals("N"))
            return new ConfirmationResponse(getPlayer(), getPosition(), false);
        return new ErrorMessage(getPlayer(), Message.NOT_VALID_CONFIRM);
    }

    @Override
    public String toString() {
        String coord = coordinateIntToLetter(position.getRow()) + "," + position.getCol();
        return MessageFormat.format(getMessage(), coord);
    }
}
