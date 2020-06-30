package it.polimi.ingsw.PSP4.functions.godPowers;

import it.polimi.ingsw.PSP4.controller.turnStates.StandardBuildState;
import it.polimi.ingsw.PSP4.controller.turnStates.StandardMoveState;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.utils.Random;
import it.polimi.ingsw.PSP4.utils.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import org.junit.Assert;

public class ApolloPowerTest {
    public final String godName = "Apollo";

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
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(4, 3), new Coordinates(4, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(2, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(1, 2)));
        buildings.put(3, Collections.singletonList(new Coordinates(1, 0)));
        buildings.put(4, Collections.singletonList(new Coordinates(2, 0)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(0, 2), new Coordinates(2, 1),
                new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(0, 2);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
        Assert.assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(startingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(startingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));

        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
    }

    @Test
    public void powerNotUsed_standardMove() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 1), new Coordinates(1, 2)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(2, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(1, 2)));
        buildings.put(3, Collections.singletonList(new Coordinates(1, 0)));
        buildings.put(4, Collections.singletonList(new Coordinates(2, 0)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(0, 2), new Coordinates(2, 1),
                new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(0, 2);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
        Assert.assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(startingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(startingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));

        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
    }

    @Test
    public void powerUsed_swapWithEnemyWorker() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 2), new Coordinates(1, 2)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(2, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(1, 2)));
        buildings.put(3, Collections.singletonList(new Coordinates(1, 0)));
        buildings.put(4, Collections.singletonList(new Coordinates(2, 0)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(0, 2), new Coordinates(2, 1),
                new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(0, 2);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
        Assert.assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(startingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(startingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));
        workers.replace(enemyPlayer, Arrays.asList(startingPosition, workers.get(enemyPlayer).get(1)));

        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
    }

    @Test
    public void powerUsed_victoryCondition_gameWon() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 1), new Coordinates(1, 2)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(2, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(1, 1)));
        buildings.put(3, Collections.singletonList(new Coordinates(1, 2)));
        buildings.put(4, Collections.singletonList(new Coordinates(2, 0)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(0, 2), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 1),
                new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(1, 2);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
        Assert.assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(startingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(startingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);

        Assert.assertTrue(Tests.gameStateClean());
    }

    @Test
    public void powerUsed_victoryConditionEnemy_gameNotWon() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 1), new Coordinates(1, 2)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(2, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(1, 2)));
        buildings.put(3, Collections.singletonList(new Coordinates(1, 1)));
        buildings.put(4, Collections.singletonList(new Coordinates(2, 0)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(0, 2), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 1),
                new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(1, 2);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
        Assert.assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(startingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(startingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));
        workers.replace(enemyPlayer, Arrays.asList(workers.get(enemyPlayer).get(0), startingPosition));

        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
    }

    @Test
    public void powerUsed_noBuildOptions_gameLost() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(1, 3)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 2), new Coordinates(1, 2)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Collections.singletonList(new Coordinates(2, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(1, 2)));
        buildings.put(3, Collections.singletonList(new Coordinates(1, 0)));
        buildings.put(4, Arrays.asList(new Coordinates(0, 1), new Coordinates(0, 3), new Coordinates(2, 0)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 2),
                new Coordinates(2, 1), new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(0, 2);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
        Assert.assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(startingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(startingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);

        Assert.assertTrue(Tests.gameStateClean());
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
