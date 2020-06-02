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
    private Socket socket;
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;
    private GUIController sceneController;
    private String username = null;
    private boolean connected = false;
    private ScheduledExecutorService serverConnectionCheckExecutor;

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
    public ScheduledExecutorService getServerConnectionCheckExecutor() { return serverConnectionCheckExecutor; }

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
        int socketTimeout = 2000;
        int port = 31713;
        InetSocketAddress socketAddress = new InetSocketAddress(address, port);
        if (socketAddress.isUnresolved())
            return false;
        try {
            socket = new Socket();
            socket.connect(socketAddress, socketTimeout);
            socketOut = new ObjectOutputStream(socket.getOutputStream());
            socketIn = new ObjectInputStream(socket.getInputStream());
            serverConnectionCheckExecutor = Executors.newSingleThreadScheduledExecutor();
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
//                    System.out.println("READ "+request.getType());
                    if (request.getType() == MessageType.PING) {
                        Platform.runLater(() -> answerPing(request));
                    } else if (request.getType() == MessageType.REMOVE_PLAYER) {
                        RemovePlayerRequest req2 = (RemovePlayerRequest) request;
                        //enemy client exits
                        if (req2.getTargetPlayer().equals("@") && !req2.isVictory()) {
                            Platform.runLater(() -> {
                                if (!closeProgram(GUIMessages.CM_PLAYER_LEFT)) {
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
//                System.out.println("WRITE "+responseToSend.getType());
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
            AlertBox.displayError(GUIMessages.WINDOW_TITLE_ERROR, message.getMessage());
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
        int serverCheckTimeout = 11;
        serverConnectionCheckExecutor.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis()/1000L - lastTimestamp > serverCheckTimeout) {
                Platform.runLater(() -> {
                    setActive(false);
                    serverConnectionCheckExecutor.shutdown();
                    AlertBox.displayError(GUIMessages.AT_SERVER_LOST, GUIMessages.AM_SERVER_LOST);
                    window.close();
                });
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Answers pings coming from the server, also initializes checkServerConnection()
     * @param pingRequest ping to be answered
     */
    private void answerPing(Request pingRequest) {
        PingRequest ping = (PingRequest) pingRequest;
        String pingRequestResponseString = "PONG";
        setLastTimestamp(ping.getTimestamp());
        asyncWriteToSocket(ping.validateResponse(pingRequestResponseString));
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
            closeProgram(GUIMessages.CM_CLOSE_MESSAGE);
        });
        updateScene(FXMLFile.LAUNCHER_PLAY, null);
    }

    /**
     * Shows a ConfirmBox that asks the player if he wants to leave. Closes window on "yes"
     * @param closingMessage message to be displayed in the ConfirmBox
     * @return answer to 'want to close the game?' question
     */
    public boolean closeProgram(String closingMessage){
        boolean answer = ConfirmBox.displayConfirm(GUIMessages.CT_CLOSE_MESSAGE, closingMessage);
        if(answer) {
            setActive(false);
            if (connected) {
                try {
                    serverConnectionCheckExecutor.shutdown();
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
            AlertBox.displayError(GUIMessages.AT_UNEXPECTED_ERROR, GUIMessages.AM_UNEXPECTED_ERROR);
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
        if (serverConnectionCheckExecutor != null)
            serverConnectionCheckExecutor.shutdown();
        connected = false;
        lastTimestamp = -1;
        username = null;
        lastRequestReceived = null;
        socket = new Socket();
    }
}
