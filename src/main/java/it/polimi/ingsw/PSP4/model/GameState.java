package it.polimi.ingsw.PSP4.model;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.observer.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Contains information about the game being played, its state and its board.
 * It is a singleton.
 */
public class GameState implements Observable<Message> {
    private ArrayList<GodType> allowedGods;
    private static GameState instance;                          //singleton instance
    private final ArrayList<Observer<Message>> observers = new ArrayList<>();

    private final Position[][] board = new Position[5][5];      //5x5 grid, represents game platform
    private ArrayList<Player> players;                          //list of players
    private Player currPlayer;                                  //reference to current player
    private int numPlayer;                                      //number of players (2 or 3)

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
    public void addPlayer(Player player) { this.players.add(player); }
    public void removePlayer(Player player) { this.players.remove(player); }

    /**
     * Constructor of the class GameState
     * Builds the board creating Position objects
     */
    private GameState(){
        this.currPlayer = null;
        this.players = null;
        for(int row=0; row<board.length; row++){
            for(int col=0; col<board[row].length; col++){
                board[row][col] = new Position(row,col,this);
            }
        }
        for(int row=0; row<board.length; row++){
            for(int col=0; col<board[row].length; col++){
                board[row][col].setUpNeighbors(row, col, this);
            }
        }
    }

    /**
     * @return ArrayList of Position of the board (1D)
     */
    public ArrayList<Position> getFlatBoard() {
        ArrayList<Position> flatBoard = new ArrayList<>();
        for(Position[] line : board){
            flatBoard.addAll(Arrays.asList(line));
        }
        return flatBoard;
    }

    public void startGame() {
     chooseAllowedGods();
    }

    public void chooseAllowedGods() {
        //TODO implement this after defining Message stucture
        //notifyObservers(new Message(""));
    }

    /**
     * @return single instance of GameState
     */
    public static GameState getInstance(){
        if(instance==null)
            instance = new GameState();
        return instance;
    }

    @Override
    public void notifyObservers(Message message) {
        synchronized (observers) {
            for (Observer<Message> obs: observers) {
                obs.update(message);
            }
        }
    }

    @Override
    public void addObserver(Observer<Message> o) {
        synchronized (observers) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<Message> o) {
        synchronized (observers) {
            observers.remove(o);
        }
    }
}
