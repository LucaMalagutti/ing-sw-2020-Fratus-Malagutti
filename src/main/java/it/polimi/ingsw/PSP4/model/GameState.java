package it.polimi.ingsw.PSP4.model;

public class GameState {

    //singleton instance
    private static GameState instance;

    private GameState(){
    }

    public static GameState getIstance(){
        if(instance==null)
            instance = new GameState();

        return instance;
    }

    //attributi
    private Position[][] board;
    private Player currPlayer;
    private int numPlayer;
    private Player[] players;

    //getter e setter
    public Position[][] getPosition() {
        return board;
    }

    public void setPosition(Position[][] position) {
        this.board = position;
    }

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

}
