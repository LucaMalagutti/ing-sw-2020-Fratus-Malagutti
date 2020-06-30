package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.ChooseStartingPlayerResponse;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Message to ask the first player connected to choose who starts the game
 */
public class ChooseStartingPlayerRequest extends Request {
    private static final long serialVersionUID = -5015794011565067400L;
    private final static MessageType staticType = MessageType.CHOOSE_STARTING_PLAYER;

    private final List<String> playerNames;         //List of usernames
    private final Map<String, String> playerGods;   //Map username, god name

    public List<String> getPlayerNames() {return playerNames;}
    public Map<String, String> getPlayerGods() { return playerGods; }

    /**
     * Constructor of the class ChooseStartingPlayerRequest
     * @param player username of the receiver
     * @param playerNames list of usernames of the players
     * @param playerGods Map playerName to playerGodName
     */
    public ChooseStartingPlayerRequest(String player, List<String> playerNames, Map<String, String> playerGods) {
        super(player, null, Message.CHOOSE_STARTING_PLAYER, staticType);
        this.playerGods = playerGods;
        this.playerNames = playerNames;
    }

    @Override
    public Message validateResponse(String stringMessage) {
        stringMessage = stringMessage.trim();
        if (getPlayerGods().containsKey(stringMessage)) {
            return new ChooseStartingPlayerResponse(getPlayer(), stringMessage);
        }
        return new ErrorMessage(getPlayer(), MessageFormat.format(Message.NOT_VALID_USERNAME, stringMessage));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getMessage() + "\n");
        for (String playerName: playerGods.keySet()) {
            sb.append(playerName).append(" ");
        }
        return sb.toString();
    }
}
