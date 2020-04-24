package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;

/**
 * Message to ask to skip the current state
 */
public class SkipStateResponse extends Response {
    private static final long serialVersionUID = 376020278839441195L;
    private static final MessageType staticType = MessageType.SKIP_STATE;

    /**
     * Constructor of the class SkipStateResponse
     * @param player username of the sender
     */
    public SkipStateResponse(String player) {
        super(player, "", staticType);
    }

    @Override
    public void handle() {
        //possible as checked in RemoteView.update()
        GameState.getInstance().getCurrPlayer().getState().skipState();
    }
}
