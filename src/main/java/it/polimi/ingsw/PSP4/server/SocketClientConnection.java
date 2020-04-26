package it.polimi.ingsw.PSP4.server;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.observer.Observable;
import it.polimi.ingsw.PSP4.observer.Observer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class SocketClientConnection implements Observable<String>, Runnable {
    private final Socket socket;
    private ObjectOutputStream out;
    private final Server server;
    private boolean active = true;

    //used for Scanner discarding during wait for lobby setup
//    private volatile boolean discardingScanner = false;
//    private volatile String lastLine;

    private final ArrayList<Observer<String>> observers = new ArrayList<>();

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
    public int initializeGameNumPlayer(String name) throws IOException {
        send(MessageFormat.format(Message.CREATING_LOBBY, name));
        send(Message.CHOOSE_NUMBER_PLAYERS);
        Scanner in = new Scanner(socket.getInputStream());
        String numPlayers = in.nextLine();
        while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("")) {
            send(Message.NOT_VALID_NUMBER);
            numPlayers = in.nextLine();
        }
        if (numPlayers.equals("")) {
            numPlayers = "2";
        }
        return Integer.parseInt(numPlayers);
    }

    /**
     * Overloading method
     * @param alreadyTaken selected username that was already taken by another player
     * @return whitespace-stripped username
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
            Scanner in = new Scanner(socket.getInputStream());
            String name = in.nextLine().replaceAll("\\s", "");
            while (name.equals("") || name.length() > 15 || name.equals("@")) {
                if (name.equals("@")) {
                    send(Message.USERNAME_CHAR);
                } else {
                    send(Message.USERNAME_LENGTH);
                }
                name = in.nextLine().replaceAll("\\s", "");
            }
            return name;
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public void run() {
        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            String name = server.selectUsername(this);
            in = new Scanner(socket.getInputStream());
            server.lobby(this, name);
            while (isActive()) {
                String read = in.nextLine();
                notifyObservers(read);
            }
        } catch (IOException e) {
            e.getStackTrace();
        } finally {
            close();
        }
    }

    @Override
    public void addObserver(Observer<String> o) {
        synchronized (observers) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<String> o) {
        synchronized (observers) {
            observers.remove(o);
        }
    }

    @Override
    public void notifyObservers(String message) {
        synchronized (observers) {
            for (Observer<String> observer : observers) {
                observer.update(message);
            }
        }
    }
}
