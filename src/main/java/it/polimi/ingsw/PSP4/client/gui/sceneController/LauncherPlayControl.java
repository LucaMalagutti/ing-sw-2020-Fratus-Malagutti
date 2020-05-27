package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.client.gui.GUIClient;
import it.polimi.ingsw.PSP4.message.requests.Request;

public class LauncherPlayControl extends GUIController {
    public void launchGame() {
        getClient().updateScene(FXMLFile.LAUNCHER_FORM, null);
    }

    @Override
    public void updateUI (Request req) {
        System.out.println(GUIClient.UNEXPECTED_REQUEST);
    }

    @Override
    public void setupAttributes(Request req) {}
}
