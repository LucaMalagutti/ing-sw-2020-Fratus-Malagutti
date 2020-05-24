package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.client.gui.AlertBox;
import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.Request;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.text.MessageFormat;

public class LauncherFormControl extends GUIController {
    @FXML
    private Label errorLabel;
    @FXML
    private TextField server;
    @FXML
    private TextField username;
    private String chosenUsername;

    boolean firstTime = true;

    public void submitLauncherForm() {
        if(server.getText().length() == 0) {
            errorLabel.setText("Server IP field cannot be empty");
            return;
        }
        if(username.getText().replaceAll("\\s", "").length() < 1 || username.getText().replaceAll("\\s", "").length() > 15) {
            errorLabel.setText("Username must be between 1 and 15 characters");
            return;
        }
        if(username.getText().replaceAll("\\s", "").equals("@")) {
            errorLabel.setText("Username must be different from '@'");
            return;
        }
        if (!getClient().isConnected())
            try {
                getClient().setConnected(getClient().connectToServer(server.getText()));
            } catch (IOException e) {
                e.getMessage();
            }
        if (!getClient().isConnected())
            errorLabel.setText("There was a problem connecting to the server. Try again!");
        else {
            errorLabel.setText(null);
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
            getClient().updateScene(FXMLFile.LAUNCHER_NUMBER_PLAYERS, null);
        } else if (req.getType() == MessageType.INFO) {
            if (req.getMessage().equals(Message.WAIT_LOBBY_SETUP)) {
                errorLabel.setText(Message.WAIT_LOBBY_SETUP);
            } else if (req.getMessage().equals(MessageFormat.format(Message.USERNAME_TAKEN, chosenUsername))) {
                errorLabel.setText(MessageFormat.format(Message.USERNAME_TAKEN, chosenUsername));
            } else if (req.getMessage().equals(Message.GAME_ALREADY_STARTED)) {
                AlertBox.displayError("Game Already Started", "A game has already started. Try again later!");
            } else if (req.getMessage().equals(MessageFormat.format(Message.ENTERING_LOBBY, chosenUsername))) {
                getClient().setUsername(chosenUsername);
                getClient().updateScene(FXMLFile.LOBBY_WAIT, null);
            }
        } else if (req.getType() == MessageType.WAIT) {
            getClient().setUsername(chosenUsername);
            getClient().updateScene(FXMLFile.LOBBY_WAIT, null);
        }
        else {
            System.out.println("Unexpected "+req.getType()+req.getMessage());
        }
    }


    public void setupAttributes(Request req) {}
}
