package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.Request;

public class LauncherNumberPlayersControl extends GUIController {
    private boolean selected = false;

    public void setTwoPlayers() {
        if (!selected) {
            selected = true;
            getClient().validate("2");
        }
    }

    public void setThreePlayers() {
        if (!selected) {
            selected = true;
            getClient().validate("3");
        }
    }

    public void updateUI (Request req) {
        if (req.getType() == MessageType.CHOOSE_ALLOWED_GODS) {
            getClient().updateScene(FXMLFile.LOBBY_GODS_SELECTION, req);
        } else if (req.getType() == MessageType.INFO) {
            if (req.getMessage().equals(Message.WAIT_PLAYERS)) {
                getClient().updateScene(FXMLFile.LOBBY_WAIT, null);
            }
        } else {
            System.out.println("Unexpected request");
        }
    }

    public void setupAttributes(Request req) {}
}
