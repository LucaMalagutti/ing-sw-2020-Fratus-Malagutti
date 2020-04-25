package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.ChangeWorkerResponse;
import it.polimi.ingsw.PSP4.message.responses.ChoosePositionResponse;
import it.polimi.ingsw.PSP4.message.responses.SkipStateResponse;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Message to give the player a set of Position to choose from
 */
public class ChoosePositionRequest extends Request {
    private static final long serialVersionUID = 1382167169614565002L;
    private static final MessageType staticType = MessageType.CHOOSE_POSITION;

    private final List<SerializablePosition> options;       //List of Position to choose from
    private final boolean canBeSkipped;                     //Defines if the player can skip current state
    private final boolean canChangeWorker;                  //Defines if the player can change the worker

    public List<SerializablePosition> getOptions() { return options; }

    /**
     * Constructor of the class ChoosePositionRequest
     * @param player username of the receiver
     * @param message text message from the sender
     * @param options list of Position to choose from
     * @param canBeSkipped defines if the player can skip current state
     * @param canChangeWorker defines if the player can change the worker
     */
    public ChoosePositionRequest(String player, String message, List<SerializablePosition> options, boolean canBeSkipped, boolean canChangeWorker) {
        super(player, message, staticType);
        this.options = options;
        this.canBeSkipped = canBeSkipped;
        this.canChangeWorker = canChangeWorker;
    }

    @Override
    public Message validateResponse(String stringMessage) {
        stringMessage = stringMessage.toUpperCase().replaceAll("\\s","");
        if(stringMessage.equals("SKIP") && canBeSkipped)
            return new SkipStateResponse(getPlayer());
        if(stringMessage.equals("CHANGE") && canChangeWorker)
            return new ChangeWorkerResponse(getPlayer());
        String[] coordinates = stringMessage.split(",");
        int row, col;
        try {
            row = Integer.parseInt(coordinates[0]);
            col = Integer.parseInt(coordinates[1]);
        } catch (NumberFormatException e) {
            return new ErrorMessage(getPlayer(), MessageFormat.format(Message.NOT_VALID_POSITION, stringMessage));
        }
        List<SerializablePosition> selected = getOptions().stream().filter(p -> p.getRow() == row && p.getCol() == col).collect(Collectors.toList());
        if(selected.size() == 1)
            return new ChoosePositionResponse(getPlayer(), selected.get(0));
        return new ErrorMessage(getPlayer(), MessageFormat.format(Message.NOT_VALID_POSITION, stringMessage));
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
