package it.polimi.ingsw.PSP4.client.cli;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.MessageType;
import it.polimi.ingsw.PSP4.message.requests.PingRequest;
import it.polimi.ingsw.PSP4.message.requests.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Base class for a Client using a CLI UI.
 */
public class CLIClient {
    private String ipAddress = "127.0.0.1";     //ipAddress of the server to connect to. HARDCODED ONLY DURING DEVELOPMENT
    private final Socket socket = new Socket();
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;

    private boolean active = true;
    private Request lastRequestReceived;
    private volatile long lastTimestamp = -1;

    public synchronized boolean isActive() {return active;}
    public synchronized void setActive(boolean active) {this.active = active;}
    public synchronized void setLastRequestReceived(Request lastRequest) {this.lastRequestReceived = lastRequest;}
    public synchronized Request getLastRequestReceived() {return this.lastRequestReceived;}

    public synchronized void setLastTimestamp(long lastTimestamp) {
        if (this.lastTimestamp != -1) {
            this.lastTimestamp = lastTimestamp;
        } else {
            this.lastTimestamp = lastTimestamp;
            serverCheck();
        }
    }

    /**
     * Creates a thread that check continuously if the server keeps sending pings, otherwise closes the connection
     */
    private void serverCheck() {
        int serverCheckTimeout = 20;
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis()/1000L - lastTimestamp > serverCheckTimeout) {
                setActive(false);
                System.out.println("Lost connection to the server. Press ENTER to exit.");
                exec.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Sends back a pong to a ping request
     * @param pingRequest request to be answered
     */
    private void answerPing(Request pingRequest) {
        PingRequest ping;
        ping = (PingRequest) pingRequest;
        setLastTimestamp(ping.getTimestamp());
        new Thread (() -> {
            try {
                socketOut.reset();
                socketOut.writeObject(ping.validateResponse("PONG"));
                socketOut.flush();
            } catch (IOException e) {
                e.getMessage();
            }
        }).start();
    }

    /**
     * Creates a thread that handles the objects arriving from the socket the client is connected to
     */
    public Thread asyncReadFromSocket() {
        Thread t = new Thread(() -> {
            try {
                while(isActive()) {
                    Object inputObject = socketIn.readObject();
                    Request request = (Request) inputObject;
                    if (request.getType() == MessageType.PING)
                        answerPing(request);
                    else {
                        if (request.getType() != MessageType.INFO)
                            setLastRequestReceived(request);
                        if (isActive())
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
     * Creates a thread that handles the sending of objects to the socket
     * @param stdIn scanner the user writes on
     */
    public Thread asyncWriteToSocket(final Scanner stdIn) {
        Thread t = new Thread(() -> {
            try {
                while (isActive()) {
                    String inputLine = stdIn.nextLine();
                    Message validated = getLastRequestReceived().validateResponse(inputLine);
                    if (validated.getType() == MessageType.ERROR && isActive())
                        System.out.println(validated.getMessage());
                    else
                        socketOut.writeObject(validated);
                    socketOut.flush();
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
     * @return InetSocketAddress with the chosen IPAddress and the hardcoded server port
     */
    public InetSocketAddress chooseServerIP(Scanner stdIn, int port) {
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
        int port = 31713;
        Scanner stdIn = new Scanner(System.in);
        if (ipAddress == null) {
            try {
                InetSocketAddress socketAddress = chooseServerIP(stdIn, port);
                socket.connect(socketAddress, 3000);
            } catch (SocketTimeoutException e) {
                System.out.println(Message.CONNECTION_ATTEMPT_TIMED_OUT);
                chooseServerIP(stdIn, port);
            }
        } else {
            socket.connect(new InetSocketAddress(ipAddress, port));
        }
        System.out.println("Connection established");
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());
        try {
            Thread t0 = asyncReadFromSocket();
            Thread t1 = asyncWriteToSocket(stdIn);
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
