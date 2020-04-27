package it.polimi.ingsw.PSP4.controller.turnStates;

import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.requests.ChoosePositionRequest;
import it.polimi.ingsw.PSP4.model.GameState;
import it.polimi.ingsw.PSP4.model.Player;
import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic class for the state of the turn
 */
abstract public class State {
    private final Player player;                    //reference to current player
    private final StateType type;                   //type of the actual state
    private List<SerializablePosition> options;     //list of positions available in current state
    private Position position;                      //position chosen by the player, initially null

    //getters and setters
    protected Player getPlayer() { return player; }

    public StateType getType() { return type; }

    public List<SerializablePosition> getOptions() { return options; }
    private void setOptions(List<SerializablePosition> options) { this.options = options; }

    protected Position getPosition() { return position; }
    private void setPosition(Position position) { this.position = position; }

    public boolean canBeSkipped() { return false; }
    public boolean canChangeWorker() { return !player.isWorkerLocked(); }

    public abstract State getNextState();

    /**
     * Constructor of the class State
     * @param player reference to current player
     * @param type type of the actual state
     */
    protected State(Player player, StateType type) {
        this.player = player;
        this.type = type;
        this.options = null;
        this.position = null;
    }

    /**
     * Sets position attribute (if null), then wakes up selectOption()
     * Sets step to PERFORM_ACTION
     * @param position reference to the position chosen by the player
     */
    public void receiveOption(Position position) {
        setPosition(position);
        performAction();
    }

    /**
     * Sets position attribute to null, then wakes up selectOption()
     * Sets step to CHANGE_WORKER
     */
    public void changeWorker() {
        if (canChangeWorker()) {
            GameState.getInstance().runTurn();
        }
    }

    /**
     * Sets position attribute to null, then wakes up selectOption()
     * Sets step to SKIP_STATE
     */
    public void skipState() {
        if (canBeSkipped()) {
            getPlayer().setState(getNextState());
            GameState.getInstance().runTurn();
        }
    }

    /**
     * Gives the player a set of Position based on his card and current state
     * Sets step to WAIT_RESPONSE
     * @param options ArrayList of Position in which the action defined by the current state can be performed
     */
    protected void selectOption(ArrayList<Position> options) {
        List<SerializablePosition> serializableOptions = new ArrayList<>();
        for(Position option : options)
            serializableOptions.add(new SerializablePosition(option));
        String player = getPlayer().getUsername();
        String text;
        if(options.size() > 0)
            text = getType().getMessage();
        else
            text = MessageFormat.format(Message.NO_OPTIONS, "You");
        if(canChangeWorker())
            text += Message.CHANGE_WORKER_COMMAND;
        if(canBeSkipped())
            text += Message.SKIP_STATE_COMMAND;
        Message message = new ChoosePositionRequest(player, text, serializableOptions, canBeSkipped(), canChangeWorker());
        GameState.getInstance().notifyObservers(message);
        setOptions(serializableOptions);
    }

    /**
     * Collects the possible positions
     */
    public abstract void runState();

    /**
     * Performs the action defined by the current state
     */
    public void performAction() {
        Player player = getPlayer();
        if (options.size() == 0 && !canBeSkipped() && !canChangeWorker()) {
            player.setState(new WaitState(player));
            GameState.getInstance().playerDefeat(player, Message.NO_OPTIONS);
            return;
        }
        if (getType() == StateType.BUILD)
            player.getMechanics().build(player, getPosition());
        else if (getType() == StateType.MOVE) {
            player.getMechanics().move(player, getPosition());
            if (player.getMechanics().checkWinCondition(player)) {
                player.setState(new WaitState(player));
                GameState.getInstance().playerVictory(player);
            }
        }
        player.setState(getNextState());
        GameState.getInstance().runTurn();
    }
}
