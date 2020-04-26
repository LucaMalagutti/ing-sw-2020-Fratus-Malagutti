package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.AssignFirstWorkerPlacementResponse;
import it.polimi.ingsw.PSP4.model.serializable.SerializableGameState;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

public class AssignFirstWorkerPlacementRequest extends Request {
    private static final long serialVersionUID = 9000340880634467516L;
    private static final MessageType staticType = MessageType.FIRST_WORKER_PLACEMENT;

    private final SerializableGameState board;
    private final int numPlayer;
    private final int numWorker;

    public SerializableGameState getBoard() { return board; }
    public int getNumPlayer() { return numPlayer; }
    public int getNumWorker() { return numWorker; }

    public AssignFirstWorkerPlacementRequest(String player, SerializableGameState board, int numPlayer, int numWorker) {
        super(player, Message.FIRST_PLACE_WORKER, staticType);
        this.board = board;
        this.numPlayer = numPlayer;
        this.numWorker = numWorker;
    }

    @Override
    public Message validateResponse(String stringMessage) {
        stringMessage = stringMessage.toUpperCase().replaceAll("\\s","");
        String[] coordinates = stringMessage.split(",");
        int row, col;
        try {
            row = Integer.parseInt(coordinates[0]);
            col = Integer.parseInt(coordinates[1]);
        } catch (NumberFormatException e) {
            return new ErrorMessage(getPlayer(), MessageFormat.format(Message.NOT_VALID_POSITION, stringMessage));
        }
        List<SerializablePosition> selected = board.getFreePositions().stream().filter(p -> p.getRow() == row && p.getCol() == col).collect(Collectors.toList());
        if (selected.size() == 1)
            return new AssignFirstWorkerPlacementResponse(getPlayer(),selected.get(0),numPlayer,numWorker);
        return new ErrorMessage(getPlayer(), MessageFormat.format(Message.NOT_VALID_POSITION, stringMessage));
    }

    @Override
    public String toString() {
        return getBoard().toString() +
            getMessage();
    }
}
