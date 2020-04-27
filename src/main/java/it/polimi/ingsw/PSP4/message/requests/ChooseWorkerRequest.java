package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.ChooseWorkerResponse;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.serializable.SerializableGameState;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Message to ask the player to choose a worker
 */
public class ChooseWorkerRequest extends Request {
    private static final long serialVersionUID = -7977688852458411208L;
    private static final MessageType staticType = MessageType.CHOOSE_WORKER;

    private final SerializableGameState board;
    private final List<int[]> workers;

    public SerializableGameState getBoard() { return board; }
    public List<int[]> getWorkers() { return workers; }

    /**
     * Constructor of the class ChooseWorkerRequest
     * @param player username of the receiver
     * @param workers list of the coordinates of each worker
     */
    public ChooseWorkerRequest(String player, List<int[]> workers) {
        super(player, null, Message.CHOOSE_WORKER, staticType);
        this.board = GameState.getSerializedInstance();
        this.workers = workers;
    }

    @Override
    public Message validateResponse(String stringMessage) {
        stringMessage = stringMessage.toUpperCase().replaceAll("\\s","");
        String[] coordinates = stringMessage.split(",");
        int[] worker = new int[2];
        try {
            worker[0] = Integer.parseInt(coordinates[0]);
            worker[1] = Integer.parseInt(coordinates[1]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return new ErrorMessage(getPlayer(), MessageFormat.format(Message.NOT_VALID_WORKER, stringMessage));
        }
        List<int[]> selected = getWorkers().stream().filter(w -> w[0] == worker[0] && w[1] == worker[1]).collect(Collectors.toList());
        if (selected.size() == 1)
            return new ChooseWorkerResponse(getPlayer(), selected.get(0));
        return new ErrorMessage(getPlayer(), MessageFormat.format(Message.NOT_VALID_WORKER, stringMessage));
    }

    @Override
    public String toString() {
        SerializableGameState board = getBoard();
        StringBuilder sb = new StringBuilder();
        if(board != null)
            sb.append(board.toString());
        sb.append(getMessage()).append("\n");
        for (int[] worker : workers) {
            sb.append(worker[0]).append(",").append(worker[1]).append(" ");
        }
        return sb.toString();
    }
}
