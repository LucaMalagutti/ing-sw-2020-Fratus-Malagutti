package it.polimi.ingsw.PSP4.model;

public class Worker {

    //attributes
    private Position currPosition;
    private Position prevPosition;
    private final Player player;

    //getter and setter
    public Position getCurrPosition() {
        return currPosition;
    }

    public void setCurrPosition(Position currPosition) {
        this.currPosition = currPosition;
    }

    public Position getPrevPosition() {
        return prevPosition;
    }

    public void setPrevPosition(Position prevPosition) {
        this.prevPosition = prevPosition;
    }

    public Player getPlayer() { return player; }

    public Worker(Player player){
        this.player=player;
        this.currPosition=null;
        this.prevPosition=null;

    }

}
