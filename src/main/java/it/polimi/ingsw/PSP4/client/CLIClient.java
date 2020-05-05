package it.polimi.ingsw.PSP4.client;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Base class for a Client using a CLI UI.
 */
public class CLIClient {
    private String ipAddress = "127.0.0.1";     //ipAddress of the server to connect to. HARDCODED ONLY DURING DEVELOPMENT
    private final int port;                     //port of the server to connect to. Should be hardcoded in ClientMain
    private final Socket socket = new Socket();

    private boolean active = true;
    private Request lastRequestReceived;

    public CLIClient(int port) {
        this.port = port;
    }

    public synchronized boolean isActive() {return active;}
    public synchronized void setActive(boolean active) {this.active = active;}
    public synchronized void setLastRequestReceived(Request lastRequest) {this.lastRequestReceived = lastRequest;}
    public synchronized Request getLastRequestReceived() {return this.lastRequestReceived;}

    /**
     * Thread that handles the objects arriving from the socket the client is connected to
     * @param socketIn inputStream on the socket
     */
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

    /**
     * Thread that handles the sending of object to the socket
     * @param stdIn scanner the user writes on
     * @param socketOut outputStream to send the objects to
     */
    public Thread asyncWriteToSocket(final Scanner stdIn, final ObjectOutputStream socketOut) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    String inputLine = stdIn.nextLine();
                    if (!socket.isClosed()) {
                        if (getLastRequestReceived() == null) {
                            socketOut.writeObject(inputLine);
                        } else {
                            Message validated = getLastRequestReceived().validateResponse(inputLine);
                            if (validated.getType() == MessageType.ERROR)
                                System.out.println(validated.getMessage());
                            else
                                socketOut.writeObject(validated);
                        }
                        socketOut.flush();
                    }
                }
            } catch (Exception e) {
                setActive(false);
            }
        });
        t.start();
        return t;
    }

    /**
     * Asks the player to choose an IP address to connect to and validates it
     * @param stdIn stdInput to read from
     * @return InetSocketAddress with the chosen IPAddress and the hardcoded server prot
     */
    public InetSocketAddress chooseServerIP(Scanner stdIn) {
        System.out.println(Message.CHOOSE_SERVER_IP);
        String address = stdIn.nextLine();
        InetSocketAddress socketAddress;
        do {
            socketAddress = new InetSocketAddress(address, port);
            if (socketAddress.isUnresolved() || address.equals("")) {
                System.out.println(Message.NOT_VALID_SERVER_IP);
                address = stdIn.nextLine();
                socketAddress = new InetSocketAddress(address, port);
            }
        } while (socketAddress.isUnresolved() || address.equals(""));
        ipAddress = address;
        return socketAddress;
    }

    /**
     * Handles the connection to the server and sets up two threads, one receiving and the other writing on the socket.
     * @throws IOException in case something goes wrong during the connection or the users forces a client exit
     */
    public void run() throws IOException {
        Scanner stdIn = new Scanner(System.in);
        if (ipAddress == null) {
            try {
                InetSocketAddress socketAddress = chooseServerIP(stdIn);
                socket.connect(socketAddress, 3000);
            } catch (SocketTimeoutException e) {
                System.out.println(Message.CONNECTION_ATTEMPT_TIMED_OUT);
                chooseServerIP(stdIn);
            }
        } else {
            socket.connect(new InetSocketAddress(ipAddress, port));
        }
        System.out.println("Connection established");
        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        try {
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdIn, socketOut);
            t0.join();
            t1.join();
        } catch (InterruptedException | NoSuchElementException e) {
            System.out.println("Connection closed from client side");
        } finally {
            stdIn.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}
