package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.client.gui.AlertBox;
import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.client.gui.GodPower;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.AssignGodRequest;
import it.polimi.ingsw.PSP4.message.requests.ChooseAllowedGodsRequest;
import it.polimi.ingsw.PSP4.message.requests.ChooseStartingPlayerRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class LobbyGodsSelectionControl extends GUIController {
    @FXML
    private GridPane implementedGodsGrid;
    @FXML
    private Text callToAction;
    @FXML
    private VBox godInfo;
    @FXML
    private Text button;
    private int numPlayers;
    public HBox highlightedGod;

    public void toggleGodSelection(Event event){
        Node god = (Node)event.getSource();
        List<Node> selectedGods = implementedGodsGrid.getChildren().stream().filter(card->card.getStyleClass().contains("selected")).collect(Collectors.toList());
        if (!selectedGods.contains(god) && selectedGods.size() < numPlayers) {
            god.getStyleClass().add("selected");
        } else {
            god.getStyleClass().remove("selected");
        }
    }

    public void showGodInfo(MouseEvent event) {
        String god = ((Node) event.getSource()).getId();

        Text godName = (Text) godInfo.getChildren().get(0);
        godName.setText(god);

        StackPane descriptionContainer = (StackPane) godInfo.getChildren().get(1);
        Text description = (Text) descriptionContainer.getChildren().get(0);
        String content = GodPower.getPowerFromGod(god).getDescription();
        description.setText(content);

        Pane powerImage = (Pane) godInfo.getChildren().get(2);
        List<String> powerClassNames = powerImage.getStyleClass();
        powerClassNames = powerClassNames.stream().filter(c -> !c.equals("god-power")).collect(Collectors.toList());
        powerImage.getStyleClass().removeAll(powerClassNames);
        powerImage.getStyleClass().add(god.toLowerCase());

        Pane godImage = (Pane) highlightedGod.getChildren().get(1);
        List<String> imageClassNames = godImage.getStyleClass();
        imageClassNames = imageClassNames.stream().filter(c -> !c.equals("god-card")).collect(Collectors.toList());
        godImage.getStyleClass().removeAll(imageClassNames);
        godImage.getStyleClass().add(god.toLowerCase());

    }

    public void sendAllowedGods() {
        List<Node> selectedGods = implementedGodsGrid.getChildren().stream().filter(card->card.getStyleClass().contains("selected")).collect(Collectors.toList());
        if (selectedGods.size() == numPlayers) {
            List<String> selectedGodsNames = implementedGodsGrid.getChildren().stream().filter(card->card.getStyleClass().contains("selected")).map(Node::getId).collect(Collectors.toList());
            StringBuilder sb = new StringBuilder();
            for (String name: selectedGodsNames) {
                sb.append(name).append(" ");
            }
            //System.out.println(sb.toString());
            getClient().validate(sb.toString());
        }
    }

    public void sendPersonalGod() {
        List<Node> selectedGods = implementedGodsGrid.getChildren().stream().filter(card->card.getStyleClass().contains("selected")).collect(Collectors.toList());
        if(selectedGods.size() == numPlayers) {
            //System.out.println(selectedGods.get(0).getId());
            getClient().validate(selectedGods.get(0).getId());
        }
    }

    private void addGodCard(String god, int index) {
        Pane image = new Pane(), frame = new Pane();
        image.getStyleClass().add("selectable-image");
        frame.getStyleClass().add("selectable-frame");

        StackPane card = new StackPane();
        card.getStyleClass().addAll("hover-effect-out", "selectable-god", god.toLowerCase());
        card.setId(god);
        card.setOnMousePressed(this::toggleGodSelection);
        card.setOnMouseEntered(this::showGodInfo);
        card.getChildren().addAll(image, frame);

        int colNum = implementedGodsGrid.getColumnCount();
        implementedGodsGrid.add(card, index % colNum, index / colNum);
    }

    private void setupAllowedGods(ChooseAllowedGodsRequest req) {
        numPlayers = req.getNumPlayer();
        callToAction.setText("SELECT " + numPlayers + " GODS");
        List<String> godList = req.getSelectableGods();
        for(int i = 0; i < godList.size(); i++)
            addGodCard(godList.get(i), i);
        button.setOnMousePressed(e -> sendAllowedGods());
    }

    private void setupPersonalGod(AssignGodRequest req) {
        numPlayers = 1;
        callToAction.setText("SELECT YOUR GOD");
        List<String> godList = req.getAllowedGods();
        for(int i = 0; i < godList.size(); i++)
            addGodCard(godList.get(i), i);
        button.setOnMousePressed(e -> sendPersonalGod());
    }

    public void updateUI (Request req) {
        if (req.getType() == MessageType.ASSIGN_GOD) {
            getClient().updateScene(FXMLFile.LOBBY_GODS_SELECTION, req);
        } else if (req.getType() == MessageType.WAIT) {
            getClient().updateScene(FXMLFile.LOBBY_WAIT, null);
        } else if (req.getType() == MessageType.CHOOSE_STARTING_PLAYER) {
            ChooseStartingPlayerRequest r = (ChooseStartingPlayerRequest) req;
            if (r.getPlayerNames().size() == 2) {
                getClient().updateScene(FXMLFile.LOBBY_STARTING_PLAYER_SELECTION_TWO, req);
            } else if (r.getPlayerNames().size() == 3){
                getClient().updateScene(FXMLFile.LOBBY_STARTING_PLAYER_SELECTION_THREE, req);
            }
        } else if (req.getType() == MessageType.INFO) {
            AlertBox.displayError("Info", req.getMessage());
        } else {
            System.out.println("Unexpected"+ req.getType());
        }
    }

    public void setupAttributes(Request req) {
        if(req.getType() == MessageType.CHOOSE_ALLOWED_GODS)
            setupAllowedGods((ChooseAllowedGodsRequest) req);
        else if(req.getType() ==  MessageType.ASSIGN_GOD)
            setupPersonalGod((AssignGodRequest) req);
    }
}
