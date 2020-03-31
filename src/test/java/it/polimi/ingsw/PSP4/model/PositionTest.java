package it.polimi.ingsw.PSP4.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {
    GameState gameState;
    @Before
    public void setUp() throws Exception {
        gameState = GameState.getIstance();
        gameState.setPlayers(new Player[]{new Player("1"), new Player("2")});
        gameState.setNumPlayer(2);
        gameState.setCurrPlayer(gameState.getPlayers()[0]);
        gameState.getBoard()[2][3].setWorker(gameState.getPlayers()[1].getWorkers().get(0));
        gameState.getBoard()[3][3].setWorker(gameState.getCurrPlayer().getWorkers().get(1));
        gameState.getBoard()[1][1].setWorker(gameState.getPlayers()[1].getWorkers().get(1));
        gameState.getBoard()[3][1].setDome(true);
    }

    @Test
    public void getNeighbour_middleCell_shouldReturn8Cells() {
        Position testedPosition = new Position(3,3,gameState);
        assertEquals(8, testedPosition.getNeighbour().size());
    }

    @Test
    public void getNeighbour_sideCell_shouldReturn5Cells() {
        Position testedPosition = new Position(0,2,gameState);
        assertEquals(5, testedPosition.getNeighbour().size());
    }

    @Test
    public void getNeighbour_cornerCell_shouldReturn3Cells() {
        Position testedPosition = new Position(4,0,gameState);
        assertEquals(3, testedPosition.getNeighbour().size());
    }

    @Test
    public void getReachableHeight_sideCell_shouldReturn2ReachableCells() {
        gameState.getBoard()[0][1].setHeight(2);
        gameState.getBoard()[0][3].setHeight(3);
        gameState.getBoard()[1][2].setHeight(1);
        Position testedPosition = new Position(0,2,gameState);
        assertEquals(3, testedPosition.getReachableHeight().size());
    }

    @Test
    public void getFree_middleCellTwoWorkersOneDome_shouldReturn5FreeCells() {
        Position testedPosition = new Position(2,2,gameState);
        assertEquals(4,testedPosition.getFree().size());
    }

    @Test
    public void getOccupied_oneEnemyWorker_shouldReturnOneOccupiedCell() {
        Position testedPosition = new Position(2,2,gameState);
        testedPosition.setWorker(gameState.getCurrPlayer().getWorkers().get(0));
        assertEquals(2,testedPosition.getOccupied().size());
    }
}