package it.polimi.ingsw.PSP4.functions.godPowers;

import it.polimi.ingsw.PSP4.model.GameState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AtlasPowerTest {
    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsed_standardBuild() {}

    @Test
    public void powerNotPossible_standardBuild_noConfirmation() {}

    @Test
    public void powerUsed_buildDome() {}

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
