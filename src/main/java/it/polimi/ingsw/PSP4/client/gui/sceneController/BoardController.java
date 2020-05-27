package it.polimi.ingsw.PSP4.client.gui.sceneController;

import it.polimi.ingsw.PSP4.client.gui.AlertBox;
import it.polimi.ingsw.PSP4.client.gui.FXMLFile;
import it.polimi.ingsw.PSP4.client.gui.GUIClient;
import it.polimi.ingsw.PSP4.client.gui.GodGraphics;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.ChoosePositionRequest;
import it.polimi.ingsw.PSP4.message.requests.RemovePlayerRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;
import it.polimi.ingsw.PSP4.model.serializable.SerializableGameState;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePlayer;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardController extends GUIController{
    public GridPane board;
    public VBox playersList;
    public VBox statusButtons;
    public AnchorPane activePlayerPane;
    public Text activePlayerAction;
    public VBox endPane;
    public VBox confirmPane;

    private SerializableGameState gameState;
    private SerializablePlayer activePlayer;
    private final List<StackPane> grid = new ArrayList<>();

    /**
     * @param row rowIndex of the cell to find
     * @param col columnIndex of the cell to find
     * @return the node inside the grid at [row, col] coordinates, creating it if not already present
     */
    private StackPane getCellFromGrid(int row, int col) {
        List<Node> match = grid.stream().filter(c -> GridPane.getColumnIndex(c) == col && GridPane.getRowIndex(c) == row).collect(Collectors.toList());
        if(match.size() > 0)
            return (StackPane) match.get(0);
        StackPane cell = new StackPane();
        cell.getStyleClass().add("cell");
        board.add(cell, col, row);
        grid.add(cell);
        return cell;
    }

    /**
     * @param username name of the player to find
     * @return reference to the first player with this username in the players list
     */
    private SerializablePlayer findFirstMatch(String username) {
        List<SerializablePlayer> matches = gameState.getPlayers().stream().filter(p -> p.getUsername().equals(username)).collect(Collectors.toList());
        if(matches.size() > 0)
            return matches.get(0);
        return null;
    }

    /**
     * Set the reference to the owner of this window
     * @param backup receiver of the request used to try to recover if client's username is not found
     */
    private void setActivePlayer(String backup) {
        SerializablePlayer player = findFirstMatch(getClient().getUsername());
        if(player != null) {
            activePlayer = player;
            return;
        }

        player = findFirstMatch(backup);
        if(player != null) {
            getClient().setUsername(backup);
            activePlayer = player;
            return;
        }

        //TODO close the game
    }

    /**
     * @param property property of the object to change
     * @param from starting value of property
     * @param to ending value of property
     * @param duration duration of the transition in ms
     */
    private void linearTransition(DoubleProperty property, double from, double to, double duration) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(property, from)),
                new KeyFrame(Duration.millis(duration), new KeyValue(property, to))
        );
        timeline.play();
    }
    public void showPlayersList() { linearTransition(playersList.translateXProperty(), 320, 0, 250.0d); }
    public void hidePlayersList() { linearTransition(playersList.translateXProperty(), 0, 320, 251.0d); }

    /**
     * Add a player to the list on the right
     * @param player reference to the player to add
     */
    private void addPlayerToList(SerializablePlayer player) {
        if(playersList.getChildren().size() > 3)
            return;

        Pane godImage = new Pane();
        godImage.getStyleClass().add("selectable-image");
        godImage.setStyle(GodGraphics.getSelectableBG(player.getCard().getName()));

        Pane godFrame = new Pane();
        godFrame.getStyleClass().add("selectable-frame");

        StackPane godCard = new StackPane();
        godCard.getStyleClass().add("selectable-god");
        godCard.setPrefWidth(80);
        godCard.getChildren().addAll(godImage, godFrame);

        Text godName = new Text(player.getCard().getName());
        godName.getStyleClass().addAll("text", "lillybelle", "brown", "big");

        Text playerName = new Text(player.getUsername());
        playerName.getStyleClass().addAll("text", "minion-pro", "black", "small");

        VBox textContainer = new VBox();
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.getChildren().addAll(godName, playerName);

        HBox playerContainer = new HBox();
        playerContainer.setAlignment(Pos.TOP_LEFT);
        playerContainer.setSpacing(10);
        playerContainer.setMaxHeight(110);
        playerContainer.setMouseTransparent(true);
        playerContainer.getChildren().addAll(godCard, textContainer);

        playersList.getChildren().add(playerContainer);
    }

    /**
     * Add a Pane with the classStyle {entity, className} to cell
     * @param cell StackPane that will contain the entity
     * @param className class (or classes divided by a space) to be added
     * @return a reference to the entity added
     */
    private Pane addEntity(StackPane cell, String className) {
        String[] classNames = className.split(" ");
        Pane block = new Pane();
        block.getStyleClass().add("entity");
        block.getStyleClass().addAll(classNames);
        cell.getChildren().add(block);
        return block;
    }

    /**
     * Adds the highlight image to the cell and a handler on mouse pressed
     * @param cell StackPane to update
     * @param handler lambda function to call on mouse pressed
     */
    private void addClickable(StackPane cell, EventHandler<MouseEvent> handler) {
        Pane block = addEntity(cell, "highlight");
        block.setMouseTransparent(true);
        cell.setOnMousePressed(handler);
        cell.setCursor(Cursor.HAND);
    }

    /**
     * Add a Pane with the classStyle {entity, number} and a class which represents the height and the color
     * @param cell StackPane that will contain the entity
     * @param height number that will be added to the Pane
     * @param hasDome if true color will be white, else brown
     */
    private void addNumber(StackPane cell, int height, boolean hasDome) {
        StringBuilder sb = new StringBuilder("number ");
        if(hasDome)
            sb.append("light-");
        else
            sb.append("dark-");
        sb.append(height);
        addEntity(cell, sb.toString());
    }

    /**
     * Draws a position of the grid, stacking buildings, workers and numbers
     * @param position SerializablePosition to draw
     */
    private void drawCell(SerializablePosition position) {
        int height = position.getHeight();
        StackPane cell = getCellFromGrid(position.getRow(), position.getCol());
        for(int i = 1; i <= height; i++)
            addEntity(cell, "level-" + i);
        if(position.hasDome())
            addEntity(cell, "dome");
        if(position.getWorker() != null)
            addEntity(cell, gameState.getPlayerColor(gameState.getPlayerFromWorker(position)).name().toLowerCase() + "-worker");
        if(position.getHeight() > 0)
            addNumber(cell, position.getHeight(), position.hasDome());
    }

    /**
     * @param positions list of positions to highlight
     * @param handler lambda function to call on mouse pressed on each of those positions
     */
    private void highlightPositions(List<SerializablePosition> positions, EventHandler<MouseEvent> handler) {
        if(gameState.getCurrPlayer() != activePlayer)
            return;
        for(SerializablePosition position : positions) {
            StackPane cell = getCellFromGrid(position.getRow(), position.getCol());
            addClickable(cell, handler);
        }
    }
    private void highlightFreeCells() {
        highlightPositions(gameState.getFreePositions(), this::positionSelected);
    }
    private void highlightWorkers() {
        highlightPositions(activePlayer.getWorkersPositions(), this::workerSelected);
    }
    private void highlightOptions() {
        highlightPositions(gameState.getOptions(), this::optionSelected);
    }

    /**
     * Builds active player's status on the bottom left corner
     */
    private void fillActivePlayerPane(String color, String message) {
        String god = activePlayer.getCard().getName();
        activePlayerPane.getStyleClass().add("player-" + color);
        activePlayerPane.getChildren().get(1).setStyle(GodGraphics.getPodiumBG(god));
        ((Text) activePlayerPane.getChildren().get(2)).setText(god);
        ((Text) activePlayerPane.getChildren().get(3)).setText(message.toUpperCase());
    }
    private void fillActivePlayerPlaying(String message){
        fillActivePlayerPane(gameState.getPlayerColor(activePlayer).name().toLowerCase(), message);
    }
    private void fillActivePlayerWinner() {
        fillActivePlayerPane("winner", GUIClient.BA_WINNER);
    }
    private void fillActivePlayerLoser() {
        fillActivePlayerPane("loser", GUIClient.BA_LOSER);
    }

    /**
     * Adds a button to the statusButtons on the left
     * @param className class (or classes divided by a space) to be added
     * @param handler lambda function to call on mouse pressed, can be null
     */
    private void addStatusButton(String className, String backgroundProperty, EventHandler<MouseEvent> handler) {
        if(gameState.getCurrPlayer() != activePlayer)
            return;
        String[] classNames = className.split(" ");
        Pane button = new Pane();
        button.getStyleClass().add("status-button");
        button.getStyleClass().addAll(classNames);
        button.setStyle(backgroundProperty);
        if(handler != null) {
            button.getStyleClass().add("hover-effect-in");
            button.setOnMousePressed(handler);
        }
        statusButtons.getChildren().add(button);
    }
    private void addStatusButtonWrappers() {
        for(String god : activePlayer.getWrappers())
            addStatusButton("wrapper", GodGraphics.getWrapperButtonBG(god), null);
    }
    private void addStatusButtonChange() {
        addStatusButton("change-worker", "", e -> changeWorker());
    }
    private void addStatusButtonSkip() {
        addStatusButton("skip", GodGraphics.getSkipButtonBG(activePlayer.getCard().getName()), e -> skipState());
    }

    /**
     * Show the menu at the end of the game
     * @param victory true if the active player is the winner
     */
    private void showEndPane(boolean victory) {
        String message, className;
        if(victory) {
            message = GUIClient.BA_VICTORY;
            className = "winner-foreground";
            fillActivePlayerWinner();
        } else {
            message = GUIClient.BA_DEFEAT;
            className = "loser-foreground";
            fillActivePlayerLoser();
        }
        ((Text) endPane.getChildren().get(0)).setText(activePlayer.getUsername());
        ((Text) endPane.getChildren().get(1)).setText(message);
        endPane.getStyleClass().add(className);
        endPane.setVisible(true);
    }

    /**
     * Show the options to confirm an action during the game
     */
    private void showConfirmPane() {
        HBox buttons = (HBox) confirmPane.getChildren().get(1);
        Map<Boolean, String> images = GodGraphics.getConfirmButtonsBG(activePlayer.getCard().getName());
        buttons.getChildren().get(0).setStyle(images.get(true));
        buttons.getChildren().get(1).setStyle(images.get(false));
        confirmPane.setVisible(true);
    }

    /**
     * Displays parts of the gameState which don't depend on the request type
     */
    private void standardGameState() {
        gameState.getPlayers().forEach(this::addPlayerToList);
        gameState.getBoard().forEach(this::drawCell);
        addStatusButtonWrappers();
    }

    /**
     * Listener for a click on the change-worker status button
     */
    private void changeWorker() {
        if(isRequestSent())
            return;
        setRequestSent(true);
        System.out.println("Change worker button pressed!");
        getClient().validate("change");
    }

    /**
     * Listener for a click on the skip status button
     */
    private void skipState() {
        if(isRequestSent())
            return;
        setRequestSent(true);
        //System.out.println("Skip state button pressed!");
        getClient().validate("skip");
    }

    /**
     * Listener for a click on the confirm button true
     */
    @FXML
    private void confirmAction() {
        if(isRequestSent())
            return;
        setRequestSent(true);
        getClient().validate("Y");
    }

    /**
     * Listener for a click on the confirm button false
     */
    @FXML
    private void refuseAction() {
        if(isRequestSent())
            return;
        setRequestSent(true);
        getClient().validate("N");
    }

    /**
     * Listener for a click on a free cell (when choosing a place for the worker)
     * @param event click event on the StackPane
     */
    private void positionSelected(MouseEvent event) {
        if(isRequestSent())
            return;
        setRequestSent(true);
        StackPane cell = (StackPane) event.getSource();
        int row = GridPane.getRowIndex(cell);
        int col = GridPane.getColumnIndex(cell);
        getClient().validate(row+","+col);
    }

    /**
     * Listener for a click on a worker (when choosing the worker to use)
     * @param event click event on the StackPane
     */
    private void workerSelected(MouseEvent event) {
        if(isRequestSent())
            return;
        setRequestSent(true);
        StackPane cell = (StackPane) event.getSource();
        int row = GridPane.getRowIndex(cell);
        int col = GridPane.getColumnIndex(cell);
        getClient().validate(row+","+col);
    }

    /**
     * Listener for a click on a cell (when choosing where to perform an action)
     * @param event click event on the StackPane
     */
    private void optionSelected(MouseEvent event) {
        if(isRequestSent())
            return;
        setRequestSent(true);
        StackPane cell = (StackPane) event.getSource();
        int row = GridPane.getRowIndex(cell);
        int col = GridPane.getColumnIndex(cell);
        getClient().validate(row+","+col);
    }

    /**
     * Listener for a click on the play again button in the end pane
     */
    public void playAgain() {
        if(isRequestSent())
            return;
        setRequestSent(true);
        getClient().reset();
        getClient().updateScene(FXMLFile.LAUNCHER_PLAY, null);
    }

    /**
     * Listener for a click in the close game button in the end pane
     */
    public void closeGame() {
        if(isRequestSent())
            return;
        setRequestSent(true);
        GUIClient.window.close();
    }

    @Override
    public void updateUI(Request req) {
        getClient().updateScene(FXMLFile.BOARD, req);
    }

    @Override
    public void setupAttributes(Request req) {
        if (req.getType() != MessageType.INFO) {
            gameState = req.getBoard();
            setActivePlayer(req.getPlayer());
            standardGameState();
        }

        switch(req.getType()) {
            case FIRST_WORKER_PLACEMENT:
                fillActivePlayerPlaying(GUIClient.BA_PLACE_WORKER);
                highlightFreeCells();
                break;
            case CHOOSE_POSITION:
                ChoosePositionRequest req1 = (ChoosePositionRequest) req;
                if(req1.canChangeWorker())
                    addStatusButtonChange();
                if(req1.canBeSkipped())
                    addStatusButtonSkip();
                if(activePlayer.getState().equals("Move"))
                    fillActivePlayerPlaying(GUIClient.BA_MOVE_WORKER);
                else
                    fillActivePlayerPlaying(GUIClient.BA_BUILD_BLOCK);
                highlightOptions();
                break;
            case CHOOSE_WORKER:
                fillActivePlayerPlaying(GUIClient.BA_CHOOSE_WORKER);
                highlightWorkers();
                break;
            case CONFIRMATION:
                fillActivePlayerPlaying(GUIClient.BA_CONFIRM_MOVE);
                showConfirmPane();
                break;
            case REMOVE_PLAYER:
                RemovePlayerRequest req2 = (RemovePlayerRequest) req;
                if(req2.getTargetPlayer().equals(activePlayer.getUsername())) {
                    showEndPane(req2.isVictory());
                } else if(req2.isVictory()) {
                    showEndPane(false);
                } else if(!req2.isVictory() && !req2.getTargetPlayer().equals(activePlayer.getUsername())) {
                    AlertBox.displayError(GUIClient.AT_ENEMY_LOST, req2.getCustomMessage());
                }
                break;
            case START_TURN:
                getClient().validate("\n");
                break;
            case WAIT:
                fillActivePlayerPlaying(MessageFormat.format(GUIClient.BA_WAIT, gameState.getCurrPlayer().getCard()));
                break;
        }
    }
}
