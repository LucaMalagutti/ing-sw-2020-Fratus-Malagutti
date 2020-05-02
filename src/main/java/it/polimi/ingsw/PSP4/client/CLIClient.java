package it.polimi.ingsw.PSP4.client;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private Request lastRequestReceived;

    public CLIClient(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public synchronized boolean isActive() {return active;}
    public synchronized void setActive(boolean active) {this.active = active;}
    public synchronized void setLastRequestReceived(Request lastRequest) {this.lastRequestReceived = lastRequest;}

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {
        Thread t = new Thread(() -> {
            try {
                while(isActive()) {
                    Object inputObject = socketIn.readObject();
                    if (inputObject instanceof String) {
                        System.out.println(inputObject);
                    }
                    else if (inputObject instanceof Request) {
                        Request request = (Request) inputObject;
                        setLastRequestReceived(request);
                        System.out.println(request.toString());
                    }
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }

    public Thread asyncWriteToSocket(final Scanner stdIn, final ObjectOutputStream socketOut) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    String inputLine = stdIn.nextLine();
                    if (lastRequestReceived == null) {
                        socketOut.writeObject(inputLine);
                    } else {
                        Message validated = lastRequestReceived.validateResponse(inputLine);
                        if (validated.getType() == MessageType.ERROR)
                            System.out.println(validated.getMessage());
                        else
                            socketOut.writeObject(validated);
                    }
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
        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
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
