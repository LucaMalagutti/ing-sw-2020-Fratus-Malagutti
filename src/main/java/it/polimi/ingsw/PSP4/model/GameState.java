package it.polimi.ingsw.PSP4.model;

public class GameState {

    //singleton instance
    private static GameState instance;

    //attributes
    private Position[][] board = new Position[5][5];
    private Player currPlayer;
    private int numPlayer;
    private Player[] players;

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

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    //methods
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

    public static GameState getIstance(){
        if(instance==null)
            instance = new GameState();

        return instance;
    }


}
