package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.client.gui.GUIClient;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.Request;

public class LauncherNumberPlayersControl extends GUIController {
    private boolean selected = false;

    /**
     * Listener for a click on the 2 players button
     */
    public void setTwoPlayers() {
        if (!selected) {
            selected = true;
            getClient().validate("2");
        }
    }

    /**
     * Listener for a click on the 3 players button
     */
    public void setThreePlayers() {
        if (!selected) {
            selected = true;
            getClient().validate("3");
        }
    }

    @Override
    public void updateUI (Request req) {
        if (req.getType() == MessageType.CHOOSE_ALLOWED_GODS) {
            getClient().updateScene(FXMLFile.LOBBY_GODS_SELECTION, req);
        } else if (req.getType() == MessageType.INFO) {
            if (req.getMessage().equals(Message.WAIT_PLAYERS)) {
                getClient().updateScene(FXMLFile.LOBBY_WAIT, null);
            }
        } else {
            System.out.println(GUIClient.UNEXPECTED_REQUEST);
        }
    }

    @Override
    public void setupAttributes(Request req) {}
}
