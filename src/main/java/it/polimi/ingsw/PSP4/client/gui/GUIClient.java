package it.polimi.ingsw.PSP4.client.gui;

import it.polimi.ingsw.PSP4.client.gui.sceneController.GUIController;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.PingRequest;
import it.polimi.ingsw.PSP4.message.requests.RemovePlayerRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Base class for the GUI Client
 */
public class GUIClient extends Application{
    public int port = 31713;
    private Socket socket;
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;
    private GUIController sceneController;
    private String username = null;
    private boolean connected = false;

    public static Stage window;

    private boolean active;
    private Request lastRequestReceived;
    private volatile long lastTimestamp = -1;

    public String getUsername() { return username; }
    public void setUsername(String username) { if(this.username == null) this.username = username; }
    public boolean isConnected() { return connected; }
    public void setConnected(boolean connected) { this.connected = connected; }
    public synchronized boolean isActive() {return active;}
    public synchronized void setActive(boolean active) {this.active = active;}
    public synchronized void setLastRequestReceived(Request lastRequest) {this.lastRequestReceived = lastRequest;}
    public synchronized Request getLastRequestReceived() { return this.lastRequestReceived; }

    /**
     * sets last Ping Timestamp. Starts serverConnectedCheck when first incoming ping
     * @param lastTimestamp timestamp to set
     */
    public synchronized void setLastTimestamp(long lastTimestamp) {
        if (this.lastTimestamp != -1) {
            this.lastTimestamp = lastTimestamp;
        } else {
            this.lastTimestamp = lastTimestamp;
            checkServerConnected();
        }
    }

    /**
     * Launches the GUI
     * @param args main args
     */
    public void run(String[] args) {
        launch(args);
    }

    /**
     * Attempts a connection to the server
     * @param address IP address of the server to connect to
     * @return boolean indicating if the connection attempt was successful
     */
    public boolean connectToServer(String address) {
        InetSocketAddress socketAddress = new InetSocketAddress(address, port);
        if (socketAddress.isUnresolved())
            return false;
        try {
            socket = new Socket();
            socket.connect(socketAddress, 2000);
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            socketIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            return false;
        }
        setActive(true);
        asyncReadFromSocket();
        return true;
    }

    /**
     * Reads and answers requests coming from the server
     */
    public void asyncReadFromSocket() {
        Thread t = new Thread(() -> {
            try {
                while(isActive()) {
                    Object inputObject = socketIn.readObject();
                    Request request = (Request) inputObject;
                    //System.out.println("READ "+request.getType());
                    if (request.getType() == MessageType.PING) {
                        Platform.runLater(() -> answerPing(request));
                    } else if (request.getType() == MessageType.REMOVE_PLAYER) {
                        RemovePlayerRequest req2 = (RemovePlayerRequest) request;
                        //enemy client exits
                        if (req2.getTargetPlayer().equals("@") && !req2.isVictory()) {
                            Platform.runLater(() -> {
                                if (!closeProgram(GUIClient.CM_PLAYER_LEFT)) {
                                    reset();
                                    updateScene(FXMLFile.LAUNCHER_PLAY, null);
                                }
                            });
                        } else {
                            Platform.runLater(() -> {
                                setLastRequestReceived(request);
                                sceneController.updateUI(request);
                            });
                        }
                    } else {
                        Platform.runLater(() -> {
                            setLastRequestReceived(request);
                            sceneController.updateUI(request);
                        });
                    }
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
    }

    /**
     * Sends an already validated request to the server
     * @param responseToSend Response to send to the server
     */
    public synchronized void asyncWriteToSocket(Message responseToSend) {
        Thread t = new Thread(() -> {
            try {
                socketOut.reset();
                socketOut.writeObject(responseToSend);
                socketOut.flush();
                //System.out.println("WRITE "+responseToSend.getType());
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
    }

    /**
     * Validates the last Request received
     * @param stringMessage string used to validate the request
     */
    public void validate(String stringMessage) {
        Message message = getLastRequestReceived().validateResponse(stringMessage);
        if (message!= null && message.getType() == MessageType.ERROR) {
            AlertBox.displayError("Error", message.getMessage());
            sceneController.setRequestSent(false);
        }
        else {
            asyncWriteToSocket(message);
        }
    }

    /**
     * Checks if the server sending pings, if not it closes the connection to the server
     */
    private void checkServerConnected() {
        int serverCheckTimeout = 20;
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis()/1000L - lastTimestamp > serverCheckTimeout && isActive()) {
                Platform.runLater(() -> {
                    setActive(false);
                    AlertBox.displayError(GUIClient.AT_SERVER_LOST, GUIClient.AM_SERVER_LOST);
                    window.close();
                });
                exec.shutdown();
            } else if (!isActive()) {
                exec.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Answers pings coming from the server, also initializes checkServerConnection()
     * @param pingRequest ping to be answered
     */
    private void answerPing(Request pingRequest) {
        PingRequest ping = (PingRequest) pingRequest;
        setLastTimestamp(ping.getTimestamp());
        new Thread (() -> {
            try {
                socketOut.reset();
                socketOut.writeObject(ping.validateResponse("PONG"));
                socketOut.flush();
            } catch (IOException e) {
                e.getMessage();
            }
        }).start();
    }

    /**
     * Sets up the first stage
     * @param stage stage to be displayed
     */
    @Override
    public void start(Stage stage) {
        window = stage;
        window.getIcons().add(new Image(GUIClient.class.getResourceAsStream("/images/icon.png")));
        window.setResizable(false);
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(GUIClient.CM_CLOSE_MESSAGE);
        });
        updateScene(FXMLFile.LAUNCHER_PLAY, null);
    }

    /**
     * Shows a ConfirmBox that asks the player if he wants to leave. Closes window on "yes"
     * @param closingMessage message to be displayed in the ConfirmBox
     * @return answer to 'want to close the game?' question
     */
    public boolean closeProgram(String closingMessage){
        boolean answer = ConfirmBox.displayConfirm(GUIClient.CT_CLOSE_MESSAGE, closingMessage);
        if(answer) {
            setActive(false);
            if (connected) {
                try {
                    socketIn.close();
                    socketOut.close();
                    socket.close();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
            window.close();
        }
        return answer;
    }

    /**
     * Loads a new scene onto the stage
     * @param file FXML file to be loaded
     * @param initReq requests used to populate the scene when it is loaded
     */
    public void updateScene(FXMLFile file, Request initReq) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(GUIClient.class.getResource("/scenes/" + file.getFileName()));
            root = loader.load();
            GUIController guiController = loader.getController();
            guiController.setClient(this);
            guiController.setupAttributes(initReq);
            sceneController = guiController;
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.displayError(GUIClient.AT_UNEXPECTED_ERROR, GUIClient.AM_UNEXPECTED_ERROR);
            window.close();
            return;
        }
        Scene scene = new Scene(root);
        if(file != FXMLFile.LOBBY_WAIT)
            window.setTitle(file.getWindowTitle());
        window.setScene(scene);
        window.show();
    }

    /**
     * Resets client to allow it to play a new game
     */
    public void reset() {
        connected = false;
        lastTimestamp = -1;
        username = null;
        lastRequestReceived = null;
        socket = new Socket();
    }

    public static String WINDOW_TITLE_LAUNCHER = "Launcher - Santorini";
    public static String WINDOW_TITLE_LOBBY = "Lobby - Santorini";
    public static String WINDOW_TITLE_GAME = "Santorini";

    //Alerts
    public static String AT_ENEMY_LOST = "Enemy player lost";
    public static String AT_GAME_STARTED = "Game Already Started";
    public static String AM_GAME_STARTED = "A game has already started. Try again later!";
    public static String AT_SERVER_LOST = "Server Lost";
    public static String AM_SERVER_LOST = "Lost connection to the server. Exiting..";
    public static String AT_UNEXPECTED_ERROR = "Unexpected error";
    public static String AM_UNEXPECTED_ERROR = "We are sorry, something is not working";

    //Confirmations
    public static String CT_CLOSE_MESSAGE = "Closing Time";
    public static String CM_CLOSE_MESSAGE = "Are you sure you want to leave Santorini?";
    public static String CM_PLAYER_LEFT = "A player has unexpectedly left the game. Do you want to leave Santorini?";

    //Lobby Actions
    public static String LA_GOD_SELECTION = "SELECT {0} GOD{1}";

    //Board Actions
    public static String BA_BUILD_BLOCK = "Build a block";
    public static String BA_CHOOSE_WORKER = "Choose a worker";
    public static String BA_CONFIRM_MOVE = "Confirm your move";
    public static String BA_DEFEAT = "DEFEAT";
    public static String BA_LOSER = "Loser";
    public static String BA_MOVE_WORKER = "Move your worker";
    public static String BA_PLACE_WORKER = "Place a worker";
    public static String BA_VICTORY = "VICTORY";
    public static String BA_WAIT = "Wait for {0}";
    public static String BA_WINNER = "Winner";

    //Errors
    public static String CONNECTION_REFUSED = "There was a problem connecting to the server. Try again!";
    public static String IP_EMPTY = "Server IP field cannot be empty";
    public static String PLAYER_NOT_FOUND = "Error, this player is not in the official players list";
    public static String UNEXPECTED = "Unexpected {0} {1}";
    public static String UNEXPECTED_REQUEST = "Unexpected request";
    public static String USERNAME_LENGTH = "Username must be between 1 and 15 characters";
    public static String USERNAME_RESERVED = "Username must be different from '@'";
}
