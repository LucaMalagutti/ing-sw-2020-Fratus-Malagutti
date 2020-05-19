package it.polimi.ingsw.PSP4.server;

import it.polimi.ingsw.PSP4.controller.Controller;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.requests.InfoRequest;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.view.RemoteView;
import it.polimi.ingsw.PSP4.view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final static int port = 31713;
    private final ServerSocket socket;
    private final ExecutorService executor = Executors.newFixedThreadPool(64);

    private final Map<SocketClientConnection, String> waitingConnections = new LinkedHashMap<>();
    private final Map<SocketClientConnection, Player> playingConnections = new LinkedHashMap<>();

    private int numPlayers = -1;                                        //number of playing clients
    private SocketClientConnection firstClientConnected;                //first client to connect to the lobby
    private final List<String> usernamesTaken = Collections.synchronizedList(new ArrayList<>());

    private synchronized void setNumPlayers(int numPlayers) { this.numPlayers = numPlayers; }

    public Server() throws IOException {
        this.socket = new ServerSocket(port);
    }

    /**
     * Removes a connection from the server after its client exits with an IOException. Handles the server state
     * @param c connection to be unregistered
     */
    public synchronized void unregisterConnection(SocketClientConnection c) {
        //Handles first client disconnection when the game is still in lobby phase. Drops all clients.
        if (c == firstClientConnected && waitingConnections.size() > 0) {
            for (SocketClientConnection connection: waitingConnections.keySet()) {
                connection.closeConnection(Message.LOBBY_CREATOR_LEFT, false);
            }
            this.reset();
        }
        //Handles a client disconnection when the game has started. Drops all clients and resets the server state
        else if (playingConnections.size() > 0) {
            GameState.getInstance().dropAllConnections();
            this.reset();
        }
        else {
            usernamesTaken.remove(waitingConnections.get(c));
            playingConnections.remove(c);
            waitingConnections.remove(c);
        }
    }

    /**
     * resets the Server object so its original state, so that another lobby can be created to play a new game
     */
    protected synchronized void reset() {
        numPlayers = -1;
        firstClientConnected = null;
        usernamesTaken.clear();
        waitingConnections.clear();
        playingConnections.clear();
    }

    /**
     * Asks the client to provide a username. Checks its uniqueness
     * @param c client that has to provide a unique username
     * @return unique username selected. Will be used as a parameter for later lobby() call
     */
    public String selectUsername(SocketClientConnection c) {
        if (firstClientConnected != null && numPlayers == -1) {
            c.asyncSend(new InfoRequest(null, Message.WAIT_LOBBY_SETUP));
        }
        synchronized (this) {
            try {
                while(firstClientConnected != null && numPlayers == -1) {
                    wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String selectedUsername = c.selectClientUsername();
        while (usernamesTaken.contains(selectedUsername) || selectedUsername.equals("")) {
            selectedUsername = c.selectClientUsername(selectedUsername);
        }
        usernamesTaken.add(selectedUsername);
        return selectedUsername;
    }

    /**
     * Handles lobby phase of the yet to start game on this server
     * @param c connection entering lobby
     * @param name username of the incoming client
     */
    public synchronized void lobby(SocketClientConnection c, String name) {
        //drops connections started with an ongoing game
        if (playingConnections.size() > 0) {
            c.closeConnection(Message.GAME_ALREADY_STARTED, false);
        }
        waitingConnections.put(c, name);
        if (waitingConnections.size() == 1) {
            firstClientConnected = c;
            try {
                setNumPlayers(c.initializeGameNumPlayer(name));
                c.asyncSend(new InfoRequest(name, Message.WAIT_PLAYERS));
            } catch (Exception e) {
                e.printStackTrace();
                unregisterConnection(c);
            } finally {
                notifyAll();
            }
        }
        else {
            c.asyncSend(new InfoRequest(name, MessageFormat.format(Message.ENTERING_LOBBY, name)));
        }
        //When the number of waiting players is reached, initializes GameState and its dependencies and starts the game
        if (waitingConnections.size() == numPlayers) {
            GameState.getInstance(true);
            Controller controller = new Controller();
            for (SocketClientConnection connection: waitingConnections.keySet()) {
                Player player = new Player(waitingConnections.get(connection));
                GameState.getInstance().addPlayer(player);
                if (GameState.getInstance().getPlayers().size() == 1) {
                    GameState.getInstance().setCurrPlayer(player);
                }
                View playerView = new RemoteView(player, connection);
                GameState.getInstance().addObserver(playerView);
                playerView.addObserver(controller);
                playingConnections.put(connection, player);
                //connection.asyncSend(new InfoRequest(name, Message.GAME_STARTING));
            }
            GameState.getInstance().setNumPlayer(numPlayers);
            GameState.getInstance().startGame();
            waitingConnections.clear();
        }
    }

    /**
     * Accepts a new connection on receiving Socket and creates corresponding SocketClientConnection on another thread
     */
    public void run() {
        while (true) {
            try {
                Socket newSocket = socket.accept();
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }
}