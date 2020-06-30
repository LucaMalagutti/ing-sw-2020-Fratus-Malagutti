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

public class LimusPowerTest {

    public final String godName = "Limus";

    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsable_enemiesWrapped_enemiesStandardBuild() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), "Default");
        gods.put(players.get(1), godName);
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(4, 1), new Coordinates(4, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(0, 2), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 0),
                new Coordinates(2, 1), new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(1, 2);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(0, 1), new Coordinates(0, 2),
                new Coordinates(0, 3), new Coordinates(1, 1),
                new Coordinates(1, 3), new Coordinates(2, 1),
                new Coordinates(2, 2), new Coordinates(2, 3)
        );
        Coordinates buildPosition = new Coordinates(0, 1);

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
        Assert.assertTrue(Tests.enemiesWrapped(enemyPlayer));
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

        Actions.selectOption(buildPosition);
        buildings.put(1, Collections.singletonList(buildPosition));

        Assert.assertTrue(Tests.currentPlayer(enemyPlayer));
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.newTurn());
    }

    @Test
    public void powerUsed_enemiesWrapped_enemiesLimitedBuild() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), "Default");
        gods.put(players.get(1), godName);
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(4, 1), new Coordinates(4, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(0, 2), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 0),
                new Coordinates(2, 1), new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(2, 1);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(1, 0), new Coordinates(1, 1),
                new Coordinates(1, 2), new Coordinates(2, 0),
                new Coordinates(2, 2)
        );
        Coordinates buildPosition = new Coordinates(2, 0);

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
        Assert.assertTrue(Tests.enemiesWrapped(enemyPlayer));
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

        Actions.selectOption(buildPosition);
        buildings.put(1, Collections.singletonList(buildPosition));

        Assert.assertTrue(Tests.currentPlayer(enemyPlayer));
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.newTurn());
    }

    @Test
    public void powerUsed_enemiesWrapped_enemiesStandardBuildDome() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), "Default");
        gods.put(players.get(1), godName);
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 4)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(4, 1), new Coordinates(4, 4)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(3, Collections.singletonList(new Coordinates(3, 1)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(0, 2), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 0),
                new Coordinates(2, 1), new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(2, 1);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(1, 0), new Coordinates(1, 1),
                new Coordinates(1, 2), new Coordinates(2, 0),
                new Coordinates(2, 2), new Coordinates(3, 1)
        );
        Coordinates buildPosition = new Coordinates(3, 1);

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
        Assert.assertTrue(Tests.enemiesWrapped(enemyPlayer));
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

        Actions.selectOption(buildPosition);
        buildings.remove(3);
        buildings.put(4, Collections.singletonList(buildPosition));

        Assert.assertTrue(Tests.currentPlayer(enemyPlayer));
        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.newTurn());
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
