package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.ChooseStartingPlayerResponse;

import java.text.MessageFormat;
import java.util.List;

/**
 * Message to ask the first player connected to choose who starts the game
 */
public class ChooseStartingPlayerRequest extends Request {
    private static final long serialVersionUID = -5015794011565067400L;
    private final static MessageType staticType = MessageType.CHOOSE_STARTING_PLAYER;

    private final List<String> playerNames;           //List of usernames

    public List<String> getPlayerNames() { return playerNames; }

    /**
     * Constructor of the class ChooseStartingPlayerRequest
     * @param player username of the receiver
     * @param playerNames list of usernames
     */
    public ChooseStartingPlayerRequest(String player, List<String> playerNames) {
        super(player, Message.CHOOSE_STARTING_PLAYER, staticType);
        this.playerNames = playerNames;
    }

    @Override
    public Message validateResponse(String stringMessage) {
        stringMessage = stringMessage.trim();
        if (getPlayerNames().contains(stringMessage)) {
            return new ChooseStartingPlayerResponse(getPlayer(), stringMessage);
        }
        return new ErrorMessage(getPlayer(), MessageFormat.format(Message.NOT_VALID_USERNAME, stringMessage));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getMessage() + "\n");
        for (String playerName: playerNames) {
            sb.append(playerName).append(" ");
        }
        return sb.toString();
    }
}
