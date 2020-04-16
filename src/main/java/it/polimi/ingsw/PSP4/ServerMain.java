package it.polimi.ingsw.PSP4;

import it.polimi.ingsw.PSP4.server.Server;

import java.io.IOException;

public class ServerMain {
    public static void main( String[] args ) {
        Server server;
        try {
            server = new Server();
            server.run();
        } catch(IOException e) {
            e.getMessage();
        }
    }
}
