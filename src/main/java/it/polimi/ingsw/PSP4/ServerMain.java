package it.polimi.ingsw.PSP4;

import it.polimi.ingsw.PSP4.server.Server;

import java.io.IOException;

/**
 * Initializes Server
 */
public class ServerMain {
    /**
     * Creates and runs a Santorini server
     * @param args command line arguments
     */
    public static void main( String[] args ) {
        Server server;
        try {
            server = new Server();
            System.out.println("Running the Santorini server. Press CTRL+C to quit.");
            server.run();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
