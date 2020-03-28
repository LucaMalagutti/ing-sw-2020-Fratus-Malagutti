package it.polimi.ingsw.PSP4.model;

public class Player {

    //attributi
    private String nickname;
    private Worker[] workers;
    private int turnNum;
    private GameMechanics mechanics;

    //getter e setter
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Worker[] getWorkers() {
        return workers;
    }

    public void setWorkers(Worker[] workers) {
        this.workers = workers;
    }

    public int getTurnNum() {
        return turnNum;
    }

    public void setTurnNum(int turnNum) {
        this.turnNum = turnNum;
    }

    public GameMechanics getMechanics() {
        return mechanics;
    }

    public void setMechanics(GameMechanics mechanics) {
        this.mechanics = mechanics;
    }

}
