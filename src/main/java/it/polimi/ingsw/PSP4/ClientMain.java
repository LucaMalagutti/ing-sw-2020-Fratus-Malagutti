package it.polimi.ingsw.PSP4;

import it.polimi.ingsw.PSP4.client.cli.CLIClient;
import it.polimi.ingsw.PSP4.client.gui.GUIClient;
import it.polimi.ingsw.PSP4.message.Message;

import java.text.MessageFormat;
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
            System.out.println(Message.CHOOSE_INTERFACE);
            inputLine = stdIn.nextLine().toUpperCase();
        } while (!inputLine.equals("CLI") && !inputLine.equals("GUI") && !inputLine.equals(""));
        if (inputLine.equals("")) {
            inputLine = "GUI";
        }
        System.out.println(MessageFormat.format(Message.CHOSEN_INTERFACE, inputLine));
        return inputLine;
    }

    public static void main(String[] args) {
        Scanner stdIn = new Scanner(System.in);
        String clientUI = chooseClientUI(stdIn);
        if (clientUI.equals("CLI")) {
            CLIClient client = new CLIClient();
            client.run();
        } else if (clientUI.equals("GUI")){
            GUIClient client = new GUIClient();
            client.run(args);
        } else {
            System.out.println(Message.NOT_VALID_UI);
        }
    }
}
