package it.polimi.ingsw.PSP4.functions.godPowers;

import it.polimi.ingsw.PSP4.controller.turnStates.StandardBuildState;
import it.polimi.ingsw.PSP4.controller.turnStates.StandardMoveState;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.utils.*;
import it.polimi.ingsw.PSP4.utils.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class PanPowerTest {
    public String godName = "Pan";

    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsable_standardMove() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(4, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 0), new Coordinates(3, 3)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(4, Arrays.asList(
            new Coordinates(2, 0), new Coordinates(2, 1), new Coordinates(2, 2),
            new Coordinates(0, 2), new Coordinates(1, 2)
        ));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(new Coordinates(1, 0), new Coordinates(0, 1));
        Coordinates movePosition = new Coordinates(0, 1);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));
        Actions.setCurrentWorker(startingPosition);

        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(startingPosition, false));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));

        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer(startingPlayer));
        assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(movePosition, true));
    }

    @Test
    public void powerUsed_specialWinCondition_gameWon() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(2, 2), new Coordinates(4, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 3)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(2, Collections.singletonList(new Coordinates(2, 2)));
        buildings.put(3, Collections.singletonList(new Coordinates(1, 1)));
        buildings.put(4, Arrays.asList(
            new Coordinates(2, 1), new Coordinates(1, 2),
            new Coordinates(3, 2), new Coordinates(2, 3)
        ));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
            new Coordinates(1, 3), new Coordinates(3, 1)
        );
        Coordinates movePosition = new Coordinates(1, 3);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));
        Actions.setCurrentWorker(startingPosition);

        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(startingPosition, false));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);

        assertTrue(Tests.gameStateClean());
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
