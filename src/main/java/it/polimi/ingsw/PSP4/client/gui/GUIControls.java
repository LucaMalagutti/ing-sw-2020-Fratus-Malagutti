package it.polimi.ingsw.PSP4.client.gui;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.stream.Collectors;

public class GUIControls {
    public TextField server;
    public TextField username;

    public GridPane implementedGodsGrid;

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
        GUIClient.window.setX(GUIClient.window.getX() - 240);
        GUIClient.window.setY(GUIClient.window.getY() - 60);
        GUIClient.updateScene(FXMLFile.LOBBY_ALLOWED_GODS, false);
    }

    public void setThreePlayers() {
        System.out.println("3 players selected");
        GUIClient.updateScene(FXMLFile.LOBBY_ALLOWED_GODS, false);
    }

    public void setTwoGods() {
        System.out.println("2 Gods selected");
        GUIClient.updateScene(FXMLFile.LOBBY_YOUR_GOD_SELECTION_TWO, false);
    }

    public void setThreeGods() {
        System.out.println("3 Gods selected");
        GUIClient.updateScene(FXMLFile.LOBBY_YOUR_GOD_SELECTION_THREE, false);
    }

    public void godSelectedTwo() {
        //TODO GET THE SELECTED GODS
        GUIClient.updateScene(FXMLFile.LOBBY_STARTING_PLAYER_SELECTION_TWO, false);
    }

    public void godSelectedThree() {
        //TODO GET THE SELECTED GODS
        GUIClient.updateScene(FXMLFile.LOBBY_STARTING_PLAYER_SELECTION_THREE, false);
    }

    public void toggleGodSelection(Event event){
        Node god = (Node)event.getSource();
        List<Node> selectedGods = implementedGodsGrid.getChildren().stream().filter(card->card.getStyleClass().contains("selected")).collect(Collectors.toList());
        if (!selectedGods.contains(god) && selectedGods.size() < 2) {
            god.getStyleClass().add("selected");
        } else {
            god.getStyleClass().remove("selected");
        }
    }
}
