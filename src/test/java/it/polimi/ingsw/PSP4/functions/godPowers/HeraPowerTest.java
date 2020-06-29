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
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class HeraPowerTest {
    public final String godName = "Hera";

    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsable_enemiesUnwrapped_enemyGameWon() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), "Default");
        gods.put(players.get(1), godName);
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(2, 3), new Coordinates(4, 0)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(2, 1), new Coordinates(3, 2)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(2, Collections.singletonList(new Coordinates(2, 3)));
        buildings.put(3, Arrays.asList(
                new Coordinates(1, 3), new Coordinates(2, 4)
        ));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(1, 2), new Coordinates(1, 3),
                new Coordinates(2, 2), new Coordinates(2, 4),
                new Coordinates(3,3), new Coordinates(3,4)
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
        //assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);

        //assertTrue(Tests.gameStateClean());
    }

    @Test
    public void powerUsable_enemiesWrapped_enemyNotWin() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), "Default");
        gods.put(players.get(1), godName);
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(2, 3), new Coordinates(4, 0)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(2, 1), new Coordinates(3, 2)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(2, Collections.singletonList(new Coordinates(2, 3)));
        buildings.put(3, Arrays.asList(
                new Coordinates(1, 3), new Coordinates(2, 4)
        ));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(1, 2), new Coordinates(1, 3),
                new Coordinates(2, 2), new Coordinates(2, 4),
                new Coordinates(3,3), new Coordinates(3,4)
        );
        Coordinates movePosition = new Coordinates(2, 4);

        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(2, 2), new Coordinates(1, 2),
                new Coordinates(1,3), new Coordinates(1,4),
                new Coordinates(2,4), new Coordinates(3,4),
                new Coordinates(3,3)
        );
        Coordinates buildPosition = new Coordinates(1, 4);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));
        Actions.wrapPlayers(startingPlayer);

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        //assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        assertTrue(Tests.currentPlayer(startingPlayer));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.allowedGodsEmpty());
        assertTrue(Tests.newTurn());

        Actions.setCurrentWorker(startingPosition);

        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(startingPosition, false));
        //assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Actions.selectOption(movePosition);
        workers.replace(startingPlayer, Arrays.asList(movePosition, workers.get(startingPlayer).get(1)));

        assertTrue(Tests.currentState(new StandardBuildState(Getters.player(startingPlayer))));
        assertTrue(Tests.currentWorker(movePosition, true));

        Actions.selectOption(buildPosition);

        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.currentPlayer(enemyPlayer));
        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(enemyPlayer))));
        assertTrue(Tests.newTurn());
    }



    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}