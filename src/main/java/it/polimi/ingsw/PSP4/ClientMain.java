package it.polimi.ingsw.PSP4;

import it.polimi.ingsw.PSP4.client.cli.CLIClient;
import it.polimi.ingsw.PSP4.client.gui.GUIClient;

/**
 * Initializes Client
 */
public class ClientMain {
    /**
     * Initializes a CLI or a GUI client
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("cli")) {
            CLIClient client = new CLIClient();
            client.run();
        } else {
            GUIClient client = new GUIClient();
            client.run(args);
        }
    }
}
