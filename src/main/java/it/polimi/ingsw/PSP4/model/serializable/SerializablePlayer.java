package it.polimi.ingsw.PSP4.model.serializable;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Worker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serializable "light" copy of Player
 */
public final class SerializablePlayer implements Serializable {
    private static final long serialVersionUID = 1173042865735757389L;

    private final String username;                                  //player's username
    private final ArrayList<String> workers = new ArrayList<>();    //list of player's worker ids
    private final ArrayList<SerializablePosition> workersPositions = new ArrayList<>();
    private final int turnNum;                                      //number of player's turn
    private final String state;                                     //String that represents the state of the player
    private final GodType card;                                     //type of the player's card
    private final List<String> wrappers;                            //name of the gods that are wrapping the player
    private final int currWorkerIndex;                              //current player's worker

    /**
     * Constructor of the class SerializablePlayer
     * @param player Player to serialize
     */
    public SerializablePlayer(Player player) {
        this.username = player.getUsername();
        for(Worker worker : player.getWorkers()) {
            this.workers.add(worker.getId());
            if(worker.getCurrPosition() != null)
                this.workersPositions.add(new SerializablePosition(worker.getCurrPosition()));
        }
        this.currWorkerIndex = player.getWorkers().indexOf(player.getCurrWorker());
        this.turnNum = player.getTurnNum();
        this.state = player.getState().getType().getString();
        this.card = player.getMechanics().getType();
        this.wrappers = player.getMechanics().getEvilList().stream().map(GodType::getName).collect(Collectors.toList());
    }

    //getter and setter
    public String getUsername() { return username; }

    public ArrayList<String> getWorkers() { return new ArrayList<>(workers); }

    public ArrayList<SerializablePosition> getWorkersPositions() { return new ArrayList<>(workersPositions); }

    public int getTurnNum() { return turnNum; }

    public String getState() { return state; }

    public GodType getCard() { return card; }

    public List<String> getWrappers() { return wrappers; }

    public String getCurrWorker() {
        if(currWorkerIndex == -1)
            return null;
        else
            return workers.get(currWorkerIndex);
    }
}
