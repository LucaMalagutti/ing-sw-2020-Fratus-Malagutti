package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;

/**
 * Message to tell the controller who starts the game
 */
public class ChooseStartingPlayerResponse extends Response {
    private static final long serialVersionUID = 8611342076728850938L;
    private final static MessageType staticType = MessageType.CHOOSE_STARTING_PLAYER;

    private final String selectedPlayer;            //Username of the player that will start the game

    public String getSelectedPlayer() { return selectedPlayer; }

    /**
     * Constructor of the class ChooseStartingPlayerResponse
     * @param player username of the sender
     * @param selectedPlayer username of the player that will start the game
     */
    public ChooseStartingPlayerResponse(String player, String selectedPlayer) {
        super(player, "", staticType);
        this.selectedPlayer = selectedPlayer;
    }

    @Override
    public void handle() {
        Player selectedPlayer = GameState.getInstance().getPlayerFromUsername(getSelectedPlayer());
        if (selectedPlayer == null)
            selectedPlayer = GameState.getInstance().getCurrPlayer();
        GameState.getInstance().setCurrPlayer(selectedPlayer);
        GameState.getInstance().firstWorkerPlacement(0,0);
    }
}
