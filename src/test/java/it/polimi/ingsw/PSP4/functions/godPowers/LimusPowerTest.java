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

public class LimusPowerTest {

    public final String godName = "Limus";

    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerUsed_enemiesWrapped_enemiesNotBuild() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), "Default");
        gods.put(players.get(1), godName);
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(1, 4), new Coordinates(4, 0)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(1, 2), new Coordinates(3, 3)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Collections.singletonList(new Coordinates(2, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(2,4)));
        buildings.put(3, Arrays.asList(
                new Coordinates(1, 3), new Coordinates(3, 4)
        ));
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 3), new Coordinates(0, 4),
                new Coordinates(2, 3)
        );
        Coordinates movePosition = new Coordinates(2,3);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(1, 3), new Coordinates(3, 4)
        );
        Coordinates buildPosition = new Coordinates(3, 4);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));
        Actions.wrapPlayers(startingPlayer);

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer(startingPlayer));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.allowedGodsEmpty());
        assertTrue(Tests.newTurn());
    }

    @Test
    public void powerNotUsed_enemiesUnwrapped_enemiesCanBuild() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put(players.get(0), "Default");
        gods.put(players.get(1), godName);
        String startingPlayer = players.get(0);
        String enemyPlayer = players.get(1);
        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put(players.get(0), Arrays.asList(new Coordinates(0, 0), new Coordinates(4, 0)));
        workers.put(players.get(1), Arrays.asList(new Coordinates(0, 4), new Coordinates(3, 3)));
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Collections.singletonList(new Coordinates(2, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(2, 4)));
        buildings.put(3, Arrays.asList(
                new Coordinates(1, 3), new Coordinates(3, 4)
        ));
        Coordinates startingPosition = workers.get(startingPlayer).get(0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 1), new Coordinates(1, 1),
                new Coordinates(1, 0)
        );
        Coordinates movePosition = new Coordinates(1, 0);
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(1, 1), new Coordinates(2, 1),
                new Coordinates(2, 0)
        );
        Coordinates buildPosition = new Coordinates(2, 1);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);
        Actions.placeWorkers(new LinkedHashMap<>(workers));
        Actions.fillBoard(new LinkedHashMap<>(buildings));

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        assertTrue(Tests.currentPlayer(startingPlayer));
        assertTrue(Tests.numberOfPlayers(numPlayer));
        assertTrue(Tests.allowedGodsEmpty());
        assertTrue(Tests.newTurn());

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

        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.currentPlayer(enemyPlayer));
        assertTrue(Tests.currentState(new StandardMoveState(Getters.player(enemyPlayer))));
        assertTrue(Tests.newTurn());
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
