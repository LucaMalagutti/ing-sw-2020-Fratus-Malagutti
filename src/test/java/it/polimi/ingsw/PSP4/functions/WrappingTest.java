package it.polimi.ingsw.PSP4.functions;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.utils.Actions;
import it.polimi.ingsw.PSP4.utils.Coordinates;
import it.polimi.ingsw.PSP4.utils.Random;
import it.polimi.ingsw.PSP4.utils.Tests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class WrappingTest {

    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void unwrapEnemiesAthena() {
        String godName = "Athena";

        int numPlayer = 3;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        gods.put(players.get(2), "Default");
        String athenaPlayer = players.get(0);
        String enemyPlayer1 = players.get(1);
        String enemyPlayer2 = players.get(2);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(0, 0), new Coordinates(0, 0)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 3)));
        workers.put(players.get(2), Arrays.asList(new Coordinates(1, 0), new Coordinates(3, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(2, Arrays.asList(
                new Coordinates(0, 1), new Coordinates(4, 3)
        ));
        List<Coordinates> expectedMoveOptions = Arrays.asList();
        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(players.get(0));
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.wrapPlayers(athenaPlayer);

        Coordinates firstWorker = workers.get(athenaPlayer).get(0);
        Actions.fillBoard(buildings);
        Actions.setCurrentWorker(firstWorker);

        assertTrue(Tests.currentWorker(firstWorker, false));
        assertTrue(Tests.stateOptions(new ArrayList<>()));

        Coordinates secondWorker = workers.get(athenaPlayer).get(1);

        Actions.changeCurrentWorker(secondWorker);
        gods.remove(athenaPlayer);
        workers.remove(athenaPlayer);

        Actions.unwrapPlayers(athenaPlayer);

        assertTrue(Tests.gameStateExists());
        //assertTrue(Tests.numberOfPlayers(2));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        //assertTrue(Tests.currentPlayer(enemyPlayer1));
        //assertTrue(Tests.newTurn());

    }

    @Test
    public void unwrapEnemiesHera() {
        String godName = "Hera";

        int numPlayer = 3;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        gods.put(players.get(2), "Default");
        String heraPlayer = players.get(0);
        String enemyPlayer1 = players.get(1);
        String enemyPlayer2 = players.get(2);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(0, 0), new Coordinates(0, 0)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 3)));
        workers.put(players.get(2), Arrays.asList(new Coordinates(1, 0), new Coordinates(3, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(2, Arrays.asList(
                new Coordinates(0, 1), new Coordinates(4, 3)
        ));
        List<Coordinates> expectedMoveOptions = Arrays.asList();
        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(players.get(0));
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.wrapPlayers(heraPlayer);

        Coordinates firstWorker = workers.get(heraPlayer).get(0);
        Actions.fillBoard(buildings);
        Actions.setCurrentWorker(firstWorker);

        assertTrue(Tests.currentWorker(firstWorker, false));
        assertTrue(Tests.stateOptions(new ArrayList<>()));

        Coordinates secondWorker = workers.get(heraPlayer).get(1);

        Actions.changeCurrentWorker(secondWorker);
        gods.remove(heraPlayer);
        workers.remove(heraPlayer);

        Actions.unwrapPlayers(heraPlayer);

        assertTrue(Tests.gameStateExists());
        //assertTrue(Tests.numberOfPlayers(2));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        //assertTrue(Tests.currentPlayer(enemyPlayer1));
        //assertTrue(Tests.newTurn());

    }

    @Test
    public void unwrapEnemiesLimus() {
        String godName = "Limus";

        int numPlayer = 3;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        gods.put(players.get(2), "Default");
        String limusPlayer = players.get(0);
        String enemyPlayer1 = players.get(1);
        String enemyPlayer2 = players.get(2);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(0, 0), new Coordinates(0, 0)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 3)));
        workers.put(players.get(2), Arrays.asList(new Coordinates(1, 0), new Coordinates(3, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(2, Arrays.asList(
                new Coordinates(0, 1), new Coordinates(4, 3)
        ));
        List<Coordinates> expectedMoveOptions = Arrays.asList();
        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(players.get(0));
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.wrapPlayers(limusPlayer);

        Coordinates firstWorker = workers.get(limusPlayer).get(0);
        Actions.fillBoard(buildings);
        Actions.setCurrentWorker(firstWorker);

        assertTrue(Tests.currentWorker(firstWorker, false));
        assertTrue(Tests.stateOptions(new ArrayList<>()));

        Coordinates secondWorker = workers.get(limusPlayer).get(1);

        Actions.changeCurrentWorker(secondWorker);
        gods.remove(limusPlayer);
        workers.remove(limusPlayer);

        Actions.unwrapPlayers(limusPlayer);

        assertTrue(Tests.gameStateExists());
        //assertTrue(Tests.numberOfPlayers(2));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        //assertTrue(Tests.currentPlayer(enemyPlayer1));
        //assertTrue(Tests.newTurn());

    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
