package it.polimi.ingsw.PSP4.model;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.DefaultGameMechanics;
import it.polimi.ingsw.PSP4.controller.cardsMechanics.GameMechanics;
import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.controller.cardsMechanics.PathType;
import it.polimi.ingsw.PSP4.controller.turnStates.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains information about the state of one player
 */
public class Player {
    private final String username;                                  //Player's username
    private final ArrayList<Worker> workers = new ArrayList<>();    //list of player's workers
    private Worker currWorker;                                      //reference to current worker
    private boolean workerLocked;                                   //if true current worker can't be changed
    private int turnNum;                                            //number of player's turn
    private State state;                                            //state of player's turn
    private GameMechanics mechanics;                                //card of the player
    private final ArrayList<Worker> stuckWorkers = new ArrayList<>();//list of worker have no move options in this turn

    //getter and setter
    public String getUsername() { return username; }

    public ArrayList<Worker> getWorkers() {
        return new ArrayList<>(workers);
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

    public ArrayList<Worker> getStuckWorkers() { return stuckWorkers; }
    public void emptyStuckWorker() { stuckWorkers.clear();}
    public void addCurrentWorkerAsStuck() {
        if (stuckWorkers.size() < 2 && !stuckWorkers.contains(getCurrWorker()))
            stuckWorkers.add(getCurrWorker());
    }

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
        this.turnNum = 0;
        this.state = new WaitState(this);
        this.mechanics = new DefaultGameMechanics();
    }

    /**
     * Wrap the mechanics with origin's evil mechanics
     * @param origin player from which the event started
     */
    public void wrapMechanics(Player origin) {
        if(origin == this)
            return;
        List<GodType> presentWrappers = mechanics.getEvilList();
        GodType newWrapper = origin.getMechanics().getType();
        if(presentWrappers.contains(newWrapper))
            return;
        GameMechanics newMechanics = newWrapper.getGameMechanics();
        newMechanics.setEvil();
        newMechanics.setComponent(getMechanics());
        setMechanics(newMechanics);
    }

    /**
     * Unwrap the mechanics from origin's evil mechanics
     * @param origin player from which the event started
     */
    public void unwrapMechanics(Player origin) {
        if(origin == this)
            return;
        List<GodType> presentWrappers = mechanics.getEvilList();
        GodType oldWrapper = origin.getMechanics().getType();
        if(!presentWrappers.contains(oldWrapper))
            return;
        GameMechanics extMechanics = null;
        GameMechanics oldMechanics = getMechanics();
        while(oldMechanics.getRealType() != oldWrapper) {
            extMechanics = oldMechanics;
            oldMechanics = oldMechanics.getComponent();
        }
        if(extMechanics == null)
            setMechanics(oldMechanics.getComponent());
        else
            extMechanics.setComponent(oldMechanics.getComponent());
    }

    /**
     * If the player is not playing, prepares it for a new turn
     */
    public void newTurn() {
        if(getState().getType() == StateType.WAIT) {
            increaseTurnNum();
            unlockWorker();
            setCurrWorker(null);
            if (getMechanics().getPath() == PathType.EARLY_BUILD)
                setState(new EarlyBuildState(this));
            else
                setState(new StandardMoveState(this));
        }
    }

    /**
     * If the player is not playing, locks currWorker as null (for safety)
     */
    public void endTurn() {
        if(getState().getType() == StateType.WAIT) {
            unlockWorker();
            setCurrWorker(null);
            lockWorker();
        }
    }
}
