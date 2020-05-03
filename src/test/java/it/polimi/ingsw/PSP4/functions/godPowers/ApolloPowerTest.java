package it.polimi.ingsw.PSP4.functions.godPowers;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.utils.Actions;
import it.polimi.ingsw.PSP4.utils.Coordinates;
import it.polimi.ingsw.PSP4.utils.Tests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class ApolloPowerTest {
    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void powerNotUsed_standardMove() {}

    @Test
    public void powerUsed_swapWithEnemyWorker() {}
}
