package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.message.requests.AssignGodRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.stream.Collectors;

public class LobbySelectGodControl extends GUIController {
    public GridPane implementedGodsGrid;
    private final int numPlayers = 1;

    public void toggleGodSelection(Event event){
        Node god = (Node)event.getSource();
        List<Node> selectedGods = implementedGodsGrid.getChildren().stream().filter(card->card.getStyleClass().contains("selected")).collect(Collectors.toList());
        if (!selectedGods.contains(god) && selectedGods.size() < numPlayers) {
            god.getStyleClass().add("selected");
        } else {
            god.getStyleClass().remove("selected");
        }
    }

    public void selectGod() {
        List<Node> selectedGods = implementedGodsGrid.getChildren().stream().filter(card->card.getStyleClass().contains("selected")).collect(Collectors.toList());
        if(selectedGods.size() == numPlayers)
            System.out.println(selectedGods.get(0));
        //TODO IMPLEMENT
    }

    public void updateUI (Request req) {
        //TODO IMPLEMENT
    }

    public void setupAttributes(Request req) {
        AssignGodRequest r = (AssignGodRequest) req;
        for(String god : r.getAllowedGods()) {
            Pane photo = new Pane(), frame = new Pane();
            StackPane card = new StackPane();
            photo.getStyleClass().add("selectable-image");
            frame.getStyleClass().add("selectable-frame");
            card.getStyleClass().addAll("hover-effect-out", "selectable-god", god.toLowerCase());
            card.getChildren().addAll(photo, frame);
            card.setId(god);
            card.setOnMousePressed(this::toggleGodSelection);
            int index = r.getAllowedGods().indexOf(god);
            int col = implementedGodsGrid.getColumnCount();
            implementedGodsGrid.add(card, index % col, index / col);
        }
    }
}
