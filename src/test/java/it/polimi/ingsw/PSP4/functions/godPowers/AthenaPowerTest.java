package it.polimi.ingsw.PSP4.functions.godPowers;

import it.polimi.ingsw.PSP4.model.GameState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AthenaPowerTest {
    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsed_enemyStandardMove() {}

    @Test
    public void powerUsed_enemyNotUpMove_twoPlayers() {}

    @Test
    public void powerUsed_enemyNotUpMove_threePlayers() {}

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
