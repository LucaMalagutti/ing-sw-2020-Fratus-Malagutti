package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.message.requests.AssignGodRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LobbyYourGodSelectionControl extends GUIController {
    public VBox godPicturesVBox;
    public Pane selectedGodPicture;
    public Text selectedGodText;

    public void selectedGod() {
        //TODO IMPLEMENT
    }

    @Override
    public void updateUI(Request req) {
        //TODO IMPLEMENT
    }

    @Override
    public void setupAttributes(Request req) {
        AssignGodRequest r = (AssignGodRequest) req;
        int numPlayers = r.getAllowedGods().size();

        selectedGodPicture.getStyleClass().remove(1);
        selectedGodPicture.getStyleClass().add(r.getAllowedGods().get(0).toLowerCase());

        selectedGodText.setText(r.getAllowedGods().get(0).toUpperCase());

        for (int i = 0; i< numPlayers; i++) {
            Node godPane = godPicturesVBox.getChildren().get(i);
            godPane.getStyleClass().remove(1);
            godPane.getStyleClass().add(r.getAllowedGods().get(i).toLowerCase());
        }
    }
}
