package it.polimi.ingsw.PSP4.client;

import it.polimi.ingsw.PSP4.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Base class for a Client using a CLI UI.
 */
public class CLIClient {
    private final String ipAddress;
    private final int port;

    private boolean active = true;

    public CLIClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public synchronized boolean isActive() {return active;}
    public synchronized void setActive(boolean active) {this.active = active;}

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {
        Thread t = new Thread((Runnable) () -> {
            try {
                while(isActive()) {
                    Object inputObject = socketIn.readObject();
                    if (inputObject instanceof String) {
                        System.out.println(inputObject);
                    }
                    else if (inputObject instanceof Message) {
                        if (((Message) inputObject).getType() == MessageType.CHOOSE_ALLOWED_GODS) {
                            ChooseAllowedGodsMessage message = (ChooseAllowedGodsMessage) inputObject;
                            System.out.println(message.toString());
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
