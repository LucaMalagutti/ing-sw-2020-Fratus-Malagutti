package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.message.requests.AssignFirstWorkerPlacementRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;
import it.polimi.ingsw.PSP4.model.serializable.SerializableGameState;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePlayer;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardController extends GUIController{
    public GridPane board;
    public VBox playersList;
    public HBox player1;
    public HBox player2;
    public HBox player3;

    private SerializableGameState gameState;

    /**
     * @param property property of the object to change
     * @param from starting value of property
     * @param to ending value of property
     * @param duration duration of the transition in ms
     */
    public void linearTransition(DoubleProperty property, double from, double to, double duration) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(property, from)),
                new KeyFrame(Duration.millis(duration), new KeyValue(property, to))
        );
        timeline.play();
    }
    public void showPlayersList() { linearTransition(playersList.translateXProperty(), 320, 0, 250.0d); }
    public void hidePlayersList() { linearTransition(playersList.translateXProperty(), 0, 320, 250.0d); }

    /**
     * Add a player to the first empty space in the list on the right
     * @param player serializable version of the player to add
     */
    public void addPlayer(SerializablePlayer player) {
        List<HBox> freeSpaces = Stream.of(player1, player2, player3).filter(p -> !p.isVisible()).collect(Collectors.toList());
        if(freeSpaces.size() <= 0)
            return;
        HBox space = freeSpaces.get(0);
        space.getChildren().get(0).getStyleClass().add(player.getCard().getName().toLowerCase());
        VBox text = (VBox)space.getChildren().get(1);
        ((Text)text.getChildren().get(0)).setText(player.getCard().getName());
        ((Text)text.getChildren().get(1)).setText(player.getUsername());
        space.setVisible(true);
    }

    private void addEntity(StackPane cell, String className) {
        Pane block = new Pane();
        block.getStyleClass().addAll("entity", className);
        cell.getChildren().add(block);
    }

    public void addCell(SerializablePosition position) {
        int height = Math.min(position.getHeight(), 3);
        StackPane cell = new StackPane();
        for(int i = 1; i <= height; i++)
            addEntity(cell, "level-" + i);
        if(position.hasDome())
            addEntity(cell, "dome");
        if(position.getWorker() != null)
            addEntity(cell, gameState.getPlayerColor(gameState.getPlayerFromWorker(position)).name().toLowerCase() + "-worker");
        board.add(cell, position.getCol(), position.getRow());
    }

    public void addCell() {
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                StackPane cell = new StackPane();
                Pane block = new Pane();
                block.getStyleClass().addAll("entity", "level-1");
                cell.getChildren().add(block);
                board.add(cell, i, j);
            }
        }
    }

    @Override
    public void updateUI(Request req) {

    }

    @Override
    public void setupAttributes(Request req) {
        AssignFirstWorkerPlacementRequest r = (AssignFirstWorkerPlacementRequest) req;
        gameState = r.getBoard();
        gameState.getPlayers().forEach(this::addPlayer);
        gameState.getBoard().forEach(this::addCell);
    }
}
