package it.polimi.ingsw.PSP4.model;

import java.util.UUID;

/**
 *  Contains the state of one of the Player's worker
 */
public class Worker {
    private final UUID id;              //universally unique identifier of the worker
    private Position currPosition;      //reference to the current position of the worker
    private Position prevPosition;      //reference to the previous position of the worker

    //getter and setter
    public String getId() { return id.toString(); }

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

    /**
     * Constructor of the class Worker
     */
    public Worker(){
        this.id = UUID.randomUUID();
        this.currPosition=null;
        this.prevPosition=null;
    }
}
