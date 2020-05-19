package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.AssignGodRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;

public class LobbyWaitControl extends GUIController{

    public void updateUI(Request req) {
        if (req.getType() == MessageType.CHOOSE_USERNAME) {
            getClient().updateScene(FXMLFile.LAUNCHER_FORM, null, false);
        } else if (req.getType() == MessageType.CHOOSE_ALLOWED_GODS) {
            getClient().updateScene(FXMLFile.LOBBY_ALLOWED_GODS, req,false);
        } else if (req.getType() == MessageType.ASSIGN_GOD) {
            getClient().updateScene(FXMLFile.LOBBY_SELECT_GOD, req, false);
        }
    }

    public void setupAttributes(Request req) {}
}
