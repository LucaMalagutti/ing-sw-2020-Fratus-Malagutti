package it.polimi.ingsw.PSP4.message.requests;

import it.polimi.ingsw.PSP4.message.ErrorMessage;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.responses.ChooseUsernameResponse;

public class ChooseUsernameRequest extends Request {
    private static final long serialVersionUID = 1728824568070830729L;
    private static final MessageType staticType = MessageType.CHOOSE_USERNAME;

    public ChooseUsernameRequest() {
        super("", null, Message.CHOOSE_USERNAME, staticType);
    }

    @Override
    public Message validateResponse(String stringMessage) {
        stringMessage = stringMessage.replaceAll("\\s", "");
       if (stringMessage.equals("") || stringMessage.length() > 15 || stringMessage.equals("@")) {
            if (stringMessage.equals("@")) {
                return new ErrorMessage("", Message.USERNAME_CHAR);
            } else {
                return new ErrorMessage("", Message.USERNAME_LENGTH);
            }
        }
        return new ChooseUsernameResponse(stringMessage);
    }

    @Override
    public String toString() { return getMessage(); }
}
