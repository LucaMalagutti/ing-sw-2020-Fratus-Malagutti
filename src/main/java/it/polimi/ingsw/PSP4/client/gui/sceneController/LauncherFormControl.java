package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.client.gui.AlertBox;
import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.Request;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LauncherFormControl extends GUIController {
    private String chosenUsername;
    public TextField server;
    public TextField username;

    boolean firstTime = true;

    public void submitLauncherForm() {
        if(server.getText().length() == 0) {
            AlertBox.displayError("Input not valid", "Server IP field cannot be empty");
            return;
        }
        if(username.getText().replaceAll("\\s", "").length() < 1 || username.getText().replaceAll("\\s", "").length() > 15) {
            AlertBox.displayError("Input not valid", "Username must be between 1 and 15 characters");
            return;
        }
        if(username.getText().replaceAll("\\s", "").equals("@")) {
            AlertBox.displayError("Username invalid", "Username must be different from '@'");
            return;
        }
        if (!getClient().isConnected())
            try {
                getClient().setConnected(getClient().connectToServer(server.getText()));
            } catch (IOException e) {
                e.getMessage();
            }
        if (!getClient().isConnected())
            AlertBox.displayError("Server connection problem", "There was a problem connecting to the server. Try again!");
        else {
            chosenUsername = username.getText();
            if (getClient().getLastRequestReceived() != null) {
                getClient().validate(chosenUsername);
            }
        }
    }

    public void updateUI (Request req) {
        if (req.getType() == MessageType.CHOOSE_USERNAME && firstTime) {
            firstTime = false;
            getClient().validate(chosenUsername);
        }  else if (req.getType() == MessageType.CHOOSE_NUM_PLAYERS) {
            getClient().setUsername(chosenUsername);
            getClient().updateScene(FXMLFile.LAUNCHER_NUMBER_PLAYERS, null,false);
        } else if (req.getType() == MessageType.INFO) {
            if (req.getMessage().equals(Message.WAIT_LOBBY_SETUP)) {
                AlertBox.displayError("Info", req.getMessage());
                getClient().updateScene(FXMLFile.LOBBY_WAIT, null, false);
            } else if (req.getMessage().equals(Message.ENTERING_LOBBY)) {
                //TODO fix when second of three players joins.
                AlertBox.displayError("Info", req.getMessage());
                //getClient().updateScene(FXMLFile.LOBBY_WAIT, null,false);
            }
            else {
                AlertBox.displayError("Info", req.getMessage());
            }
        } else if (req.getType() == MessageType.WAIT) {
            getClient().setUsername(chosenUsername);
            getClient().updateScene(FXMLFile.LOBBY_WAIT, null,false);
        }
        else {
            //TODO checks
            System.out.println("Unexpected "+req.getType());
        }
    }


    public void setupAttributes(Request req) {}
}
