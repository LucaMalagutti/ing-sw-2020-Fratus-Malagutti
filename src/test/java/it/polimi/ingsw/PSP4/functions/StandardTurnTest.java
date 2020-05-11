package it.polimi.ingsw.PSP4.functions;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.utils.Actions;
import it.polimi.ingsw.PSP4.utils.Coordinates;
import it.polimi.ingsw.PSP4.utils.Tests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class StandardTurnTest {
    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void standardTurn_noChangeWorker() {
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put("Hero", "Default");
        gods.put("Enemy", "Default");

        Actions.addPlayers(new ArrayList<>(gods.keySet()));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer("Hero");

        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put("Hero", Arrays.asList(new Coordinates(0, 0), new Coordinates(4, 4)));
        workers.put("Enemy", Arrays.asList(new Coordinates(1, 0), new Coordinates(4, 0)));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        Coordinates startingPosition = new Coordinates(0, 0);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(0, 1), new Coordinates(1, 1)
        );

        Actions.setCurrentWorker(startingPosition);
        assertTrue(Tests.currentWorker(startingPosition, false));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Coordinates movePosition = new Coordinates(0, 1);
        workers.replace("Hero", Arrays.asList(movePosition, new Coordinates(4, 4)));
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 2),
                new Coordinates(1, 1), new Coordinates(1, 2)
        );

        Actions.selectOption(movePosition);
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardNoBuildings());
        assertTrue(Tests.currentWorker(movePosition, true));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedBuildOptions)));

        Coordinates buildPosition = new Coordinates(1, 1);
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Collections.singletonList(buildPosition));

        Actions.selectOption(buildPosition);
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer("Enemy"));
        assertTrue(Tests.newTurn());

        //Redundant assertions
        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        assertTrue(Tests.numberOfPlayers(gods.size()));
        assertTrue(Tests.allowedGodsEmpty());
    }

    @Test
    public void standardTurn_changeWorker() {
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put("Hero", "Default");
        gods.put("Enemy", "Default");

        Actions.addPlayers(new ArrayList<>(gods.keySet()));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer("Hero");

        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put("Hero", Arrays.asList(new Coordinates(0, 0), new Coordinates(3, 3)));
        workers.put("Enemy", Arrays.asList(new Coordinates(0, 4), new Coordinates(4, 0)));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        Coordinates fakePosition = workers.get("Hero").get(0);
        List<Coordinates> fakeMoveOptions = Arrays.asList(
                new Coordinates(0, 1), new Coordinates(1, 1),
                new Coordinates(1, 0)
        );

        Actions.setCurrentWorker(fakePosition);
        assertTrue(Tests.currentWorker(fakePosition, false));
        assertTrue(Tests.stateOptions(new ArrayList<>(fakeMoveOptions)));

        Coordinates startingPosition = workers.get("Hero").get(1);
        List<Coordinates> expectedMoveOptions = Arrays.asList(
                new Coordinates(2, 2), new Coordinates(2, 3),
                new Coordinates(2, 4), new Coordinates(3, 2),
                new Coordinates(3, 4), new Coordinates(4, 2),
                new Coordinates(4, 3), new Coordinates(4, 4)
        );

        Actions.changeCurrentWorker(startingPosition);
        assertTrue(Tests.currentWorker(startingPosition, false));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedMoveOptions)));

        Coordinates movePosition = new Coordinates(2, 4);
        workers.replace("Hero", Arrays.asList(new Coordinates(0, 0), movePosition));
        List<Coordinates> expectedBuildOptions = Arrays.asList(
                new Coordinates(1, 3), new Coordinates(1, 4),
                new Coordinates(2, 3), new Coordinates(3, 3),
                new Coordinates(3, 4)
        );

        Actions.selectOption(movePosition);
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardNoBuildings());
        assertTrue(Tests.currentWorker(movePosition, true));
        assertTrue(Tests.stateOptions(new ArrayList<>(expectedBuildOptions)));

        Coordinates buildPosition = new Coordinates(4, 4);
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Collections.singletonList(buildPosition));

        Actions.selectOption(buildPosition);
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer("Enemy"));
        assertTrue(Tests.newTurn());

        //Redundant assertions
        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        assertTrue(Tests.numberOfPlayers(gods.size()));
        assertTrue(Tests.allowedGodsEmpty());
    }

    @Test
    public void standardTurn_twoPlayers_playerDefeat() {
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put("Hero", "Default");
        gods.put("Enemy", "Default");

        Actions.addPlayers(new ArrayList<>(gods.keySet()));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer("Enemy");

        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put("Hero", Arrays.asList(new Coordinates(1, 3), new Coordinates(3, 4)));
        workers.put("Enemy", Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 2)));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(4, Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(0, 2), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 0),
                new Coordinates(2, 1), new Coordinates(2, 2),
                new Coordinates(2, 3), new Coordinates(3, 1),
                new Coordinates(3, 3), new Coordinates(4, 1),
                new Coordinates(4, 2), new Coordinates(4, 3)
        ));
        Coordinates firstWorker = workers.get("Enemy").get(0);

        Actions.fillBoard(buildings);
        Actions.setCurrentWorker(firstWorker);
        assertTrue(Tests.currentWorker(firstWorker, false));
        assertTrue(Tests.stateOptions(new ArrayList<>()));

        Coordinates secondWorker = workers.get("Enemy").get(1);

        Actions.changeCurrentWorker(secondWorker);

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.boardEmpty());
        assertEquals(0, GameState.getInstance().getPlayers().size());
        assertNull(GameState.getInstance().getCurrPlayer());
        assertTrue(Tests.numberOfPlayers(0));
        assertTrue(Tests.allowedGodsEmpty());
    }

    @Test
    public void standardTurn_threePlayers_playerDefeat() {
        Map<String, String> gods = new LinkedHashMap<>();
        gods.put("Hero", "Default");
        gods.put("Enemy1", "Default");
        gods.put("Enemy2", "Default");

        Actions.addPlayers(new ArrayList<>(gods.keySet()));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer("Enemy1");

        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put("Hero", Arrays.asList(new Coordinates(1, 3), new Coordinates(3, 4)));
        workers.put("Enemy1", Arrays.asList(new Coordinates(1, 1), new Coordinates(3, 2)));
        workers.put("Enemy2", Arrays.asList(new Coordinates(4, 0), new Coordinates(4, 4)));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(4, Arrays.asList(
                new Coordinates(0, 0), new Coordinates(0, 1),
                new Coordinates(0, 2), new Coordinates(1, 0),
                new Coordinates(1, 2), new Coordinates(2, 0),
                new Coordinates(2, 1), new Coordinates(2, 2),
                new Coordinates(2, 3), new Coordinates(3, 1),
                new Coordinates(3, 3), new Coordinates(4, 1),
                new Coordinates(4, 2), new Coordinates(4, 3)
        ));
        Coordinates firstWorker = workers.get("Enemy1").get(0);

        Actions.fillBoard(buildings);
        Actions.setCurrentWorker(firstWorker);
        assertTrue(Tests.currentWorker(firstWorker, false));
        assertTrue(Tests.stateOptions(new ArrayList<>()));

        Coordinates secondWorker = workers.get("Enemy1").get(1);

        Actions.changeCurrentWorker(secondWorker);
        gods.remove("Enemy1");
        workers.remove("Enemy1");

        assertTrue(Tests.gameStateExists());
        assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        assertTrue(Tests.numberOfPlayers(2));
        assertTrue(Tests.boardWorkers(new LinkedHashMap<>(workers)));
        assertTrue(Tests.boardBuildings(new LinkedHashMap<>(buildings)));
        assertTrue(Tests.currentPlayer("Enemy2"));
        assertTrue(Tests.newTurn());
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
