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

    public abstract void updateUI (Request req);

    public abstract void setupAttributes(Request req);
}
