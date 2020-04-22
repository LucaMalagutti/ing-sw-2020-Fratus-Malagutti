package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.MessageType;

import java.util.List;

/**
 * Message to ask the first player connected to choose the gods to be used during the game
 */
public class ChooseAllowedGodsRequest extends Request {
    private static final long serialVersionUID = -6997231787914012633L;
    private static final MessageType staticType = MessageType.CHOOSE_ALLOWED_GODS;

    private final List<String> selectableGods;          //List of the implemented god names to choose from

    public List<String> getSelectableGods() { return selectableGods; }

    /**
     * Constructor of the class ChooseAllowedGodsRequest
     * @param player username of the receiver
     * @param message text message from the sender
     * @param selectableGods list of the implemented god names to choose from
     */
    public ChooseAllowedGodsRequest(String player, String message, List<String> selectableGods) {
        super(player, message, staticType);
        this.selectableGods = selectableGods;
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
