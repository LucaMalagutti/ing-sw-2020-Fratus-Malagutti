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

import org.junit.Assert;

public class AthenaPowerTest {
    public final String godName = "Athena";

    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsable_enemiesUnwrapped_enemiesStandardMove() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(3, 0), new Coordinates(4, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Arrays.asList(new Coordinates(1, 1), new Coordinates(2, 0)));
        buildings.put(3, Collections.singletonList(new Coordinates(0, 0)));
        buildings.put(4, Arrays.asList(new Coordinates(0, 2), new Coordinates(2, 1)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 1), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 0),
                new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(0, 1);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(1, 0),
                new Coordinates(1, 1), new Coordinates(1, 2)
        );
        Coordinates buildPosition = new Coordinates(0, 0);
        Coordinates enemyStartingPosition = workers.get(enemyPlayer).get(0);
        List<Coordinates> enemyExpectedMoveOptions = Arrays.asList(
                new Coordinates(2, 0), new Coordinates(3, 1),
                new Coordinates(4, 0), new Coordinates(4, 1)
        );

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));
        Actions.wrapPlayers(startingPlayer);

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

        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedBuildOptions)));
        Assert.assertTrue(Tests.enemiesUnwrapped(startingPlayer));

        Actions.selectOption(buildPosition);
        buildings.remove(3);
        buildings.replace(4, Arrays.asList(new Coordinates(0, 2), new Coordinates(2, 1), buildPosition));

        Assert.assertTrue(Tests.currentPlayer(enemyPlayer));
        Assert.assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(enemyStartingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(enemyPlayer))));
        Assert.assertTrue(Tests.currentWorker(enemyStartingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(enemyExpectedMoveOptions)));
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
    }

    @Test
    public void powerNotUsed_enemiesUnwrapped_enemiesStandardMove() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(3, 0), new Coordinates(4, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Arrays.asList(new Coordinates(1, 1), new Coordinates(2, 0)));
        buildings.put(2, Collections.singletonList(new Coordinates(2, 2)));
        buildings.put(3, Collections.singletonList(new Coordinates(0, 0)));
        buildings.put(4, Arrays.asList(new Coordinates(0, 2), new Coordinates(2, 1)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 1), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 0),
                new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(0, 1);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(1, 0),
                new Coordinates(1, 1), new Coordinates(1, 2)
        );
        Coordinates buildPosition = new Coordinates(0, 0);
        Coordinates enemyStartingPosition = workers.get(enemyPlayer).get(0);
        List<Coordinates> enemyExpectedMoveOptions = Arrays.asList(
                new Coordinates(2, 0), new Coordinates(3, 1),
                new Coordinates(4, 0), new Coordinates(4, 1)
        );

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));
        Actions.wrapPlayers(startingPlayer);

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
        Assert.assertTrue(Tests.enemiesUnwrapped(startingPlayer));

        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedBuildOptions)));

        Actions.selectOption(buildPosition);
        buildings.remove(3);
        buildings.replace(4, Arrays.asList(new Coordinates(0, 2), new Coordinates(2, 1), buildPosition));

        Assert.assertTrue(Tests.currentPlayer(enemyPlayer));
        Assert.assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(enemyStartingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(enemyPlayer))));
        Assert.assertTrue(Tests.currentWorker(enemyStartingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(enemyExpectedMoveOptions)));
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
    }

    @Test
    public void powerUsed_enemiesWrapped_enemiesNoMoveUp() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(3, 0), new Coordinates(4, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Arrays.asList(new Coordinates(1, 1), new Coordinates(2, 0)));
        buildings.put(2, Collections.singletonList(new Coordinates(2, 2)));
        buildings.put(3, Collections.singletonList(new Coordinates(0, 0)));
        buildings.put(4, Arrays.asList(new Coordinates(0, 2), new Coordinates(2, 1)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 1), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 0),
                new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(2, 2);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(1, 1), new Coordinates(1, 2),
                new Coordinates(1, 3), new Coordinates(2, 3),
                new Coordinates(3, 1), new Coordinates(3, 2),
                new Coordinates(3, 3)
        );
        Coordinates buildPosition = new Coordinates(3, 2);
        Coordinates enemyStartingPosition = workers.get(enemyPlayer).get(0);
        List<Coordinates> enemyExpectedMoveOptions = Arrays.asList(
                new Coordinates(3, 1), new Coordinates(4, 0),
                new Coordinates(4, 1)
        );

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));
        Actions.unwrapPlayers(startingPlayer);

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
        Assert.assertTrue(Tests.enemiesWrapped(startingPlayer));

        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedBuildOptions)));

        Actions.selectOption(buildPosition);
        buildings.replace(1, Arrays.asList(new Coordinates(1, 1), new Coordinates(2, 0), buildPosition));

        Assert.assertTrue(Tests.currentPlayer(enemyPlayer));
        Assert.assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(enemyStartingPosition);

        Assert.assertTrue(Tests.currentState(new StandardMoveState(Getters.player(enemyPlayer))));
        Assert.assertTrue(Tests.currentWorker(enemyStartingPosition, false));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(enemyExpectedMoveOptions)));
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
    }

    @Test
    public void threePlayers_losesWhileWrapping_enemiesUnwrapped() {
        int numPlayer = 3;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), godName);
        gods.put(players.get(1), "Default");
        gods.put(players.get(2), "Default");
        String startingPlayer = players.get(0);
        String enemyPlayer1 = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(0, 0), new Coordinates(4, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 3)));
        workers.put(players.get(2), Arrays.asList(new Coordinates(1, 0), new Coordinates(3, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(2, Arrays.asList(
                new Coordinates(0, 1), new Coordinates(4, 3)
        ));
        Coordinates firstWorker = workers.get(startingPlayer).get(0);
        Coordinates secondWorker = workers.get(startingPlayer).get(1);
        List<Coordinates> expectedMoveOptions = new ArrayList<>();

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));
        Actions.wrapPlayers(startingPlayer);

        Actions.setCurrentWorker(firstWorker);

        Assert.assertTrue(Tests.enemiesWrapped(startingPlayer));
        Assert.assertTrue(Tests.currentWorker(firstWorker, false));
        Assert.assertTrue(Tests.stateOptions(expectedMoveOptions));

        Actions.changeCurrentWorker(secondWorker);

        gods.remove(startingPlayer);
        workers.remove(startingPlayer);
        numPlayer--;

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.currentPlayer(enemyPlayer1));
        Assert.assertTrue(Tests.playersUnwrappedAll());
        Assert.assertTrue(Tests.newTurn());
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
