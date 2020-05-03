package it.polimi.ingsw.PSP4.utils;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class of static methods to get random values
 */
public class Random {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * @return random number of player (2 or 3)
     */
    public static int numberOfPlayers() {
        return (int) Math.round(Math.random()) + 2;
    }

    /**
     * @param numPlayer number of players
     * @return list of random usernames
     */
    public static List<String> playerList(int numPlayer) {
        List<String> players = new ArrayList<>();
        while(players.size() != numPlayer) {
            StringBuilder sb = new StringBuilder();
            int length = (int) Math.round(Math.random() * 14) + 1;
            while(length-- != 0) {
                int index = (int) (Math.random() * ALPHABET.length());
                sb.append(ALPHABET.charAt(index));
            }
            String username = sb.toString();
            if(!players.contains(username))
                players.add(username);
        }
        return players;
    }

    /**
     * @param players list of usernames
     * @return map {username, god} with random associations
     */
    public static Map<String, String> godsAssignments(List<String> players) {
        List<String> gods = GodType.getImplementedGodsList();
        Map<String, String> godsMap = new LinkedHashMap<>();
        for(String player : players) {
            int index = (int) (Math.random() * gods.size());
            String godName = gods.remove(index);
            godsMap.put(player, godName.substring(0, 1).toUpperCase() + godName.substring(1).toLowerCase());
        }
        return godsMap;
    }

    /**
     * @param players list of usernames
     * @return username of a random player
     */
    public static String player(List<String> players) {
        int index = (int) (Math.random() * players.size());
        return players.get(index);
    }

    /**
     * @param occupied list of coordinates to avoid
     * @return random coordinates in a 5x5 grid (not in occupied)
     */
    public static Coordinates position(List<Coordinates> occupied) {
        List<Coordinates> matches;
        Coordinates random;
        do {
            int row = (int) (Math.random() * 5);
            int col = (int) (Math.random() * 5);
            random = new Coordinates(row, col);
            matches = occupied.stream().filter(random::equals).collect(Collectors.toList());
        } while(matches.size() != 0);
        return random;
    }

    /**
     * @return random coordinates in a 5x5 grid
     */
    public static Coordinates position() {
        return position(new ArrayList<>());
    }

    /**
     * @param players list of usernames
     * @return map {username, list of coordinates} to put the workers on the board
     */
    public static Map<String, List<Coordinates>> startingPositions(List<String> players) {
        int numWorker = 2;

        List<Coordinates> positions = new ArrayList<>();
        while(positions.size() != players.size() * numWorker) {
            positions.add(position(new ArrayList<>(positions)));
        }

        Map<String, List<Coordinates>> workersMap = new LinkedHashMap<>();
        for(String player : players) {
            List<Coordinates> workers = new ArrayList<>();
            for(int i = 0; i < numWorker; i++) {
                int index = (int)(Math.random() * positions.size());
                workers.add(positions.remove(index));
            }
            workersMap.put(player, workers);
        }

        return workersMap;
    }
}
