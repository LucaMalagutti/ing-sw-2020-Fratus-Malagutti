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
    private ServerSocket socket;
    private ExecutorService executor = Executors.newFixedThreadPool(64);
    private final Map<String, SocketClientConnection> waitingConnections = new LinkedHashMap<>();
    private final Map<SocketClientConnection, Player> playingConnections = new LinkedHashMap<>();

    private void setNumPlayers(int numPlayers) { this.numPlayers = numPlayers; }

    private int numPlayers;

    public Server() throws IOException {
        this.socket = new ServerSocket(port);
    }

    public synchronized void unregisterConnection(SocketClientConnection c) {
        playingConnections.remove(c);
        waitingConnections.keySet().removeIf(s -> waitingConnections.get(s) == c);
    }

    public synchronized void lobby(SocketClientConnection c, String name) {
        waitingConnections.put(name, c);
        if (waitingConnections.size() == 1) {
            setNumPlayers(c.initializeGameNumPlayer());
        }
        if (waitingConnections.size() == numPlayers) {
            List<String> keys = new ArrayList<>(waitingConnections.keySet());
            ArrayList<Player> players = new ArrayList<>();
            for (int i = 0; i < numPlayers; i++) {
                SocketClientConnection connection = waitingConnections.get(keys.get(i));
                Player player = new Player(keys.get(i));
                players.add(player);
                View playerView = new RemoteView(player, connection);
                Controller controller = new Controller(GameState.getInstance());
                GameState.getInstance().addObserver(playerView);
                playerView.addObserver(controller);
                playingConnections.put(connection, player);
                if (i == 0) {
                    GameState.getInstance().setCurrPlayer(player);
                }
            }
            GameState.getInstance().setNumPlayer(numPlayers);
            GameState.getInstance().setPlayers(players);
            GameState.getInstance().startGame();
            waitingConnections.clear();
        }
    }

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
