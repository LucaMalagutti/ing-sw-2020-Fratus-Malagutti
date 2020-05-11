package it.polimi.ingsw.PSP4.controller;

import it.polimi.ingsw.PSP4.client.gui.AlertBox;
import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.client.gui.GUIClient;
import it.polimi.ingsw.PSP4.message.requests.Request;
import it.polimi.ingsw.PSP4.message.responses.Response;
import it.polimi.ingsw.PSP4.observer.Observer;
import javafx.scene.control.TextField;

/**
 * Controller base class. Observes all the (virtual) views
 */
public class Controller implements Observer<Response> {

    public TextField server;
    public TextField username;

    public void launchGame() {
        GUIClient.updateScene(FXMLFile.LAUNCHER_FORM, false);
    }

    public void submitLauncherForm() {
        if(server.getText().length() == 0) {
            AlertBox.displayError("Input not valid", "Server IP field cannot be empty");
            return;
        }
        if(username.getText().length() < 1 || username.getText().length() > 15) {
            AlertBox.displayError("Input not valid", "Username must be between 1 and 15 characters");
            return;
        }

        System.out.println("Submission: " + server.getText() + " " + username.getText());
        GUIClient.updateScene(FXMLFile.LAUNCHER_NUMBER_PLAYERS, false);
    }

    public void setTwoPlayers() {
        System.out.println("2 players selected");
        GUIClient.updateScene(FXMLFile.LOBBY_ALLOWED_GODS, true);
    }

    public void setThreePlayers() {
        System.out.println("3 players selected");
        GUIClient.updateScene(FXMLFile.LOBBY_ALLOWED_GODS, true);
    }

    /**
     * At the view's notifyObservers(), updates the model depending on the message content
     * @param response Response sent by the view
     */
    @Override
    public void update(Response response) {
        response.handle();
    }
}
