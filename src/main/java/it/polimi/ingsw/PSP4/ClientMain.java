package it.polimi.ingsw.PSP4;

import it.polimi.ingsw.PSP4.client.Client;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 31713);
        try {
            client.run();
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
}
