package it.polimi.ingsw.PSP4.utils;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.controller.turnStates.State;
import it.polimi.ingsw.PSP4.controller.turnStates.WaitState;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Collection of static methods that test the GameState
 */
public class Tests {
    /**
     * @return true if GameState has been initialized
     */
    public static boolean gameStateExists() {
        return GameState.getInstance() != null;
    }

    /**
     * @return true if GameState has been reset
     */
    public static boolean gameStateClean() {
        return  gameStateExists() &&
                boardEmpty() &&
                GameState.getInstance().getPlayers().size() == 0 &&
                GameState.getInstance().getCurrPlayer() == null &&
                numberOfPlayers(0) &&
                allowedGodsEmpty();
    }

    /**
     * @param exclusions list of coordinates to avoid
     * @return true if all positions, not excluded, have no workers
     */
    public static boolean boardNoWorkers(List<Coordinates> exclusions) {
        for(Position pos : GameState.getInstance().getFlatBoard()) {
            List<Coordinates> match = exclusions.stream().filter(coord -> coord.equals(pos)).collect(Collectors.toList());
            if(match.size() == 0 && pos.getWorker() != null)
                return false;
        }
        return true;
    }

    /**
     * @return true if all positions have no workers
     */
    public static boolean boardNoWorkers() {
        return boardNoWorkers(new ArrayList<>());
    }

    /**
     * @param exclusions list of coordinates to avoid
     * @return true if all positions, not excluded, have no buildings
     */
    public static boolean boardNoBuildings(List<Coordinates> exclusions) {
        for(Position pos : GameState.getInstance().getFlatBoard()) {
            List<Coordinates> match = exclusions.stream().filter(coord -> coord.equals(pos)).collect(Collectors.toList());
            if(match.size() == 0 && pos.getHeight() != 0)
                return false;
        }
        return true;
    }

    /**
     * @return true if all positions have no buildings
     */
    public static boolean boardNoBuildings() {
        return boardNoBuildings(new ArrayList<>());
    }

    /**
     * @return true if the board is empty (no workers, no buildings)
     */
    public static boolean boardEmpty() {
        return boardNoBuildings() && boardNoWorkers();
    }

    /**
     * @param input map {username, list of coordinates} to check
     * @return true if there are exactly those workers in those positions
     */
    public static boolean boardWorkers(Map<String, List<Coordinates>> input) {
        for (Player player : GameState.getInstance().getPlayers()) {
            List<Coordinates> expected = new ArrayList<>(input.get(player.getUsername()));
            for (Worker worker : player.getWorkers()) {
                Position pos = worker.getCurrPosition();
                List<Coordinates> actual = expected.stream().filter(coord -> coord.equals(pos)).collect(Collectors.toList());
                if(pos.getWorker() != worker || actual.size() != 1)
                    return false;
                expected.removeAll(actual);
            }
            if(expected.size() != 0)
                return false;
        }
        List<Coordinates> exclusions = input.values().stream().flatMap(List::stream).collect(Collectors.toList());
        return boardNoWorkers(exclusions);
    }

    /**
     * @param input map {height, list of coordinates} to check
     * @return true if there are exactly those buildings
     */
    public static boolean boardBuildings(Map<Integer, List<Coordinates>> input) {
        for (int height : input.keySet()) {
            for (Coordinates coord : input.get(height)) {
                Position position = GameState.getInstance().getPosition(coord.getRow(), coord.getCol());
                int posHeight = position.getHeight();
                if(position.hasDome())
                    posHeight = 4;
                if(posHeight != height)
                    return false;
            }
        }
        List<Coordinates> exclusions = input.values().stream().flatMap(List::stream).collect(Collectors.toList());
        return boardNoBuildings(exclusions);
    }

    /**
     * @param input map {username, god} to test
     * @return true if there are exactly those players with their gods
     */
    public static boolean godsAssignments(Map<String, String> input) {
        Map<String, String> playersList = new LinkedHashMap<>();
        for(Player player : GameState.getInstance().getPlayers())
            playersList.put(player.getUsername(), player.getMechanics().getType().getName());
        return playersList.equals(input);
    }

    /**
     * @param username expected current player
     * @return true if the current player is username
     */
    public static boolean currentPlayer(String username) {
        return GameState.getInstance().getCurrPlayer().getUsername().equals(username);
    }

    /**
     * @param numPlayer expected number of players
     * @return true if the number of players is numPlayer
     */
    public static boolean numberOfPlayers(int numPlayer) {
        return GameState.getInstance().getNumPlayer() == numPlayer;
    }

    /**
     * @return true if allowedGods is empty
     */
    public static boolean allowedGodsEmpty() {
        return GameState.getInstance().getAllowedGods().size() == 0;
    }

    /**
     * @return true if current player's current worker is unlocked and null, and if it's state is not WAIT
     */
    public static boolean newTurn() {
        Player currentPlayer = GameState.getInstance().getCurrPlayer();
        return !currentPlayer.isWorkerLocked() && currentPlayer.getCurrWorker() == null && !currentState(new WaitState(currentPlayer));
    }

    /**
     * @param coord expected coordinates of the current worker
     * @param locked if the current player's current worker is locked
     * @return true if the current player's current worker is in the right position and as defined by locked
     */
    public static boolean currentWorker(Coordinates coord, boolean locked) {
        Player currentPlayer = GameState.getInstance().getCurrPlayer();
        return coord.equals(currentPlayer.getCurrWorker().getCurrPosition()) && currentPlayer.isWorkerLocked() == locked;
    }

    /**
     * @param expected instance of the state expected for current player
     * @return true if current player's state is the same class as expected
     */
    public static boolean currentState(State expected) {
        State actual = GameState.getInstance().getCurrPlayer().getState();
        return actual.getClass().equals(expected.getClass());
    }

    /**
     * @param expected list of coordinates of the positions expected as current state options
     * @return true if there are exactly those options
     */
    public static boolean stateOptions(List<Coordinates> expected) {
        List<SerializablePosition> positions = GameState.getInstance().getCurrPlayer().getState().getOptions();
        for (SerializablePosition pos : positions) {
            List<Coordinates> actual = expected.stream().filter(coord -> coord.equals(pos)).collect(Collectors.toList());
            if(actual.size() != 1)
                return false;
            expected.removeAll(actual);
        }
        return expected.size() == 0;
    }

    /**
     * @param username username of the player from which the event started
     * @return true if each player, username excluded, is wrapped with his evil god
     */
    public static boolean enemiesWrapped(String username) {
        GodType god = Getters.player(username).getMechanics().getType();
        for(Player player : GameState.getInstance().getPlayers())
            if(!player.getUsername().equals(username) && !player.getMechanics().getEvilList().contains(god))
                return false;
        return true;
    }

    /**
     * @param username username of the player from which the event started
     * @return true if each player is not wrapped with username's evil god
     */
    public static boolean enemiesUnwrapped(String username) {
        GodType god = Getters.player(username).getMechanics().getType();
        for(Player player : GameState.getInstance().getPlayers()) {
            if (player.getMechanics().getEvilList().contains(god))
                return false;
        }
        return true;
    }
}
