package it.polimi.ingsw.PSP4.server;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.*;
import it.polimi.ingsw.PSP4.message.responses.ChooseNumPlayersResponse;
import it.polimi.ingsw.PSP4.message.responses.ChooseUsernameResponse;
import it.polimi.ingsw.PSP4.message.responses.PingResponse;
import it.polimi.ingsw.PSP4.message.responses.Response;
import it.polimi.ingsw.PSP4.observer.Observable;
import it.polimi.ingsw.PSP4.observer.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.System.*;

public class SocketClientConnection implements Observable<Response>, Runnable {
    private final ArrayList<Observer<Response>> observers = new ArrayList<>();
    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final Server server;
    private boolean active = true;

    private final int pingInterval = 8;
    private final int pongTimeout = pingInterval - 2;

    private long lastTimestampReceived;

    private synchronized void setLastTimestampReceived(long timestamp) {this.lastTimestampReceived = timestamp;}
    public synchronized long getLastTimestampReceived() { return lastTimestampReceived; }
    private synchronized boolean isActive(){
        return active;
    }

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized void send(Request message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void closeConnection(String message, boolean resetServer) {
        send(new InfoRequest(null, message));
        closeConnection(resetServer);
    }

    public void closeConnection(boolean resetServer) {
        send(new InfoRequest(null, "Connection Closed"));
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        active = false;
        if (resetServer)
            server.reset();
    }

    private void close() {
        closeConnection(false);
        System.out.println("Unregistering client from server");
        server.unregisterConnection(this);
        System.out.println("Connection unregistered from server");
    }

    public void asyncSend(Request message) {
        new Thread(() -> send(message)).start();
    }

    /**
     * Asks the first player in the lobby to select how many players will play this game
     * @param username username of the first player, i.e. the one who sets the number of players
     * @return number of players for this game
     */
    public int initializeGameNumPlayer(String username) throws IOException, ClassNotFoundException {
        send(new InfoRequest(username, MessageFormat.format(Message.CREATING_LOBBY, username)));
        send(new ChooseNumPlayersRequest(username));
        Response numPlayersResponse;
        do {
           numPlayersResponse = (Response) in.readObject();
        }
        while (numPlayersResponse.getType() != MessageType.CHOOSE_NUM_PLAYERS);
        ChooseNumPlayersResponse chooseNumPlayersResponse = (ChooseNumPlayersResponse) numPlayersResponse;
        return chooseNumPlayersResponse.getSelectedNumPlayers();
    }

    /**
     * Overloading method
     */
    public String selectClientUsername(String alreadyTaken) {
        send(new InfoRequest(null, MessageFormat.format(Message.USERNAME_TAKEN, alreadyTaken)));
        return selectClientUsername();
    }

    /**
     * Asks the player to select a username. Performs length checks and removes whitespace
     * @return whitespace-stripped username
     */
    public String selectClientUsername() {
        send(new ChooseUsernameRequest());
        try {
            Response response = (Response) in.readObject();
            if (response.getType() == MessageType.CHOOSE_USERNAME) {
                ChooseUsernameResponse usernameResponse = (ChooseUsernameResponse) response;
                return usernameResponse.getSelectedUsername();
            }
            else {
                return "USERNAME_ERROR";
            }
        } catch (IOException | ClassNotFoundException e) {
            return e.getMessage();
        }
    }

    /**
     * Creates a thread that checks that a corresponding pong arrives before a timeout, otherwise it closes the connection
     * Called every time that ping is sent to the connected client.
     */
    public void checkPingTimeout(long timestamp){
        new Thread(() -> {
            try {
                Thread.sleep(pongTimeout*1000);
                if (getLastTimestampReceived() != timestamp && isActive()) {
                    System.out.println("CLOSING CLIENT CONNECTION TIMEOUT");
                    close();
                }
            } catch (InterruptedException e) {
                closeConnection(true);
            }
        }).start();
    }

    /**
     * Creates a thread that sends a ping to the connected client every this.pingInterval seconds
     * Should be called after that a player has joined the server lobby
     * @param username username of the player using the connected client
     */
    public void clientPing(String username) {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            if (isActive()) {
                long timestamp = currentTimeMillis() / 1000L;
                System.out.println("Sent ping @" + timestamp + " to " + username);
                send(new PingRequest(username, null, timestamp));
                checkPingTimeout(timestamp);
            } else {
                exec.shutdown();
            }
        }, 0, pingInterval, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            String name = server.selectUsername(this);
            server.lobby(this, name);
            clientPing(name);
            while (isActive()) {
                Response read = (Response) in.readObject();
                if (read.getType() == MessageType.PING) {
                    PingResponse pong = (PingResponse) read;
                    System.out.println("Received pong " + pong.getTimestamp() + " from " + name);
                    setLastTimestampReceived(pong.getTimestamp());
                }
                else {
                    notifyObservers(read);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.getMessage();
        } finally {
            if (isActive())
                close();
        }
    }

    @Override
    public void addObserver(Observer<Response> o) {
        synchronized (observers) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<Response> o) {
        synchronized (observers) {
            observers.remove(o);
        }
    }

    @Override
    public void notifyObservers(Response message) {
        synchronized (observers) {
            for (Observer<Response> observer : observers) {
                observer.update(message);
            }
        }
    }
}
