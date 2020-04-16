package it.polimi.ingsw.PSP4.model.serializable;

import it.polimi.ingsw.PSP4.model.GameState;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Serializable "light" copy of GameState, contains information to be sent to all the players
 */
public final class SerializableGameState implements Serializable {
    private static final long serialVersionUID = 8734470972752644234L;

    private final ArrayList<SerializablePosition> board = new ArrayList<>();  //copy of GameState (straight) board
    private final ArrayList<SerializablePlayer> players = new ArrayList<>();  //copy of GameState players
    private final int currPlayerIndex;                                        //index of the current player
    private final int numPlayer;                                              //number of players (2 or 3)

    /**
     * Constructor of the class SerializableGameState
     * @param gameState GameState to serialize
     */
    public SerializableGameState(GameState gameState) {
        gameState.getStraightBoard().forEach(position -> this.board.add(new SerializablePosition(position)));
        gameState.getPlayers().forEach(player -> this.players.add(new SerializablePlayer(player)));
        this.currPlayerIndex = gameState.getPlayers().indexOf(gameState.getCurrPlayer());
        this.numPlayer = gameState.getNumPlayer();
    }

    //getter and setter
    public ArrayList<SerializablePosition> getBoard() { return new ArrayList<>(board); }

    public ArrayList<SerializablePlayer> getPlayers() { return new ArrayList<>(players); }

    public int getCurrPlayerIndex() { return currPlayerIndex; }
    public SerializablePlayer getCurrPlayer() { return players.get(currPlayerIndex); }

    public int getNumPlayer() { return numPlayer; }
}
