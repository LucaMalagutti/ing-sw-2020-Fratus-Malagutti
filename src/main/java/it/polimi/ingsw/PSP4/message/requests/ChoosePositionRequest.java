package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

import java.util.List;

/**
 * Message to give the player a set of Position to choose from
 */
public class ChoosePositionRequest extends Request {
    private static final long serialVersionUID = 1382167169614565002L;
    private static final MessageType staticType = MessageType.CHOOSE_POSITION;

    private final List<SerializablePosition> options;       //List of Position to choose from

    public List<SerializablePosition> getOptions() { return options; }

    /**
     * Constructor of the class ChoosePositionRequest
     * @param player username of the receiver
     * @param message text message from the sender
     * @param options list of Position to choose from
     */
    public ChoosePositionRequest(String player, String message, List<SerializablePosition> options) {
        super(player, message, staticType);
        this.options = options;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getMessage() + "\n");
        for (SerializablePosition position : options) {
            sb.append(position.getRow()).append(",").append(position.getCol()).append(" ");
        }
        return sb.toString();
    }
}
