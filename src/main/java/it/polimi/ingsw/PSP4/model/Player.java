package it.polimi.ingsw.PSP4.model;

import it.polimi.ingsw.PSP4.controller.*;

import java.util.ArrayList;

/**
 * Contains information about the state of one player
 */
public class Player {
    private final String username;                              //Player's username
    private ArrayList<Worker> workers = new ArrayList<>();      //list of player's workers
    private Worker currWorker;                                  //reference to current worker
    private boolean workerLocked;                               //if true current worker can't be changed
    private int turnNum;                                        //number of player's turn
    private State state;                                        //state of player's turn
    private GameMechanics mechanics;                            //card of the player

    //getter and setter
    public String getUsername() { return username; }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public Worker getCurrWorker() { return currWorker; }
    public void setCurrWorker(Worker worker) { if(!isWorkerLocked()) this.currWorker = worker; }

    public boolean isWorkerLocked() { return workerLocked; }
    public void lockWorker() { this.workerLocked = true; }
    private void unlockWorker() { this.workerLocked = false; }

    public int getTurnNum() { return turnNum; }
    public void increaseTurnNum() { turnNum++; }

    public State getState() { return state; }
    public void setState(State state) { this.state = state; }

    public GameMechanics getMechanics() { return mechanics; }
    public void setMechanics(GameMechanics mechanics) { this.mechanics = mechanics; }

    /**
     * Constructor of the class Player
     * Creates Worker objects
     * @param username unique name of the player
     */
    public Player (String username){
        this.username = username;
        workers.add(new Worker());
        workers.add(new Worker());
        this.currWorker = null;
        this.workerLocked = false;
        this.turnNum = 1;
        this.state = new WaitState();
        this.mechanics = null;
    }

    /**
     * Asks the player to select a worker, then sets it as current
     */
    public void selectWorker() {
        //To be implemented
    }

    /**
     * Prepares the player for a new turn
     */
    public void newTurn() {
        increaseTurnNum();
        unlockWorker();
        setCurrWorker(null);
        if (getMechanics().getPath() == PathType.EARLY_BUILD)
            setState(new EarlyBuildState());
        else
            setState(new StandardMoveState());
    }

    /**
     * Runs the turn until the player's state is WAIT
     */
    public void runTurn() {
        while(getState().getType() != StateType.WAIT) {
            if(!isWorkerLocked())
                selectWorker();
            State nextState = getState().performAction(this);
            setState(nextState);
        }
    }
}
