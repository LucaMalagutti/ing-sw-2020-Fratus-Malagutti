package it.polimi.ingsw.PSP4.model;

import it.polimi.ingsw.PSP4.utils.Tests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

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

    @After
    public void tearDown() {
        GameState.getInstance().dropAllConnections();
    }
}
