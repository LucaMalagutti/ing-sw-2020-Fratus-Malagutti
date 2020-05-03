package it.polimi.ingsw.PSP4.utils;

/**
 * Class of static methods used to take the game to a defined point
 */
public class Runner {
    /**
     * Run until random players are added
     * @param numPlayers number of players
     */
    public static void addPlayers(int numPlayers) {
        Actions.addPlayers(Random.playerList(numPlayers));
    }

    /**
     * Run until random players are added
     */
    public static void addPlayers() {
        addPlayers(Random.numberOfPlayers());
    }

    /**
     * Run until random gods are assigned
     * @param numPlayers number of players
     */
    public static void godAssignments(int numPlayers) {
        addPlayers(numPlayers);
        Actions.assignGods(Random.godsAssignments(Getters.playerList()));
    }
    /**
     * Run until random gods are assigned
     */
    public static void godAssignments() {
        godAssignments(Random.numberOfPlayers());
    }

    /**
     * Run until a random player is selected to start
     * @param numPlayer number of players
     * @param starting index of the starting player
     */
    public static void selectStartingPlayer(int numPlayer, int starting) {
        godAssignments(numPlayer);
        Actions.selectStartingPlayer(Getters.playerList().get(starting));
    }

    /**
     * Run until a random player is selected to start
     */
    public static void selectStartingPlayer() {
        int numPlayer = Random.numberOfPlayers();
        int index = (int) (Math.random() * numPlayer);
        selectStartingPlayer(numPlayer, index);
    }

    /**
     * Run until every player is placed in a random position
     * @param numPlayer number of players
     * @param starting index of the starting player
     */
    public static void workersPlacement(int numPlayer, int starting) {
        selectStartingPlayer(numPlayer, starting);
        Actions.placeWorkers(Random.startingPositions(Getters.playerList()));
    }
    /**
     * Run until every player is placed in a random position
     */
    public static void workersPlacement() {
        int numPlayer = Random.numberOfPlayers();
        int index = (int) (Math.random() * numPlayer);
        workersPlacement(numPlayer, index);
    }
}
