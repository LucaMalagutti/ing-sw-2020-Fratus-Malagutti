package it.polimi.ingsw.PSP4.message;

import java.io.Serializable;

/**
 * Message base class
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = -2598699184641521335L;

    private final String player;        //player receiving the message (use all to broadcast)
    private final String message;       //message string to be displayed
    private final MessageType type;     //type of the message

    //getter and setter
    public String getPlayer() { return this.player; }
    public String getMessage() { return this.message; }
    public MessageType getType() { return type; }

    /**
     * Constructor of the class Message
     * @param player username of the sender/receiver
     * @param message text message from the sender
     * @param type type of the message
     */
    public Message(String player, String message, MessageType type) {
        this.player = player;
        this.message = message;
        this.type = type;
    }

    //Default messages
    public static String ATLAS_BUILD = "Do you want to build a dome in {0}? (Y or N)";
    public static String CHANGE_WORKER_COMMAND = "\nWrite \"change\" to change worker.";
    public static String CHOOSE_ALLOWED_GODS = "Select {0} gods from this list:";
    public static String CHOOSE_GOD = "Select your god from the following list:";
    public static String CHOOSE_INTERFACE = "Choose your graphical interface:\n(Type \"GUI\" or \"CLI\", Default: CLI)";
    public static String CHOOSE_NUMBER_PLAYERS = "Choose the number of players for this game: (2) or (3). Default: 2";
    public static String CHOOSE_POSITION_BUILD = "Select a position to build a new level from the following list.";
    public static String CHOOSE_POSITION_MOVE = "Select a position to move your worker from the following list.";
    public static String CHOOSE_SERVER_IP = "Type a server IP to connect to:";
    public static String CHOOSE_STARTING_PLAYER = "Choose the first player to start the game from the following list:";
    public static String CHOOSE_USERNAME = "Select your username:";
    public static String CHOOSE_WORKER = "Select a worker from the following list of positions:";
    public static String CHOSEN_INTERFACE = "You have chosen \"{0}\" as your UI for the game.";
    public static String CREATING_LOBBY = "Creating a new lobby to play a game as {0}";
    public static String ENTERING_LOBBY = "Entering lobby as {0}";
    public static String FIRST_PLACE_WORKER = "Select the first placement on the board of one of your workers\nType two coordinates separated by a comma";
    public static String GAME_ALREADY_STARTED = "A game has already started. Try again later!";
    //public static String GAME_STARTED = "The game has started with {0} players.";
    public static String GAME_STARTING = "\nSTARTING A NEW SANTORINI GAME\n";
    public static String HEPHAESTUS_BUILD = "Do you want to build two blocks in {0}? (Y or N)";
    public static String LAST_ASSIGNED_GOD = "As the last player to choose, you have been assigned {0}. Press Enter to continue";
    public static String LOBBY_CREATOR_LEFT = "The lobby creator has left.";
    public static String SKIP_STATE_COMMAND = "\nWrite \"skip\" to skip this action.";
    public static String WAIT_END_TURN = "Wait for {0} to finish his turn\n";
    public static String WAIT_LOBBY_SETUP = "Wait for the first player to set up the lobby";
    public static String WAIT_PLAYERS = "\nWaiting for the other players to join your lobby";
    public static String YOUR_TURN = "It's your turn! Press Enter to start";

    //Default errors
    public static String CLIENT_EXIT_DURING_GAME = "A player has unexpectedly left the game. Exiting..";
    public static String CONNECTION_ATTEMPT_TIMED_OUT = "The connection attempt timed out. Try again!";
    public static String NOT_VALID_CONFIRM = "Not a valid confirmation. Type \"Y\" or \"N\".";
    public static String NOT_VALID_GOD_LIST = "Not a valid god list. Try again with valid god names separated by a single space.";
    public static String NOT_VALID_GOD_LIST_DUPLICATES = "The god list can't contain duplicates. Try again with valid god names separated by a single space.";
    public static String NOT_VALID_GOD_NAME = "Not a valid god name. Try again.";
    public static String NOT_VALID_PLAYER_NUMBER = "Not a valid number of players. Type 2 or 3";
    public static String NOT_VALID_POSITION = "{0} is not a valid position. Try again.";
    public static String NOT_VALID_SERVER_IP = "This IP address is invalid. Try again";
    public static String NOT_VALID_UI = "Couldn't select a valid UI. Exiting..";
    public static String NOT_VALID_USERNAME = "{0} is not a valid player name. Try again.";
    public static String NOT_VALID_WORKER = "{0} is not a valid worker position. Try again.";
    public static String NOT_YOUR_TURN = "There is a time and place for everything, but not now.";
    public static String USERNAME_LENGTH = "Username's length has to be between 1 and 15 characters. Try again:";
    public static String USERNAME_TAKEN = "{0} was already taken by another player. Select another one";
    public static String USERNAME_CHAR = "\"@\" is not a valid username. Try again:";

    //Default causes for game win or loss
    public static String NO_OPTIONS = "{0} ended up in a dead end. No options available.\n";
    public static String WIN_CONDITION_SATISFIED = "{0} satisfied the win condition!\n";
    public static String WORKERS_STUCK = "{0} has both workers stuck. {0} can't complete this turn!\n";

    //Default game over messages
    public static String VICTORY_LOSER = "Unfortunately, {0} won the game! Bow before the new sovereign of SANTORINI!";
    public static String VICTORY_WINNER = "Congratulations {0}, you won the game! You are the new sovereign of SANTORINI!";
    public static String DEFEAT_LOSER = "We are sorry {0}, you are out of the game!";
    public static String DEFEAT_ENEMY = "Good news! {0} is out of the game!";

    /**
     * Used to switch between CLI represented row, as String, during display phase and row saved in Position, as int
     * @param rowLetter Letter to switch to a storable int
     * @return coordinate as number (to be parsed with ParseInt)
     */
    public String coordinateLetterToInt(String rowLetter) {
        switch (rowLetter) {
            case "a":
            case "A":
                return "0";
            case "b":
            case "B":
                return "1";
            case "c":
            case "C":
                return "2";
            case "d":
            case "D":
                return "3";
            case "e":
            case "E":
                return "4";
        }
        return rowLetter;
    }

    /**
     * Used to switch between row saved in Position, as int and CLI represented row, as String, during display phase
     * @param rowInt Int row coordinate to be displayed as letter
     * @return row coordinate as single letter
     */
    public String coordinateIntToLetter(int rowInt) {
        switch (rowInt) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
            case 4:
                return "E";
        }
        return String.valueOf(rowInt);
    }
}
