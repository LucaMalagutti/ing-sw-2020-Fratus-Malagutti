package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

public class AssignFirstWorkerPlacementResponse extends Response {
    private static final long serialVersionUID = -5375163783914853986L;
    private static final MessageType staticType = MessageType.FIRST_WORKER_PLACEMENT;

    private final SerializablePosition selected;            //Position chosen by the player
    private final int numPlayer;
    private final int numWorker;

    public int getNumPlayer() { return numPlayer; }
    public int getNumWorker() { return numWorker; }

    public SerializablePosition getSelected() { return selected; }

    /**
     * Constructor of the class ChoosePositionResponse
     * @param player username of the sender
     * @param selected Position chosen by the player
     */
    public AssignFirstWorkerPlacementResponse(String player, SerializablePosition selected, int numPlayer, int numWorker) {
        super(player, "", staticType);
        this.selected = selected;
        this.numPlayer = numPlayer;
        this.numWorker = numWorker;
    }

    @Override
    public void handle() {
        Position selectedPosition = GameState.getInstance().getPosition(selected.getRow(), selected.getCol());
        Worker selectedWorker = GameState.getInstance().getCurrPlayer().getWorkers().get(numWorker);
        selectedWorker.setCurrPosition(selectedPosition);
        selectedPosition.setWorker(selectedWorker);
        if(numPlayer + 1 == GameState.getInstance().getNumPlayer() && numWorker == 1)
            GameState.getInstance().newTurn();
        else if(numWorker == 1)
            GameState.getInstance().firstWorkerPlacement(numPlayer + 1, 0);
        else
            GameState.getInstance().firstWorkerPlacement(numPlayer, 1);
    }
}
