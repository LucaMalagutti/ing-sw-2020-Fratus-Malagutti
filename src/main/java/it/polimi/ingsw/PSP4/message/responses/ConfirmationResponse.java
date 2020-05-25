package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

public class ConfirmationResponse extends Response {
    private static final long serialVersionUID = -2034605167045234453L;
    private static final MessageType staticType = MessageType.CONFIRMATION;

    private final SerializablePosition selected;
    private final boolean confirmed;

    /**
     * Constructor of the class ConfirmationResponse
     * @param player username of the sender
     * @param selected position in which the action will occur
     * @param confirmed if the action has been confirmed
     */
    public ConfirmationResponse(String player, SerializablePosition selected, boolean confirmed) {
        super(player, "", staticType);
        this.selected = selected;
        this.confirmed = confirmed;
    }

    @Override
    public void handle() {
        Position position = GameState.getInstance().getPosition(selected.getRow(), selected.getCol());
        GameState.getInstance().getCurrPlayer().getState().setConfirmed(confirmed);
        GameState.getInstance().getCurrPlayer().getState().setOption(position);
    }
}
