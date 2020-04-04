package it.polimi.ingsw.PSP4.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PositionTest {
    GameState gameState;
    @Before
    public void setUp() {
        gameState = GameState.getInstance();
        gameState.setPlayers(new ArrayList<Player>(Arrays.asList(new Player("1"), new Player("2"))));
        gameState.setNumPlayer(2);
        gameState.setCurrPlayer(gameState.getPlayers().get(0));
        gameState.getBoard()[2][3].setWorker(gameState.getPlayers().get(1).getWorkers().get(0));
        gameState.getBoard()[3][3].setWorker(gameState.getCurrPlayer().getWorkers().get(1));
        gameState.getBoard()[1][1].setWorker(gameState.getPlayers().get(1).getWorkers().get(1));
        gameState.getBoard()[3][1].setDome(true);
    }

    @Test
    public void getNeighbour_middleCell_shouldReturn8Cells() {
        Position testedPosition = gameState.getBoard()[3][3];
        assertEquals(8, testedPosition.getNeighbour().size());
    }

    @Test
    public void getNeighbour_sideCell_shouldReturn5Cells() {
        Position testedPosition = gameState.getBoard()[0][2];
        assertEquals(5, testedPosition.getNeighbour().size());
    }

    @Test
    public void getNeighbour_cornerCell_shouldReturn3Cells() {
        Position testedPosition = gameState.getBoard()[4][0];
        assertEquals(3, testedPosition.getNeighbour().size());
    }

    @Test
    public void getReachableHeight_sideCell_shouldReturn2ReachableCells() {
        gameState.getBoard()[0][1].setHeight(2);
        gameState.getBoard()[0][3].setHeight(3);
        gameState.getBoard()[1][2].setHeight(1);
        Position testedPosition = gameState.getBoard()[0][2];
        assertEquals(3, testedPosition.getReachableHeight().size());
    }

    @Test
    public void getFree_middleCellTwoWorkersOneDome_shouldReturn5FreeCells() {
        Position testedPosition = gameState.getBoard()[2][2];
        assertEquals(4,testedPosition.getFree().size());
    }

    @Test
    public void getOccupied_oneEnemyWorker_shouldReturnOneOccupiedCell() {
        Position testedPosition = gameState.getBoard()[2][2];
        testedPosition.setWorker(gameState.getCurrPlayer().getWorkers().get(0));
        assertEquals(2,testedPosition.getOccupied(gameState.getCurrPlayer()).size());
    }
}