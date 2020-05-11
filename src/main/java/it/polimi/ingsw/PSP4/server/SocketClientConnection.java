package it.polimi.ingsw.PSP4.server;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.PingRequest;
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
    private final int pongTimeout = pingInterval / 2;

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

    private synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void closeConnection(String message, boolean resetServer) {
        send(message);
        closeConnection(resetServer);
    }

    public void closeConnection(boolean resetServer) {
        send("Connection closed");
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

    public void asyncSend(Object message) {
        new Thread(() -> send(message)).start();
    }

    /**
     * Asks the first player in the lobby to select how many players will play this game
     * @param name name of the first player, i.e. the one who sets the number of players
     * @return number of players for this game
     */
    public int initializeGameNumPlayer(String name) throws IOException, ClassNotFoundException {
        send(MessageFormat.format(Message.CREATING_LOBBY, name));
        send(Message.CHOOSE_NUMBER_PLAYERS);
        String numPlayers = (String) in.readObject();

        while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("")) {
            send(Message.NOT_VALID_NUMBER);
            numPlayers = (String) in.readObject();
        }
        if (numPlayers.equals("")) {
            numPlayers = "2";
        }
        return Integer.parseInt(numPlayers);
    }

    /**
     * Overloading method
     */
    public String selectClientUsername(String alreadyTaken) {
        send(MessageFormat.format(Message.USERNAME_TAKEN, alreadyTaken));
        return selectClientUsername();
    }

    /**
     * Asks the player to select a username. Performs length checks and removes whitespace
     * @return whitespace-stripped username
     */
    public String selectClientUsername() {
        send(Message.CHOOSE_USERNAME);
        try {
            String name = (String) in.readObject();
            name = name.replaceAll("\\s", "");
            while (name.equals("") || name.length() > 15 || name.equals("@")) {
                if (name.equals("@")) {
                    send(Message.USERNAME_CHAR);
                } else {
                    send(Message.USERNAME_LENGTH);
                }
                name = (String) in.readObject();
                name = name.replaceAll("\\s", "");
            }
            return name;
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
                if (getLastTimestampReceived() != timestamp)
                    closeConnection("Pong timeout exceeded", true);
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
                //System.out.println("Sent ping @" + timestamp + " to " + username);
                asyncSend(new PingRequest(username, null, timestamp));
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
                if (read.getType() != MessageType.PING)
                    notifyObservers(read);
                else {
                    PingResponse pong = (PingResponse) read;
                    //System.out.println("Received pong " + pong.getTimestamp()  + " from " + name);
                    setLastTimestampReceived(pong.getTimestamp());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.getMessage();
        } finally {
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
