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
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Base class for a Client using a CLI UI.
 */
public class CLIClient {
    private Socket socket;
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;

    private boolean active = true;
    private Request lastRequestReceived;
    private volatile long lastTimestamp = -1;

    public synchronized boolean isActive() {return active;}
    public synchronized void setActive(boolean active) {this.active = active;}
    public synchronized void setLastRequestReceived(Request lastRequest) {this.lastRequestReceived = lastRequest;}
    public synchronized Request getLastRequestReceived() {return this.lastRequestReceived;}

    /**
     * sets last Ping Timestamp. Starts serverConnectedCheck when first incoming ping
     * @param lastTimestamp timestamp to set
     */
    public synchronized void setLastTimestamp(long lastTimestamp) {
        if (this.lastTimestamp != -1) {
            this.lastTimestamp = lastTimestamp;
        } else {
            this.lastTimestamp = lastTimestamp;
            serverConnectedCheck();
        }
    }

    /**
     * Creates a thread that check continuously if the server keeps sending pings, otherwise closes the connection
     */
    private void serverConnectedCheck() {
        int serverCheckTimeout = 20;
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis()/1000L - lastTimestamp > serverCheckTimeout) {
                setActive(false);
                System.out.println("Lost connection to the server. Press ENTER to exit.");
            }
            else if (!isActive()) {
                exec.shutdown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Sends back a pong to a ping request
     * @param pingRequest request to be answered
     */
    private void answerPing(Request pingRequest) {
        String pingResponseString = "PONG";
        PingRequest ping;
        ping = (PingRequest) pingRequest;
        setLastTimestamp(ping.getTimestamp());
        new Thread (() -> {
            try {
                synchronized (socketOut) {
                    socketOut.reset();
                    socketOut.writeObject(ping.validateResponse(pingResponseString));
                    socketOut.flush();
                }
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
                    synchronized (socketOut) {
                        if (validated.getType() == MessageType.ERROR && isActive())
                            System.out.println(validated.getMessage());
                        else {
                            socketOut.reset();
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
     * @return InetSocketAddress with the chosen IPAddress and the hardcoded server port
     */
    public InetSocketAddress chooseServerIP(Scanner stdIn, int port) {
        System.out.println(Message.CHOOSE_SERVER_IP);
        String address = stdIn.nextLine();
        return new InetSocketAddress(address, port);
    }

    /**
     * Handles the connection to the server and sets up two threads, one receiving and the other writing on the socket.
     */
    public void run() {
        int port = 31713;
        int socketTimeout = 3000;
        String connectionClosed = "Connection closed from client side";
        Scanner stdIn = new Scanner(System.in);
        boolean connectionAttemptSucceeded = false;
        while (!connectionAttemptSucceeded) {
            try {
                InetSocketAddress socketAddress = chooseServerIP(stdIn, port);
                if (!socketAddress.isUnresolved()) {
                    socket = new Socket();
                    socket.connect(socketAddress, socketTimeout);
                    socketOut = new ObjectOutputStream(socket.getOutputStream());
                    socketIn = new ObjectInputStream(socket.getInputStream());
                    connectionAttemptSucceeded = true;
                } else {
                    System.out.println(Message.NOT_VALID_SERVER_IP);
                }
            } catch (IOException e) {
                System.out.println(Message.CONNECTION_ATTEMPT_TIMED_OUT);
            }
        }
        try {
            Thread t0 = asyncReadFromSocket();
            Thread t1 = asyncWriteToSocket(stdIn);
            t0.join();
            t1.join();
        } catch (InterruptedException | NoSuchElementException e) {
            System.out.println(connectionClosed);
        } finally {
            try {
                stdIn.close();
                socketIn.close();
                socketOut.close();
                socket.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }
}
