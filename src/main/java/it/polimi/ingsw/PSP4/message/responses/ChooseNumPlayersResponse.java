package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;

public class ChooseNumPlayersResponse extends Response {
    private static final long serialVersionUID = 2044113170037084232L;
    private final static MessageType staticType = MessageType.CHOOSE_NUM_PLAYERS;
    private final int selectedNumPlayers;                                               //NumPlayers chosen by the player

    public int getSelectedNumPlayers() { return selectedNumPlayers; }

    public ChooseNumPlayersResponse(String username, int selectedNumPlayers) {
        super(username, "", staticType);
        this.selectedNumPlayers = selectedNumPlayers;
    }

    @Override
    public void handle() {}
}
