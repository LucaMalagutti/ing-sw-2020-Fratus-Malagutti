package it.polimi.ingsw.PSP4.message;

import java.util.List;

public class ChooseStartingPlayerMessage extends Message {
    private static final long serialVersionUID = 6584214517057772796L;
    private final static MessageType staticType = MessageType.CHOOSE_STARTING_PLAYER_MESSAGE;

    public List<String> getPlayerNames() { return playerNames; }

    List<String> playerNames;

    public ChooseStartingPlayerMessage(String player, List<String> playerNames) {
        super(player, "Choose the first player to start the game from the following list:");
        this.playerNames = playerNames;
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
