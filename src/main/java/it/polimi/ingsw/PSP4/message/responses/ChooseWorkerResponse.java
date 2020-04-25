package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Worker;

/**
 * Message to tell the controller the selected worker
 */
public class ChooseWorkerResponse extends Response {
    private static final long serialVersionUID = 3151282684531940740L;
    private static final MessageType staticType = MessageType.CHOOSE_WORKER;

    private final int[] worker;

    /**
     * Constructor of the class ChooseWorkerResponse
     * @param player username of the sender
     * @param worker coordinates of the selected worker
     */
    public ChooseWorkerResponse(String player, int[] worker) {
        super(player, "", staticType);
        this.worker = worker;
    }

    @Override
    public void handle() {
        Worker selected = GameState.getInstance().getPosition(worker[0], worker[1]).getWorker();
        GameState.getInstance().receiveWorker(selected);
    }
}
