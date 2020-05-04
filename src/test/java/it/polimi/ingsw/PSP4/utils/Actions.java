package it.polimi.ingsw.PSP4.utils;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.AthenaEnemyGameMechanics;
import it.polimi.ingsw.PSP4.controller.cardsMechanics.GameMechanics;
import it.polimi.ingsw.PSP4.message.responses.*;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Collection of static methods that modify the GameState
 */
public class Actions {

    /**
     * Removes any building from the board
     */
    public static void demolishBoard() {
        List<Position> positions = GameState.getInstance().getFlatBoard();
        for(Position position : positions) {
            position.setDome(false);
            position.setHeight(0);
        }
    }

    /**
     * Removes any existing building from the board, then builds those in input
     * @param input map {height, list of coordinates} to build
     */
    public static void fillBoard(Map<Integer, List<Coordinates>> input) {
        demolishBoard();
        for(Integer height : input.keySet()) {
            for(Coordinates coord : input.get(height)) {
                Position position = GameState.getInstance().getPosition(coord.getRow(), coord.getCol());
                position.setHeight(height);
                if(height == 4)
                    position.setDome(true);
            }
        }
    }

    /**
     * Add a new Player for each username
     * Set the first player of the list as current
     * Set the usernames size as numPlayers
     * @param usernames list of usernames
     */
    public static void addPlayers(List<String> usernames) {
        for (String username : usernames) {
            Player player = new Player(username);
            GameState.getInstance().addPlayer(player);
            if(username.equals(usernames.get(0)))
                GameState.getInstance().setCurrPlayer(player);
        }
        GameState.getInstance().setNumPlayer(usernames.size());
    }

    /**
     * Start the game
     * Set allowed gods as input.values()
     * Assign each god to its player
     * @param input map {username, god to assign}
     */
    public static void assignGods(Map<String, String> input) {
        GameState.getInstance().startGame();

        Player startingPlayer = GameState.getInstance().getCurrPlayer();
        new ChooseAllowedGodsResponse(startingPlayer.getUsername(), new ArrayList<>(input.values())).handle();

        Player currentPlayer;
        do {
            currentPlayer = GameState.getInstance().getCurrPlayer();
            String selected = input.remove(currentPlayer.getUsername());
            new AssignGodResponse(currentPlayer.getUsername(), new ArrayList<>(input.values()), selected).handle();
        } while(currentPlayer != startingPlayer);
    }

    /**
     * Set username as starting player
     * @param username name of the player which starts
     */
    public static void selectStartingPlayer(String username) {
        String current = GameState.getInstance().getCurrPlayer().getUsername();
        new ChooseStartingPlayerResponse(current, username).handle();
    }

    /**
     * Set workers in places
     * @param input map {coordinates, username} to place
     */
    public static void placeWorkers(Map<String, List<Coordinates>> input) {
        for (int numPlayer = 0; numPlayer < GameState.getInstance().getNumPlayer(); numPlayer++) {
            Player currentPlayer = GameState.getInstance().getCurrPlayer();
            List<Coordinates> coordList = input.get(currentPlayer.getUsername());
            for(int numWorker = 0; numWorker < coordList.size(); numWorker++) {
                Coordinates coord = coordList.get(numWorker);
                SerializablePosition selected = new SerializablePosition(GameState.getInstance().getPosition(coord.getRow(), coord.getCol()));
                new AssignFirstWorkerPlacementResponse(currentPlayer.getUsername(), selected, numPlayer, numWorker).handle();
            }
        }
    }

    /**
     * Set current player's current worker
     * @param coord coordinates of the worker to choose
     */
    public static void setCurrentWorker(Coordinates coord) {
        Player currentPlayer = GameState.getInstance().getCurrPlayer();
        new StartTurnResponse(currentPlayer.getUsername()).handle();
        new ChooseWorkerResponse(currentPlayer.getUsername(), new int[] { coord.getRow(), coord.getCol() }).handle();
    }

    /**
     * Change current player's current worker
     * @param coord coordinates of the new worker to choose
     */
    public static void changeCurrentWorker(Coordinates coord) {
        Player currentPlayer = GameState.getInstance().getCurrPlayer();
        new ChangeWorkerResponse(currentPlayer.getUsername()).handle();
        new ChooseWorkerResponse(currentPlayer.getUsername(), new int[] { coord.getRow(), coord.getCol() }).handle();
    }

    /**
     * Skip current player's state
     */
    public static void skipCurrentState() {
        Player currentPlayer = GameState.getInstance().getCurrPlayer();
        new SkipStateResponse(currentPlayer.getUsername()).handle();
    }

    /**
     * Select a position to perform an action
     * @param coord coordinates of the selected position
     */
    public static void selectOption(Coordinates coord) {
        Player currentPlayer = GameState.getInstance().getCurrPlayer();
        SerializablePosition selected = new SerializablePosition(GameState.getInstance().getPosition(coord.getRow(), coord.getCol()));
        new ChoosePositionResponse(currentPlayer.getUsername(), selected).handle();
    }

    /**
     * Confirm or refuse the selected position
     * @param coord coordinates of the selected position
     * @param confirmed if the position must be confirmed
     */
    public static void confirmPosition(Coordinates coord, boolean confirmed) {
        Player currentPlayer = GameState.getInstance().getCurrPlayer();
        SerializablePosition selected = new SerializablePosition(GameState.getInstance().getPosition(coord.getRow(), coord.getCol()));
        new ConfirmationResponse(currentPlayer.getUsername(), selected, confirmed).handle();
    }


    /**
     * Wrap a player with a god
     * @param username username of the player to wrap
     * @param god name of the god to wrap around the player
     */
    public static void wrapPlayer(String username, String god) {
        Player player = Getters.player(username);
        GameMechanics mechanics = player.getMechanics();
        if(!mechanics.getName().equals(god) && god.equals("Athena_Enemy"))
            player.setMechanics(new AthenaEnemyGameMechanics(mechanics));
    }

    /**
     * Unwrap a player from a god
     * @param username username of the player to unwrap
     * @param god name of the god around the player
     */
    public static void unwrapPlayer(String username, String god) {
        Player player = Getters.player(username);
        GameMechanics mechanics = player.getMechanics();
        if(mechanics.getName().equals(god) && god.equals("Athena_Enemy"))
            player.setMechanics(mechanics.getComponent());
    }
}
