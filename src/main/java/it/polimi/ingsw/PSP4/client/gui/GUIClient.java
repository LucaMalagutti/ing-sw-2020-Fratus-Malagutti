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
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GUIClient extends Application{
    public int port = 31713;
    private Socket socket = new Socket();
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

    public void run(String[] args) {
        launch(args);
    }

    /**
     * Attempts a connection to the server
     * @param address IP address of the server to connect to
     * @return boolean indicating if the connection attempt was successful
     * @throws IOException socket exceptions
     */
    public boolean connectToServer(String address) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress(address, port);
        if (socketAddress.isUnresolved())
            return false;
        try {
            socket.connect(socketAddress, 3000);
        } catch (SocketTimeoutException e) {
            return false;
        }
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());
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
                                closeProgram(Message.CLIENT_EXIT_DURING_GAME);
                                reset();
                                updateScene(FXMLFile.LAUNCHER_PLAY, null);
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
        if (message.getType() == MessageType.ERROR) {
            AlertBox.displayError("Error", message.getMessage());
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
                    AlertBox.displayError("Server Lost", "Lost connection to the server. Exiting..");
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
        window.setTitle("Launcher - Santorini");
        window.setResizable(false);
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram("Are you sure you want to leave Santorini?");
        });
        updateScene(FXMLFile.LAUNCHER_PLAY, null);
        //To debug the BOARD scene, please don't remove
//        updateScene(FXMLFile.BOARD, new AssignFirstWorkerPlacementRequest("Lorenzo", 0, 0));
    }

    /**
     * Shows a ConfirmBox that asks the player if he wants to leave. Closes window on "yes"
     * @param closingMessage message to be displayed in the ConfirmBox
     */
    public void closeProgram(String closingMessage){
        boolean answer = ConfirmBox.displayConfirm("Closing Time", closingMessage);
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
            AlertBox.displayError("Unexpected error", "We are sorry, something is not working");
            window.close();
            return;
        }
        Scene scene = new Scene(root);
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
}
