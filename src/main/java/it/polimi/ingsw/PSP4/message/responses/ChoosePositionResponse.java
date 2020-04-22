package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

/**
 * Message to tell the controller the Position selected by the player
 */
public class ChoosePositionResponse extends Response {
    private static final long serialVersionUID = -6890997551663794038L;
    private static final MessageType staticType = MessageType.CHOOSE_POSITION;

    private final SerializablePosition selected;            //Position chosen by the player

    public SerializablePosition getSelected() { return selected; }

    /**
     * Constructor of the class ChoosePositionResponse
     * @param player username of the sender
     * @param selected Position chosen by the player
     */
    public ChoosePositionResponse(String player, SerializablePosition selected) {
        super(player, "", staticType);
        this.selected = selected;
    }

    @Override
    public void handle() {
        SerializablePosition selected = getSelected();
        Position position = GameState.getInstance().getPosition(selected.getRow(), selected.getCol());
        GameState.getInstance().getCurrPlayer().getState().receiveOption(position);
    }
}
