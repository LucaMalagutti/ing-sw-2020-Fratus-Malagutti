package it.polimi.ingsw.PSP4.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    private final String ipAddress;
    private final int port;

    private boolean active = true;

    private String clientUI;

    public Client(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public synchronized boolean isActive() {return active;}

    public synchronized void setActive(boolean active) {this.active = active;}

    private void setClientUI(String clientUI) {this.clientUI = clientUI;}

    /**
     * Asks the user (repeatedly) to choose which kind of UI he wants to use during the game
     * @param stdIn buffer from command line to input the UI chosen
     * @return String representing the kind of UI
     */
    private String chooseClientUI(final Scanner stdIn) {
        String inputLine;
        do {
            System.out.println("Choose your graphical interface:\n(Type \"GUI\" or \"CLI\")");
            inputLine = stdIn.nextLine().toUpperCase();
        } while (!inputLine.equals("CLI") && !inputLine.equals("GUI"));
        System.out.println("You have chosen \""+inputLine+"\" as your UI for the game.");
        return inputLine;
    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {
        Thread t = new Thread((Runnable) () -> {
            try {
                while(isActive()) {
                    Object inputObject = socketIn.readObject();
                    if(inputObject instanceof String) {
                        System.out.println((String) inputObject);
                    }
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }

    public Thread asyncWriteToSocket(final Scanner stdIn, final PrintWriter socketOut) {
        Thread t = new Thread((Runnable) () -> {
            try {
                while (isActive()) {
                    String inputLine = stdIn.nextLine();
                    socketOut.println(inputLine);
                    socketOut.flush();
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }

    public void run() throws IOException {
        Socket socket = new Socket(ipAddress, port);
        System.out.println("Connection established");

        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdIn = new Scanner(System.in);
        setClientUI(chooseClientUI(stdIn));
        try {
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdIn, socketOut);
            t0.join();
            t1.join();
        } catch (InterruptedException |NoSuchElementException e) {
            System.out.println("Connection closed from client side");
        } finally {
            stdIn.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}
