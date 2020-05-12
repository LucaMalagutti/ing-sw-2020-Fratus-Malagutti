package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.ChooseNumPlayersResponse;

public class ChooseNumPlayersRequest extends Request {
    private static final long serialVersionUID = -287969460027335156L;
    private static final MessageType staticType = MessageType.CHOOSE_NUM_PLAYERS;

    public ChooseNumPlayersRequest(String player) {
        super(player, null, Message.CHOOSE_NUMBER_PLAYERS, staticType);
    }

    @Override
    public Message validateResponse(String stringMessage) {
        String numPlayers = stringMessage.replaceAll("\\s", "");
        if (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("")) {
            return new ErrorMessage(getPlayer(), Message.NOT_VALID_PLAYER_NUMBER);
        }
        if (numPlayers.equals("")) {
            numPlayers = "2";
        }
        return new ChooseNumPlayersResponse(getPlayer(), Integer.parseInt(numPlayers));
    }

    @Override
    public String toString() { return getMessage(); }
}
