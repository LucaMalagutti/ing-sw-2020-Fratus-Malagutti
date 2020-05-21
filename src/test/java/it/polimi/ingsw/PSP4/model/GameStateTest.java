package it.polimi.ingsw.PSP4.model;

import it.polimi.ingsw.PSP4.client.gui.GUIClient;
import it.polimi.ingsw.PSP4.utils.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GameStateTest {
    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void getInstance_postImplementation() {
        assertTrue(Tests.gameStateClean());
    }

    @Test
    public void getInstance_postReset() {
        GameState.getInstance().dropAllConnections();

        assertTrue(Tests.gameStateClean());
    }

    @Ignore
    @Test
    public void gameState_printTest() {
        Runner.workersPlacement(3, 0);
        List<Coordinates> firstLevel = Arrays.asList(
                new Coordinates(0, 0), new Coordinates(3, 3),
                new Coordinates(1, 1), new Coordinates(2, 2),
                new Coordinates(4, 4)
        );
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, firstLevel);
        Actions.fillBoard(buildings);
        Actions.setCurrentWorker(Getters.workersOnBoard().get(Getters.currentPlayer()).get(0));
        System.out.println(GameState.getSerializedInstance().toString());
    }

    @Ignore
    @Test
    public void gameState_guiTest() {Map<String, String> gods = new LinkedHashMap<>();
        gods.put("Hero", "Athena");
        gods.put("Enemy", "Demeter");

        Actions.addPlayers(new ArrayList<>(gods.keySet()));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer("Hero");

        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Collections.singletonList(new Coordinates(0, 1)));
        Actions.fillBoard(buildings);

        Map<String, List<Coordinates>> workers = new LinkedHashMap<>();
        workers.put("Hero", Arrays.asList(new Coordinates(0, 0), new Coordinates(4, 4)));
        workers.put("Enemy", Arrays.asList(new Coordinates(1, 0), new Coordinates(4, 0)));

        Actions.placeWorkers(new LinkedHashMap<>(workers));

        Coordinates startingPosition = new Coordinates(0, 0);

        Actions.setCurrentWorker(startingPosition);

        Coordinates movePosition = new Coordinates(0, 1);
        workers.replace("Hero", Arrays.asList(movePosition, new Coordinates(4, 4)));

        Actions.selectOption(movePosition);

        Coordinates buildPosition = new Coordinates(1, 1);
        buildings.replace(1, Arrays.asList(buildPosition, new Coordinates(0, 1)));

        Actions.selectOption(buildPosition);
        GUIClient guiClient = new GUIClient();
        guiClient.run(new String[]{});
    }

    @After
    public void tearDown() {
        GameState.getInstance().dropAllConnections();
    }
}
