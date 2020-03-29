package it.polimi.ingsw.PSP4.model;
import it.polimi.ingsw.PSP4.controller.GameMechanics;
import java.util.ArrayList;


public class Player {

    //attributes
    private String nickname;
    private ArrayList<Worker> workers = new ArrayList<Worker>();
    private int turnNum;
    private GameMechanics mechanics;

    //getter and setter
    public String getNickname() { return nickname; }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public int getTurnNum() { return turnNum; }

    public void increaseTurnNum() { turnNum++; }

    public GameMechanics getMechanics() { return mechanics; }

    public void setMechanics(GameMechanics mechanics) { this.mechanics = mechanics; }

    //methods
    public Player (String nickname){
        this.nickname=nickname;
        workers.add(new Worker(this));
        workers.add(new Worker(this));
        this.turnNum=1;
        this.mechanics=null;
    }


}
