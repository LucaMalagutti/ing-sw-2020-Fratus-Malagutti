package it.polimi.ingsw.PSP4.client.gui;

import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.PingRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;
import it.polimi.ingsw.PSP4.message.responses.Response;
import javafx.application.Application;
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
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GUIClient extends Application{
    private String ipAddress = "127.0.0.1";     //ipAddress of the server to connect to. HARDCODED ONLY DURING DEVELOPMENT
    private final Socket socket = new Socket();
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;

    private boolean active = true;
    private Request lastRequestReceived;
    private volatile long lastTimestamp = -1;

    public synchronized boolean isActive() {return active;}
    public synchronized void setActive(boolean active) {this.active = active;}
    public synchronized void setLastRequestReceived(Request lastRequest) {this.lastRequestReceived = lastRequest;}
    public synchronized Request getLastRequestReceived() {return this.lastRequestReceived;}

    public synchronized void setLastTimestamp(long lastTimestamp) {
        if (this.lastTimestamp != -1) {
            this.lastTimestamp = lastTimestamp;
        } else {
            this.lastTimestamp = lastTimestamp;
            serverCheck();
        }
    }

    static Stage window;

    public void run(String[] args, int port) throws IOException {
        //TODO implement ipAddress selection
        socket.connect(new InetSocketAddress(ipAddress, port));
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());
        try {
            Thread t0 = asyncReadFromSocket();
            launch(args);
            t0.join();
        } catch (InterruptedException | NoSuchElementException e) {
            System.out.println("Connection closed from client side");
        } finally {
            socketIn.close();
            socketOut.close();
            socket.close();
        }
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

    public Thread asyncReadFromSocket() {
        Thread t = new Thread(() -> {
            try {
                while(isActive()) {
                    Object inputObject = socketIn.readObject();
                    if (inputObject instanceof String) {
                        //TODO Display on screen
                    }
                    else if (inputObject instanceof Request) {
                        Request request = (Request) inputObject;
                        if (request.getType() == MessageType.PING) {
                            answerPing(request);
                        }
                        else {
                            setLastRequestReceived(request);
                            if (isActive()) {
                                //TODO send message to objects involved
                            }
                        }
                    }
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }

    //The corresponding request should have already been validated externally, this method should only be used to send a response
    public Thread asyncWriteToSocket(final Response responseToSend) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    socketOut.reset();
                    socketOut.writeObject(responseToSend);
                    socketOut.flush();
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }

    private void serverCheck() {
        int serverCheckTimeout = 20;
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis()/1000L - lastTimestamp > serverCheckTimeout) {
                setActive(false);
                //TODO show alert box indicating that the connection was lost, then close the window
                exec.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
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

        GUIClient.updateScene(FXMLFile.LAUNCHER_PLAY, false);
    }

    public void closeProgram(){
        boolean answer = ConfirmBox.displayConfirm("We'll miss you", "Are you sure you want to leave Santorini?");
        if(answer)
            window.close();
    }

    public static void updateScene(FXMLFile file, boolean fullScreen) {
        Parent root;
        try {
            root = FXMLLoader.load(GUIClient.class.getResource("/scenes/" + file.getFileName()));
        } catch (Exception e) {
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
