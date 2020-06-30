package it.polimi.ingsw.PSP4.functions.godPowers;

import it.polimi.ingsw.PSP4.controller.turnStates.SecondMoveState;
import it.polimi.ingsw.PSP4.controller.turnStates.StandardBuildState;
import it.polimi.ingsw.PSP4.controller.turnStates.StandardMoveState;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.utils.*;
import it.polimi.ingsw.PSP4.utils.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import org.junit.Assert;

public class TritonPowerTest {
    public final String godName = "Triton";

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
        workers.put(players.get(0), Arrays.asList(new Coordinates(2, 2), new Coordinates(2, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 4), new Coordinates(1, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(4, Arrays.asList(
            new Coordinates(1, 0), new Coordinates(1, 1),
            new Coordinates(1, 3), new Coordinates(2, 0),
            new Coordinates(3, 0), new Coordinates(3, 1),
            new Coordinates(3, 3)
        ));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
            new Coordinates(1, 2), new Coordinates(2, 1),
            new Coordinates(3, 2), new Coordinates(2,3)
        );
        Coordinates movePosition = new Coordinates(2, 3);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));
        Actions.setCurrentWorker(startingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(startingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));

        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
    }

    @Test
    public void powerUsedTwice_doubleMoveOnPerimeter() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(3, 1), new Coordinates(2, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 4), new Coordinates(1, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(4, Arrays.asList(
            new Coordinates(2, 0), new Coordinates(2, 1),
            new Coordinates(2, 2), new Coordinates(3, 0),
            new Coordinates(3, 2)
        ));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedFirstMoveOptions = Arrays.asList(
            new Coordinates(4, 0), new Coordinates(4, 1),
            new Coordinates(4, 2)
        );
        Coordinates firstMovePosition = new Coordinates(4, 1);
        List<Coordinates> expectedSecondMoveOptions = Arrays.asList(
            new Coordinates(4, 0), new Coordinates(4, 2),
            new Coordinates(3, 1)
        );
        Coordinates secondMovePosition = new Coordinates(4, 2);
        List<Coordinates> expectedThirdMoveOptions = Arrays.asList(
            new Coordinates(4, 1), new Coordinates(4, 3),
            new Coordinates(3, 3), new Coordinates(3,1)
        );
        Coordinates thirdMovePosition = new Coordinates(4, 1);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));
        Actions.setCurrentWorker(startingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(startingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedFirstMoveOptions)));

        Actions.selectOption(firstMovePosition);
        workers.replace(startingPlayer, Arrays.asList(firstMovePosition, workers.get(startingPlayer).get(1)));

        Assert.assertTrue(Tests.currentState(new SecondMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(firstMovePosition, true));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedSecondMoveOptions)));

        Actions.selectOption(secondMovePosition);
        workers.replace(startingPlayer, Arrays.asList(secondMovePosition, workers.get(startingPlayer).get(1)));

        Assert.assertTrue(Tests.currentState(new SecondMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(secondMovePosition, true));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedThirdMoveOptions)));

        Actions.selectOption(thirdMovePosition);
        workers.replace(startingPlayer, Arrays.asList(thirdMovePosition, workers.get(startingPlayer).get(1)));

        Actions.skipCurrentState();

        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(thirdMovePosition, true));
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
