package it.polimi.ingsw.PSP4.client.gui;

import it.polimi.ingsw.PSP4.client.gui.sceneController.GUIController;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.AssignFirstWorkerPlacementRequest;
import it.polimi.ingsw.PSP4.message.requests.PingRequest;
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
    private final Socket socket = new Socket();
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;
    private GUIController sceneController;
    private String username = null;
    private boolean connected = false;

    public static Stage window;

    private boolean active = true;
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
    public synchronized void setLastTimestamp(long lastTimestamp) {
        if (this.lastTimestamp != -1) {
            this.lastTimestamp = lastTimestamp;
        } else {
            this.lastTimestamp = lastTimestamp;
            serverCheck();
        }
    }

    public void validate(String stringMessage) {
        Message message = getLastRequestReceived().validateResponse(stringMessage);
        if (message.getType() == MessageType.ERROR) {
            AlertBox.displayError("Error", message.getMessage());
        }
        else {
            asyncWriteToSocket(message);
        }
    }

    public void run(String[] args) {
        launch(args);
    }

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
        asyncReadFromSocket();
        return true;
    }

    public void asyncReadFromSocket() {
        Thread t = new Thread(() -> {
            try {
                while(isActive()) {
                    Object inputObject = socketIn.readObject();
                    Request request = (Request) inputObject;
                    System.out.println("READ "+request.getMessage());
                    if (request.getType() == MessageType.PING) {
                        Platform.runLater(() -> answerPing(request));
                    }
                    else {
                        Platform.runLater(() -> {
                            setLastRequestReceived(request);
                            sceneController.updateUI(request);
                        });
                    }
                }
            } catch (Exception e) {
                setActive(false);
            } finally {
                try {
                    socketIn.close();
                    socketOut.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Platform.runLater(()-> window.close());
                }
            }
        });
        t.start();
    }

    //The corresponding request should have already been validated externally, this method should only be used to send a response
    public void asyncWriteToSocket(Message responseToSend) {
        Thread t = new Thread(() -> {
            try {
                socketOut.reset();
                socketOut.writeObject(responseToSend);
                socketOut.flush();
                System.out.println("WRITE "+responseToSend.getType());
            } catch (Exception e) {
                setActive(false);
                e.printStackTrace();
            }
        });
        t.start();
    }

    private void serverCheck() {
        int serverCheckTimeout = 20;
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis()/1000L - lastTimestamp > serverCheckTimeout) {
                Platform.runLater(() -> {
                    setActive(false);
                    AlertBox.displayError("Server Lost", "Lost connection to the server");
                    closeProgram();
                });
                exec.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

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


    @Override
    public void start(Stage stage) {
        window = stage;
        window.getIcons().add(new Image(GUIClient.class.getResourceAsStream("/images/icon.png")));
        window.setTitle("Launcher - Santorini");
        window.setResizable(false);
        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        updateScene(FXMLFile.LAUNCHER_PLAY, null, false);
        //To debug the BOARD scene, please don't remove
//        updateScene(FXMLFile.BOARD, new AssignFirstWorkerPlacementRequest("Lorenzo", 0, 0), false);
    }

    public void closeProgram(){
        boolean answer = ConfirmBox.displayConfirm("We'll miss you", "Are you sure you want to leave Santorini?");
        if(answer) {
            setActive(false);
            window.close();
        }
    }

    public void updateScene(FXMLFile file, Request initReq, boolean fullScreen) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(GUIClient.class.getResource("/scenes/" + file.getFileName()));
            root = loader.load();
            GUIController guiController = loader.getController();
            guiController.setupAttributes(initReq);
            guiController.setClient(this);
            sceneController = guiController;
        } catch (Exception e) {
            e.printStackTrace();
            AlertBox.displayError("Unexpected error", "We are sorry, something is not working");
            window.close();
            return;
        }
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.setFullScreen(fullScreen);
        window.show();
    }

}
