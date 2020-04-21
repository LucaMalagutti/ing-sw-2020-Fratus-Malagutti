package it.polimi.ingsw.PSP4.server;

import it.polimi.ingsw.PSP4.observer.Observable;
import it.polimi.ingsw.PSP4.observer.Observer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class SocketClientConnection implements Observable<String>, Runnable {
    private Socket socket;
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

    public void closeConnection(String message) {
        send(message);
        closeConnection();
    }

    public void closeConnection() {
        send("Connection closed");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        active = false;
    }

    private void close() {
        closeConnection();
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
        send("Creating a new lobby to play a game as "+name);
        send("Choose the number of players for this game: (2) or (3). Default: 2");
        Scanner in = new Scanner(socket.getInputStream());
        String numPlayers = in.nextLine();
        while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("")) {
            send("Not a valid number of players. Type 2 or 3");
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
        send(alreadyTaken+" was already taken by another player. Select another one");
        return selectClientUsername();
    }

    /**
     * Asks the player to select a username. Performs length checks and removes whitespace
     * @return whitespace-stripped username
     */
    public String selectClientUsername() {
        send("Select your username:");
        try {
            Scanner in = new Scanner(socket.getInputStream());
            String name = in.nextLine().replaceAll("\\s", "");
            while (name.equals("") || name.length() > 15) {
                send("Username's length has to be between 1 and 15 characters. Try again: ");
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

//    public void discardScanner() {
//        discardingScanner = true;
//        new Thread(() -> {
//            Scanner in = null;
//            try {
//                in = new Scanner(socket.getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            while (discardingScanner && in != null && in.hasNextLine()) {
//                 lastLine = in.nextLine();
//            }
//        }).start();
//    }
//
//    public void stopDiscarding() {
//        if (discardingScanner) {
//            discardingScanner = false;
//        }
//    }
}
