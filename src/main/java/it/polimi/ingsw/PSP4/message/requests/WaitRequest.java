package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;

import java.text.MessageFormat;

/**
 * Message to tell a player to wait the end of the current player's turn
 */
public class WaitRequest extends Request {
    private static final long serialVersionUID = -6625160458601366945L;
    private final static MessageType staticType = MessageType.WAIT;

    public final String playingPlayer;        //username of the current player

    @Override
    public boolean needsResponse() { return false; }

    /**
     * Constructor of the class WaitRequest
     * @param player username of the receiver
     * @param playingPlayer username of the current player
     */
    public WaitRequest(String player, String playingPlayer) {
        super(player, MessageFormat.format(Message.WAIT_END_TURN, playingPlayer), staticType);
        this.playingPlayer = playingPlayer;
    }

    @Override
    public Message validateResponse(String stringMessage) {
        return new ErrorMessage(getPlayer(), Message.NOT_YOUR_TURN);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
