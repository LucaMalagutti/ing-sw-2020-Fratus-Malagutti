package it.polimi.ingsw.PSP4.utils;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.Worker;

import java.util.*;

/**
 * Class of static methods used to get attributes from the GameState
 */
public class Getters {
    /**
     * @return current number of players
     */
    public static int numberOfPlayers() {
        return GameState.getInstance().getNumPlayer();
    }

    /**
     * @return list of player usernames
     */
    public static List<String> playerList() {
        List<String> players = new ArrayList<>();
        for(Player player : GameState.getInstance().getPlayers())
            players.add(player.getUsername());
        return players;
    }

    /**
     * @return map {username, god} currently assigned
     */
    public static Map<String, String> godsAssignments() {
        Map<String, String> gods = new LinkedHashMap<>();
        for(Player player : GameState.getInstance().getPlayers())
            gods.put(player.getUsername(), player.getMechanics().getType().getName());
        return gods;
    }

    /**
     * @return username of the current player
     */
    public static String currentPlayer() {
        return GameState.getInstance().getCurrPlayer().getUsername();
    }

    /**
     * @return map {username, list of coordinates} of workers currently on the board
     */
    public static Map<String, List<Coordinates>> workersOnBoard() {
        Map<String, List<Coordinates>> workersMap = new LinkedHashMap<>();
        for(Player player : GameState.getInstance().getPlayers()) {
            List<Coordinates> workers = new ArrayList<>();
            for(Worker worker : player.getWorkers())
                workers.add(new Coordinates(worker.getCurrPosition()));
            workersMap.put(player.getUsername(), workers);
        }
        return workersMap;
    }

    /**
     * @return map {height, list of coordinates} of buildings currently on the board
     * (Height 4 includes any dome)
     */
    public static Map<Integer, List<Coordinates>> buildingsOnBoard() {
        Map<Integer, List<Coordinates>> buildingsMap = new LinkedHashMap<>();
        for(Position position : GameState.getInstance().getFlatBoard()) {
            int height = position.getHeight();
            if(position.hasDome())
                height = 4;
            if(height > 0) {
                List<Coordinates> existing = buildingsMap.get(position.getHeight());
                if (existing == null)
                    buildingsMap.put(position.getHeight(), Collections.singletonList(new Coordinates(position)));
                else {
                    List<Coordinates> updated = new ArrayList<>(existing);
                    updated.add(new Coordinates(position));
                    buildingsMap.replace(position.getHeight(), updated);
                }
            }
        }
        return buildingsMap;
    }
}
