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
    public void gameState_guiTest() {
        Runner.workersPlacement(3, 0);
        Map<Integer, List<Coordinates>> buildings = new LinkedHashMap<>();
        buildings.put(1, Arrays.asList(new Coordinates(0, 1), new Coordinates(0, 2)));
        buildings.put(2, Collections.singletonList(new Coordinates(1, 0)));
        buildings.put(3, Collections.singletonList(new Coordinates(2, 1)));
        buildings.put(4, Arrays.asList(new Coordinates(0, 0), new Coordinates(1, 3)));
        Actions.fillBoard(buildings);
        Actions.setCurrentWorker(Getters.workersOnBoard().get(Getters.currentPlayer()).get(0));
        GUIClient guiClient = new GUIClient();
        guiClient.run(new String[]{});
    }

    @After
    public void tearDown() {
        GameState.getInstance().dropAllConnections();
    }
}
