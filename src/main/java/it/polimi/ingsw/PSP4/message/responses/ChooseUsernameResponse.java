package it.polimi.ingsw.PSP4.message.responses;

import it.polimi.ingsw.PSP4.message.MessageType;

public class ChooseUsernameResponse extends Response {
    private static final long serialVersionUID = 8779750296669797591L;
    private final static MessageType staticType = MessageType.CHOOSE_USERNAME;
    private final String selectedUsername;                                               //Username chosen by the player

    public String getSelectedUsername() { return selectedUsername; }

    public ChooseUsernameResponse(String selectedUsername) {
        super("", "", staticType);
        this.selectedUsername = selectedUsername;
    }

    @Override
    public void handle() {}
}
