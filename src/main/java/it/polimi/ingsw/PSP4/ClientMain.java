package it.polimi.ingsw.PSP4;

import it.polimi.ingsw.PSP4.client.CLIClient;
import it.polimi.ingsw.PSP4.client.GUIClient;

import java.io.IOException;
import java.util.Scanner;

/**
 * Initializes Client
 */
public class ClientMain {
    /**
     * Asks the user (repeatedly) to choose which kind of UI he wants to use during the game
     * @param stdIn buffer from command line to input the UI chosen
     * @return String representing the kind of UI
     */
    private static String chooseClientUI(final Scanner stdIn) {
        String inputLine;
        do {
            System.out.println("Choose your graphical interface:\n(Type \"GUI\" or \"CLI\", Default: CLI)");
            inputLine = stdIn.nextLine().toUpperCase();
        } while (!inputLine.equals("CLI") && !inputLine.equals("GUI") && !inputLine.equals(""));
        if (inputLine.equals("")) {
            inputLine = "CLI";
        }
        System.out.println("You have chosen \""+inputLine+"\" as your UI for the game.");
        return inputLine;
    }

    public static void main(String[] args) {
        try {
            Scanner stdIn = new Scanner(System.in);
            String clientUI = chooseClientUI(stdIn);
            if (clientUI.equals("CLI")) {
                CLIClient client = new CLIClient("127.0.0.1", 31713);
                client.run();
            } else if (clientUI.equals("GUI")){
                GUIClient client = new GUIClient();
                System.out.println("GUI was not implemented yet :))))");
//                client.run();
            } else {
                System.out.println("Couldn't select a valid UI. Exiting..");
            }
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }
}
