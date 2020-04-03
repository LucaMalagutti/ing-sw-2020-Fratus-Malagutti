package it.polimi.ingsw.PSP4.model;

import java.util.ArrayList;

/**
 * Contains information about the game being played, its state and its board.
 * It is a singleton.
 */
public class GameState {
    private static GameState instance;                  //singleton instance

    private Position[][] board = new Position[5][5];    //5x5 grid, represents game platform
    private ArrayList<Player> players;                  //list of players
    private Player currPlayer;                          //reference to current player
    private int numPlayer;                              //number of players (2 or 3)

    //getter and setter
    public Position[][] getBoard() { return board; }

    public Player getCurrPlayer() {
        return currPlayer;
    }
    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    public int getNumPlayer() {
        return numPlayer;
    }
    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Constructor of the class GameState
     * Builds the board creating Position objects
     */
    private GameState(){
        this.currPlayer = null;
        this.numPlayer = -1;
        this.players = null;
        for(int row=0; row<board.length; row++){
            for(int col=0; col<board[row].length; col++){
                board[row][col]= new Position(row,col,this);
            }
        }
    }

    /**
     * @return single instance of GameState
     */
    public static GameState getInstance(){
        if(instance==null)
            instance = new GameState();
        return instance;
    }
}
