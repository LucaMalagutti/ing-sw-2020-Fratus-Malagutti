package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.client.gui.AlertBox;
import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.ChooseAllowedGodsRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class LobbyAllowedGodsControl extends GUIController {
    public GridPane implementedGodsGrid;
    public Text callToAction;
    private int numPlayers;

    public void toggleGodSelection(Event event){
        Node god = (Node)event.getSource();
        List<Node> selectedGods = implementedGodsGrid.getChildren().stream().filter(card->card.getStyleClass().contains("selected")).collect(Collectors.toList());
        if (!selectedGods.contains(god) && selectedGods.size() < numPlayers) {
            god.getStyleClass().add("selected");
        } else {
            god.getStyleClass().remove("selected");
        }
    }

    public void setGods() {
        List<Node> selectedGods = implementedGodsGrid.getChildren().stream().filter(card->card.getStyleClass().contains("selected")).collect(Collectors.toList());
        if (selectedGods.size() == numPlayers) {
            List<String> selectedGodsNames = implementedGodsGrid.getChildren().stream().filter(card->card.getStyleClass().contains("selected")).map(Node::getId).collect(Collectors.toList());
            StringBuilder sb = new StringBuilder();
            for (String name: selectedGodsNames) {
                sb.append(name).append(" ");
            }
            System.out.println(sb.toString());
            getClient().validate(sb.toString());
        }
    }

    public void updateUI (Request req) {
        if (req.getType() == MessageType.ASSIGN_GOD && numPlayers == 2) {
            getClient().updateScene(FXMLFile.LOBBY_YOUR_GOD_SELECTION_TWO, req, false);
        } else if (req.getType() == MessageType.ASSIGN_GOD && numPlayers == 3) {
            getClient().updateScene(FXMLFile.LOBBY_YOUR_GOD_SELECTION_THREE, req, false);
        } else if (req.getType() == MessageType.WAIT) {
            getClient().updateScene(FXMLFile.LOBBY_WAIT, null, false);
        } else if (req.getType() == MessageType.INFO) {
            AlertBox.displayError("Info", req.getMessage());
        } else {
            System.out.println("Unexpected"+ req.getType());
        }
    }

    public void setupAttributes(Request req) {
        ChooseAllowedGodsRequest r = (ChooseAllowedGodsRequest) req;
        numPlayers = r.getNumPlayer();
        callToAction.setText("SELECT " + numPlayers + " GODS");
//        for(String god : r.getSelectableGods()) {
//            Pane photo = new Pane(), frame = new Pane();
//            StackPane card = new StackPane();
//            photo.getStyleClass().add("selectable-image");
//            frame.getStyleClass().add("selectable-frame");
//            card.getStyleClass().addAll("hover-effect-out", "selectable-god", god.toLowerCase());
//            card.getChildren().addAll(photo, frame);
//            card.setOnMousePressed(this::toggleGodSelection);
//            if(implementedGodsGrid.getChildren().size() < 16)
//                implementedGodsGrid.getChildren().add(card);
//        }
    }
}
