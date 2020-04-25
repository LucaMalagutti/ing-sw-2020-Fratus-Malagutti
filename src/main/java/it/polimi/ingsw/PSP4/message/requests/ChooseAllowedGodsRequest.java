package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.ChooseAllowedGodsResponse;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Message to ask the first player connected to choose the gods to be used during the game
 */
public class ChooseAllowedGodsRequest extends Request {
    private static final long serialVersionUID = -6997231787914012633L;
    private static final MessageType staticType = MessageType.CHOOSE_ALLOWED_GODS;

    private final List<String> selectableGods;          //List of the implemented god names to choose from
    private final int numPlayer;                        //Number of players

    public List<String> getSelectableGods() { return selectableGods; }
    public int getNumPlayer() { return numPlayer; }

    /**
     * Constructor of the class ChooseAllowedGodsRequest
     * @param player username of the receiver
     * @param selectableGods list of the implemented god names to choose from
     * @param numPlayer number of players
     */
    public ChooseAllowedGodsRequest(String player, List<String> selectableGods, int numPlayer) {
        super(player, MessageFormat.format(Message.CHOOSE_ALLOWED_GODS, numPlayer), staticType);
        this.selectableGods = selectableGods;
        this.numPlayer = numPlayer;
    }

    @Override
    public Message validateResponse(String stringMessage) {
        String[] godNames = stringMessage.split(" ");
        if (godNames.length == getNumPlayer()) {
            List<String> allowedGods = new ArrayList<>();
            for (String godName: godNames) {
                if (!getSelectableGods().contains(godName.toUpperCase()))
                    return new ErrorMessage(getPlayer(), Message.NOT_VALID_GOD_LIST);
                allowedGods.add(godName.toUpperCase());
            }
            return new ChooseAllowedGodsResponse(getPlayer(), allowedGods);
        }
        return new ErrorMessage(getPlayer(), Message.NOT_VALID_GOD_LIST);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getMessage() + "\n");
        for (String godName: selectableGods) {
            sb.append(godName.substring(0, 1).toUpperCase()).append(godName.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString();
    }
}
