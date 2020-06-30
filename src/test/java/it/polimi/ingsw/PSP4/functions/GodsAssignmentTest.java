package it.polimi.ingsw.PSP4.functions;

import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.utils.Actions;
import it.polimi.ingsw.PSP4.utils.Random;
import it.polimi.ingsw.PSP4.utils.Tests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

public class GodsAssignmentTest {
    @Before
    public void setUp() { GameState.getInstance(true).dropAllConnections(); }

    @Test
    public void godsAssignment_randomParameters() {
        int numPlayer = Random.numberOfPlayers();
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = Random.godsAssignments(new ArrayList<>(players));
        String startingPlayer = Random.player(new ArrayList<>(players));

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardEmpty());
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
    }

    @Test
    public void godsAssignment_twoPlayers_firstConnected() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = Random.godsAssignments(new ArrayList<>(players));
        String startingPlayer = players.get(0);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardEmpty());
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
    }

    @Test
    public void godsAssignment_twoPlayers_secondConnected() {
        int numPlayer = 2;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = Random.godsAssignments(new ArrayList<>(players));
        String startingPlayer = players.get(1);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardEmpty());
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
    }

    @Test
    public void godsAssignment_threePlayers_firstConnected() {
        int numPlayer = 3;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = Random.godsAssignments(new ArrayList<>(players));
        String startingPlayer = players.get(0);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardEmpty());
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
    }

    @Test
    public void godsAssignment_threePlayers_secondConnected() {
        int numPlayer = 3;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = Random.godsAssignments(new ArrayList<>(players));
        String startingPlayer = players.get(1);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardEmpty());
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
    }

    @Test
    public void godsAssignment_threePlayers_thirdConnected() {
        int numPlayer = 3;
        List<String> players = Random.playerList(numPlayer);
        Map<String, String> gods = Random.godsAssignments(new ArrayList<>(players));
        String startingPlayer = players.get(2);

        Actions.addPlayers(new ArrayList<>(players));
        Actions.assignGods(new LinkedHashMap<>(gods));
        Actions.selectStartingPlayer(startingPlayer);

        Assert.assertTrue(Tests.gameStateExists());
        Assert.assertTrue(Tests.boardEmpty());
        Assert.assertTrue(Tests.godsAssignments(new LinkedHashMap<>(gods)));
        Assert.assertTrue(Tests.currentPlayer(startingPlayer));
        Assert.assertTrue(Tests.numberOfPlayers(numPlayer));
        Assert.assertTrue(Tests.allowedGodsEmpty());
    }

    @After
    public void tearDown() { GameState.getInstance().dropAllConnections(); }
}
