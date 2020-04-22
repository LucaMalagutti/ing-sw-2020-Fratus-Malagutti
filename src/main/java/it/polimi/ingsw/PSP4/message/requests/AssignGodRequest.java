package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;

import java.text.MessageFormat;
import java.util.List;

/**
 * Message to ask a player to choose his card from the list
 */
public class AssignGodRequest extends Request {
    private static final long serialVersionUID = 7862594283408545286L;
    private final static MessageType staticType = MessageType.ASSIGN_GOD;

    private final List<String> allowedGods;       //List of the allowed god names to choose from

    public List<String> getAllowedGods() { return allowedGods; }

    /**
     * Constructor of the class AssignGodRequest
     * @param player username of the receiver
     * @param allowedGods list of allowed god names to choose from
     */
    public AssignGodRequest(String player, List<String> allowedGods) {
        super(player, Message.CHOOSE_GOD, staticType);
        this.allowedGods = allowedGods;
    }

    @Override
    public String toString() {
        if (allowedGods.size() > 1) {
            StringBuilder sb = new StringBuilder(getMessage() + "\n");
            for (String godName : allowedGods) {
                sb.append(godName.substring(0, 1).toUpperCase()).append(godName.substring(1).toLowerCase()).append(" ");
            }
            return sb.toString();
        } else {
            String godName = allowedGods.get(0);
            godName = godName.substring(0,1).toUpperCase() + godName.substring(1).toLowerCase();
            return MessageFormat.format(Message.LAST_ASSIGNED_GOD, godName);
        }
    }
}
