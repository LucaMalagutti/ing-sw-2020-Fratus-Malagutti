package it.polimi.ingsw.PSP4.model.serializable;

import it.polimi.ingsw.PSP4.model.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Serializable "light" copy of Player
 */
public final class SerializablePlayer implements Serializable {
    private static final long serialVersionUID = 1173042865735757389L;

    private final String username;                                  //player's username
    private final ArrayList<String> workers = new ArrayList<>();    //list of player's worker ids
    private final int turnNum;                                      //number of player's turn
    private final String state;                                     //String that represents the state of the player
    private final String card;                                      //name of the player's card

    /**
     * Constructor of the class SerializablePlayer
     * @param player Player to serialize
     */
    public SerializablePlayer(Player player) {
        this.username = player.getUsername();
        player.getWorkers().forEach(worker -> this.workers.add(worker.getId()));
        this.turnNum = player.getTurnNum();
        this.state = player.getState().getType().getString();
        this.card = player.getMechanics().getName();
    }

    //getter and setter
    public String getUsername() { return username; }

    public ArrayList<String> getWorkers() { return new ArrayList<>(workers); }

    public int getTurnNum() { return turnNum; }

    public String getState() { return state; }

    public String getCard() { return card; }
}
