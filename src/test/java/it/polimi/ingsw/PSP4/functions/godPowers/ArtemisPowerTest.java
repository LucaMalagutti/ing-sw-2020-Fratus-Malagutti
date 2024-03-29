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

public class ArtemisPowerTest {
    public final String godName = "Artemis";

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
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 1), new Coordinates(0, 3)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 1), new Coordinates(1, 2)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(0, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(1, 0)));
        buildings.put(3, Collections.singletonList(new Coordinates(2, 1)));
        buildings.put(4, Arrays.asList(new Coordinates(0, 0), new Coordinates(1, 3)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedFirstMoveOptions = Arrays.asList(
                new Coordinates(0, 2), new Coordinates(2, 0),
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
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedFirstMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));

        Assert.assertTrue(Tests.currentState(new SecondMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
        //No options left for second move, forced to skip
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>()));

        Actions.skipCurrentState();

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
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(0, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(1, 0)));
        buildings.put(3, Collections.singletonList(new Coordinates(2, 1)));
        buildings.put(4, Arrays.asList(new Coordinates(0, 0), new Coordinates(1, 3)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedFirstMoveOptions = Arrays.asList(
                new Coordinates(0, 2), new Coordinates(2, 0),
                new Coordinates(2, 2)
        );
        Coordinates movePosition = new Coordinates(0, 2);
        List<Coordinates> expectedSecondMoveOptions = Collections.singletonList(new Coordinates(0, 3));

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
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedFirstMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));

        Assert.assertTrue(Tests.currentState(new SecondMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedSecondMoveOptions)));

        Actions.skipCurrentState();

        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(movePosition, true));
    }

    @Test
    public void powerUsed_doubleMove() {
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
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(0, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(1, 0)));
        buildings.put(3, Collections.singletonList(new Coordinates(2, 1)));
        buildings.put(4, Arrays.asList(new Coordinates(0, 0), new Coordinates(1, 3)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedFirstMoveOptions = Arrays.asList(
                new Coordinates(0, 2), new Coordinates(2, 0),
                new Coordinates(2, 2)
        );
        Coordinates firstMovePosition = new Coordinates(0, 2);
        List<Coordinates> expectedSecondMoveOptions = Collections.singletonList(new Coordinates(0, 3));
        Coordinates secondMovePosition = new Coordinates(0, 3);

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
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedFirstMoveOptions)));

        Actions.selectOption(firstMovePosition);
        workers.replace(startingPlayer, Arrays.asList(firstMovePosition, workers.get(startingPlayer).get(1)));

        Assert.assertTrue(Tests.currentState(new SecondMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(firstMovePosition, true));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedSecondMoveOptions)));

        Actions.selectOption(secondMovePosition);
        workers.replace(startingPlayer, Arrays.asList(secondMovePosition, workers.get(startingPlayer).get(1)));

        Assert.assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        Assert.assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(secondMovePosition, true));
    }

    @Test
    public void powerUsed_victoryCondition_gameWonAtFirst() {
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
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(0, 3)));
        buildings.put(2, Arrays.asList(new Coordinates(1, 0), new Coordinates(1, 1)));
        buildings.put(3, Arrays.asList(new Coordinates(2, 1), new Coordinates(0, 2)));
        buildings.put(4, Arrays.asList(new Coordinates(0, 0), new Coordinates(1, 3)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedFirstMoveOptions = Arrays.asList(
                new Coordinates(0, 2), new Coordinates(1, 0),
                new Coordinates(2, 0), new Coordinates(2, 1),
                new Coordinates(2, 2)
        );
        Coordinates firstMovePosition = new Coordinates(0, 2);

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
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedFirstMoveOptions)));

        Actions.selectOption(firstMovePosition);

        Assert.assertTrue(Tests.gameStateClean());
    }

    @Test
    public void powerUsed_victoryCondition_gameWonAtSecond() {
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
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(1, 1)));
        buildings.put(2, Arrays.asList(new Coordinates(1, 0), new Coordinates(0, 2)));
        buildings.put(3, Arrays.asList(new Coordinates(2, 1), new Coordinates(0, 3)));
        buildings.put(4, Arrays.asList(new Coordinates(0, 0), new Coordinates(1, 3)));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedFirstMoveOptions = Arrays.asList(
                new Coordinates(0, 2), new Coordinates(1, 0),
                new Coordinates(2, 0), new Coordinates(2, 2)
        );
        Coordinates firstMovePosition = new Coordinates(0, 2);
        List<Coordinates> expectedSecondMoveOptions = Collections.singletonList(new Coordinates(0, 3));
        Coordinates secondMovePosition = new Coordinates(0, 3);

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
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedFirstMoveOptions)));

        Actions.selectOption(firstMovePosition);
        workers.replace(startingPlayer, Arrays.asList(firstMovePosition, workers.get(startingPlayer).get(1)));

        Assert.assertTrue(Tests.currentState(new SecondMoveState(Getters.player(startingPlayer))));
        Assert.assertTrue(Tests.currentWorker(firstMovePosition, true));
        Assert.assertTrue(Tests.stateOptions(new ArrayList<>(expectedSecondMoveOptions)));

        Actions.selectOption(secondMovePosition);

        Assert.assertTrue(Tests.gameStateClean());
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
