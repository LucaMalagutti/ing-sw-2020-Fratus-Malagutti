package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.client.gui.GUIClient;
import it.polimi.ingsw.PSP4.message.requests.Request;

public abstract class GUIController {
    private GUIClient client;

    public void setClient(GUIClient client) {
        if (this.client == null) {
            this.client = client;
        }
    }

    public GUIClient getClient() {return this.client;}

    /**
     * Updates the existing scene when a new request is received from the server
     * @param req request received
     */
    public abstract void updateUI (Request req);

    /**
     * Describes how to populate the scene when it is first loaded
     * @param req request used to populate the scene
     */
    public abstract void setupAttributes(Request req);
}
