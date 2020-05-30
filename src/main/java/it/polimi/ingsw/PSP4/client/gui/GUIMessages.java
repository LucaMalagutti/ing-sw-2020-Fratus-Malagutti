package it.polimi.ingsw.PSP4.client.gui;

public abstract class GUIMessages {
    public static final String WINDOW_TITLE_LAUNCHER = "Launcher - Santorini";
    public static final String WINDOW_TITLE_LOBBY = "Lobby - Santorini";
    public static final String WINDOW_TITLE_GAME = "Santorini";
    public static final String WINDOW_TITLE_INFO = "Info";
    public static final String WINDOW_TITLE_ERROR = "Error";

    //Alerts
    public static final String AT_ENEMY_LOST = "Enemy player lost";
    public static final String AT_GAME_STARTED = "Game Already Started";
    public static final String AM_GAME_STARTED = "A game has already started. Try again later!";
    public static final String AT_SERVER_LOST = "Server Lost";
    public static final String AM_SERVER_LOST = "Lost connection to the server. Exiting..";
    public static final String AT_UNEXPECTED_ERROR = "Unexpected error";
    public static final String AM_UNEXPECTED_ERROR = "We are sorry, something is not working";

    //Confirmations
    public static final String CT_CLOSE_MESSAGE = "Closing Time";
    public static final String CM_CLOSE_MESSAGE = "Are you sure you want to leave Santorini?";
    public static final String CM_PLAYER_LEFT = "A player has unexpectedly left the game. Do you want to leave Santorini?";

    //Lobby Actions
    public static final String LA_GOD_SELECTION = "SELECT {0} GOD{1}";

    //Board Actions
    public static final String BA_BUILD_BLOCK = "Build a block";
    public static final String BA_CHOOSE_WORKER = "Choose a worker";
    public static final String BA_CONFIRM_MOVE = "Confirm your move";
    public static final String BA_DEFEAT = "DEFEAT";
    public static final String BA_LOSER = "Loser";
    public static final String BA_MOVE_WORKER = "Move your worker";
    public static final String BA_PLACE_WORKER = "Place a worker";
    public static final String BA_VICTORY = "VICTORY";
    public static final String BA_WAIT = "Wait for {0}";
    public static final String BA_WINNER = "Winner";

    //Errors
    public static final String CONNECTION_REFUSED = "There was a problem connecting to the server. Try again!";
    public static final String IP_EMPTY = "Server IP field cannot be empty";
    public static String PLAYER_NOT_FOUND = "Error, this player is not in the official players list";
    public static final String UNEXPECTED = "Unexpected {0} {1}";
    public static final String UNEXPECTED_REQUEST = "Unexpected request";
    public static final String USERNAME_LENGTH = "Username must be between 1 and 15 characters";
    public static final String USERNAME_RESERVED = "Username must be different from '@'";
}
