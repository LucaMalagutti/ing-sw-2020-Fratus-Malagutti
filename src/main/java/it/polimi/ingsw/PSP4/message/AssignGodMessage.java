package it.polimi.ingsw.PSP4.message;

import java.util.List;

public class AssignGodMessage extends Message {
    private static final long serialVersionUID = -605427450694294094L;
    private final static MessageType staticType = MessageType.ASSIGN_GOD;
    //Array of the allowed god names left to choose from (request)
    private final List<String> allowedGods;
    //God that this player has picked
    private final String chosenGod;

    public String getChosenGod() { return chosenGod; }
    public List<String> getAllowedGods() {return allowedGods;}


    public AssignGodMessage(String player, String message, List<String> allowedGods, String chosenGod) {
        super(player, message);
        this.allowedGods = allowedGods;
        this.chosenGod = chosenGod;
    }

    @Override
    public String toString() {
        if (allowedGods.size() > 1) {
            StringBuilder sb = new StringBuilder(getMessage() + "\n");
            for (String godName : allowedGods) {
                sb.append(godName.substring(0, 1).toUpperCase()).append(godName.substring(1).toLowerCase()).append(" ");
            }
            return sb.toString();
        }
        else {
            String godName = allowedGods.get(0);
            godName = godName.substring(0,1).toUpperCase() + godName.substring(1).toLowerCase();
            return "As the last player to choose, you have been assigned "+godName+". Press Enter to continue";
        }
    }
}
