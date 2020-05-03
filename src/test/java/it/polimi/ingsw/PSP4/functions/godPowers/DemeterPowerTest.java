package it.polimi.ingsw.PSP4.functions.godPowers;

import it.polimi.ingsw.PSP4.model.GameState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DemeterPowerTest {
    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsed_standardBuild() {}

    @Test
    public void powerUsed_doubleBuild_notSamePosition() {}

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
