package it.polimi.ingsw.PSP4.server;

import it.polimi.ingsw.PSP4.controller.Controller;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.view.RemoteView;
import it.polimi.ingsw.PSP4.view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private final static int port = 31713;
    private final ServerSocket socket;
    private final ExecutorService executor = Executors.newFixedThreadPool(64);
    private final Map<SocketClientConnection, String> waitingConnections = new LinkedHashMap<>();
    private final Map<SocketClientConnection, Player> playingConnections = new LinkedHashMap<>();

    private int numPlayers;                                                 //number of playing clients
    private SocketClientConnection numPlayersSetter;                        //first client to connect to the lobby
    private final ArrayList<String> usernamesTaken = new ArrayList<>();

    private void setNumPlayers(int numPlayers) { this.numPlayers = numPlayers; }

    public Server() throws IOException {
        this.socket = new ServerSocket(port);
    }

    /**
     * Removes a connection after its client exits
     * @param c connection to be unregistered
     */
    public synchronized void unregisterConnection(SocketClientConnection c) {
        usernamesTaken.remove(waitingConnections.get(c));
        playingConnections.remove(c);
        waitingConnections.remove(c);
        //Handles first client disconnection when the game is still in lobby phase
        if (c == numPlayersSetter && waitingConnections.size() > 0) {
            numPlayers = 0;
            numPlayersSetter = null;
            for (SocketClientConnection connection: waitingConnections.keySet()) {
                connection.closeConnection("The lobby creator has left.");
            }
            waitingConnections.clear();
        }
    }

    /**
     * Asks the client to provide a username. Checks its uniqueness
     * @param c client that has to provide a unique username
     * @return unique username selected. Will be used as a parameter for later lobby() call
     */
    public String selectUsername(SocketClientConnection c) {
        synchronized (usernamesTaken) {
            String selectedUsername = c.selectClientUsername();
            while (usernamesTaken.contains(selectedUsername) || selectedUsername.equals("")) {
                selectedUsername = c.selectClientUsername(selectedUsername);
            }
            usernamesTaken.add(selectedUsername);
            return selectedUsername;
        }
    }

    /**
     * Handles lobby phase of the yet to start game on this server
     * @param c connection entering lobby
     * @param name username of the incoming client
     */
    public synchronized void lobby(SocketClientConnection c, String name) {
        //drops connections started with an ongoing game
        if (playingConnections.size() > 0) {
            c.closeConnection("A game has already started. Try again later!");
        }
        waitingConnections.put(c, name);
        if (waitingConnections.size() == 1) {
            numPlayersSetter = c;
            setNumPlayers(c.initializeGameNumPlayer());
        }
        //When the number of waiting players is reached, initializes GameState and its dependencies and starts the game
        if (waitingConnections.size() == numPlayers) {
            GameState.getInstance(true);
            for (SocketClientConnection connection: waitingConnections.keySet()) {
                Player player = new Player(waitingConnections.get(connection));
                GameState.getInstance().addPlayer(player);
                if (GameState.getInstance().getPlayers().size() == 1) {
                    GameState.getInstance().setCurrPlayer(player);
                }
                View playerView = new RemoteView(player, connection);
                Controller controller = new Controller();
                GameState.getInstance().addObserver(playerView);
                playerView.addObserver(controller);
                playingConnections.put(connection, player);
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