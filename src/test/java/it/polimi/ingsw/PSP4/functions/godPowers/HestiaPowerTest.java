package it.polimi.ingsw.PSP4.functions.godPowers;

import it.polimi.ingsw.PSP4.controller.turnStates.SecondBuildState;
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

public class HestiaPowerTest {
    public final String godName = "Hestia";

    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsed_standardBuild() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(2, 2), new Coordinates(0, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(2, 1), new Coordinates(1, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(4, Collections.singletonList(
            new Coordinates(1, 2)
        ));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
            new Coordinates(1, 3), new Coordinates(1,1),
            new Coordinates(2,3), new Coordinates(3,1),
            new Coordinates(3,2), new Coordinates(3,3));
        Coordinates movePosition = new Coordinates(1, 1);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
            new Coordinates(0, 0), new Coordinates(0, 1),
            new Coordinates(1, 0), new Coordinates(2, 0),
            new Coordinates(0, 2),new Coordinates(2, 2));
        Coordinates buildPosition = new Coordinates(0, 0);
        List<Coordinates> expectedSecondBuildOptions = Collections.singletonList(new Coordinates(2, 2));

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

        assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(movePosition, true));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedBuildOptions)));

        Actions.selectOption(buildPosition);
        buildings.put(1,Collections.singletonList(new Coordinates(0,0)));

        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer(startingPlayer));
        assertTrue(Tests.currentState(new SecondBuildState(Getters.player(enemyPlayer))));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedSecondBuildOptions)));

        Actions.skipCurrentState();

        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer(enemyPlayer));
        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(enemyPlayer))));
        assertTrue(Tests.newTurn());
    }

    @Test
    public void powerUsed_doubleBuildNotSamePosition() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(2, 2), new Coordinates(0, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(2, 1), new Coordinates(1, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(4, Collections.singletonList(
            new Coordinates(1, 2)
        ));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
            new Coordinates(1, 3), new Coordinates(1,1),
            new Coordinates(2,3), new Coordinates(3,1),
            new Coordinates(3,2), new Coordinates(3,3));
        Coordinates movePosition = new Coordinates(1, 1);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
            new Coordinates(0, 0), new Coordinates(0, 1),
            new Coordinates(1, 0), new Coordinates(2, 0),
            new Coordinates(0, 2),new Coordinates(2, 2));
        Coordinates buildPosition = new Coordinates(0, 0);
        List<Coordinates> expectedSecondBuildOptions = Collections.singletonList(new Coordinates(2, 2));
        Coordinates secondBuildPosition = new Coordinates(2,2);

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

        assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(movePosition, true));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedBuildOptions)));

        Actions.selectOption(buildPosition);
        buildings.put(1,Collections.singletonList(new Coordinates(0,0)));

        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer(startingPlayer));
        assertTrue(Tests.currentState(new SecondBuildState(Getters.player(enemyPlayer))));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedSecondBuildOptions)));

        Actions.selectOption(secondBuildPosition);
        buildings.remove(1);
        buildings.put(1, Arrays.asList(new Coordinates(0,0),new Coordinates(2, 2)));

        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer(enemyPlayer));
        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(enemyPlayer))));
        assertTrue(Tests.newTurn());
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
