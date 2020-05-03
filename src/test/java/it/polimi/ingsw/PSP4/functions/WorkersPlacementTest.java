package it.polimi.ingsw.PSP4.functions;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.utils.*;
import it.polimi.ingsw.PSP4.utils.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class WorkersPlacementTest {

    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void workerPlacement_randomParameters() {
        Runner.selectStartingPlayer();

        List<String> players = Getters.playerList();
        Map<String, List<Coordinates>> workers = Random.startingPositions(new ArrayList<>(players));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardNoBuildings());
        assertTrue(Tests.newTurn());
    }

    @Test
    public void workersPlacement_twoPlayers_firstConnected() {
        int numPlayer = 2;
        int startingPlayer = 0;
        Runner.selectStartingPlayer(numPlayer, startingPlayer);

        List<String> players = Getters.playerList();
        Map <String, List<Coordinates>> workers = Random.startingPositions(new ArrayList<>(players));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardNoBuildings());
        assertTrue(Tests.currentPlayer(players.get(startingPlayer)));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.newTurn());
    }

    @Test
    public void workersPlacement_twoPlayers_secondConnected() {
        int numPlayer = 2;
        int startingPlayer = 1;
        Runner.selectStartingPlayer(numPlayer, startingPlayer);

        List<String> players = Getters.playerList();
        Map <String, List<Coordinates>> workers = Random.startingPositions(new ArrayList<>(players));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardNoBuildings());
        assertTrue(Tests.currentPlayer(players.get(startingPlayer)));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.newTurn());
    }

    @Test
    public void workersPlacement_threePlayers_firstConnected() {
        int numPlayer = 3;
        int startingPlayer = 0;
        Runner.selectStartingPlayer(numPlayer, startingPlayer);

        List<String> players = Getters.playerList();
        Map <String, List<Coordinates>> workers = Random.startingPositions(new ArrayList<>(players));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardNoBuildings());
        assertTrue(Tests.currentPlayer(players.get(startingPlayer)));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.newTurn());
    }

    @Test
    public void workersPlacement_threePlayers_secondConnected() {
        int numPlayer = 3;
        int startingPlayer = 1;
        Runner.selectStartingPlayer(numPlayer, startingPlayer);

        List<String> players = Getters.playerList();
        Map <String, List<Coordinates>> workers = Random.startingPositions(new ArrayList<>(players));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardNoBuildings());
        assertTrue(Tests.currentPlayer(players.get(startingPlayer)));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.newTurn());
    }


    @Test
    public void workersPlacement_threePlayers_thirdConnected() {
        int numPlayer = 3;
        int startingPlayer = 2;
        Runner.selectStartingPlayer(numPlayer, startingPlayer);

        List<String> players = Getters.playerList();
        Map <String, List<Coordinates>> workers = Random.startingPositions(new ArrayList<>(players));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardNoBuildings());
        assertTrue(Tests.currentPlayer(players.get(startingPlayer)));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.newTurn());
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
