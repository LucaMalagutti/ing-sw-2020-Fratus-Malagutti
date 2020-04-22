package it.polimi.ingsw.PSP4.model;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.controller.turnStates.State;
import it.polimi.ingsw.PSP4.controller.turnStates.StateType;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.requests.AssignGodRequest;
import it.polimi.ingsw.PSP4.message.requests.ChooseAllowedGodsRequest;
import it.polimi.ingsw.PSP4.message.requests.ChooseStartingPlayerRequest;
import it.polimi.ingsw.PSP4.observer.Observable;
import it.polimi.ingsw.PSP4.observer.Observer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains information about the game being played, its state and its board.
 * It is a singleton.
 */
public class GameState implements Observable<Message> {
    private static GameState instance;                          //singleton instance
    private final ArrayList<Observer<Message>> observers = new ArrayList<>();

    private final Position[][] board = new Position[5][5];      //5x5 grid, represents game platform
    private ArrayList<Player> players;                          //list of players
    private Player currPlayer;                                  //reference to current player
    private int numPlayer;                                      //number of players (2 or 3)
    private List<GodType> allowedGods;                          //gods the player can use during this game

    //getter and setter
    public Position[][] getBoard() { return board; }
    public Position getPosition(int row, int col) { return board[row][col]; }

    public Player getCurrPlayer() { return currPlayer; }
    public void setCurrPlayer(Player currPlayer) { this.currPlayer = currPlayer; }
    public Player getNextPlayer() { return players.get((players.indexOf(currPlayer) + 1) % numPlayer); }
    private void skipPlayer() { setCurrPlayer(getNextPlayer()); }

    public int getNumPlayer() { return numPlayer; }
    public void setNumPlayer(int numPlayer) { this.numPlayer = numPlayer; }

    public ArrayList<Player> getPlayers() {return new ArrayList<>(players);}
    public void setPlayers(ArrayList<Player> players) {this.players = players;}
    public void addPlayer(Player player) { this.players.add(player);}
    public void removePlayer(Player player) { this.players.remove(player);}

    public List<GodType> getAllowedGods() { return new ArrayList<>(allowedGods);}
    public synchronized void setAllowedGods(List<GodType> allowedGods) { this.allowedGods = allowedGods; }

    /**
     * Constructor of the class GameState
     * Builds the board creating Position objects
     */
    private GameState(){
        this.currPlayer = null;
        this.players = new ArrayList<>();
        for(int row=0; row<board.length; row++){
            for(int col=0; col<board[row].length; col++){
                board[row][col] = new Position(row, col);
            }
        }
        for(int row=0; row<board.length; row++){
            for(int col=0; col<board[row].length; col++){
                board[row][col].setUpNeighbors(row, col, this);
            }
        }
    }

    /**
     * @return single instance of GameState, null if not initialized
     */
    public static GameState getInstance() {
        return getInstance(false);
    }
    /**
     * @param create if true and instance == null creates a new GameState
     * @return single instance of GameState
     */
    public static GameState getInstance(boolean create) {
        if(create && instance == null)
            instance = new GameState();
        return instance;
    }

    /**
     * @return ArrayList of Position of the board (1D)
     */
    public ArrayList<Position> getFlatBoard() {
        ArrayList<Position> flatBoard = new ArrayList<>();
        for(Position[] line : board){
            flatBoard.addAll(Arrays.asList(line));
        }
        return flatBoard;
    }

    public Player getPlayerFromUsername (String username) {
        for (Player player:getPlayers()) {
            if (player.getUsername().equals(username))
                return player;
        }
        return null;
    }

    //TODO move this to Controller?
    public void startGame() {
        System.out.println(MessageFormat.format(Message.GAME_STARTED, getNumPlayer()));
        chooseAllowedGods();
    }

    /**
     * Starts sending a ChooseAllowedGodMessage to the first player
     */
    public void chooseAllowedGods() {
        String message = MessageFormat.format(Message.CHOOSE_ALLOWED_GODS, getNumPlayer());
        List<String> implementedGodList = GodType.getImplementedGodsList();
        notifyObservers(new ChooseAllowedGodsRequest(currPlayer.getUsername(), message, implementedGodList));
    }

    public void assignGod() {
        this.skipPlayer();
        notifyObservers(new AssignGodRequest(currPlayer.getUsername(),
            getAllowedGods().stream().map(Enum::toString).collect(Collectors.toList())));
    }

    public void chooseStartingPlayer() {
        List<String> playerList = this.getPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
        notifyObservers(new ChooseStartingPlayerRequest(this.getCurrPlayer().getUsername(), playerList));
    }

    public void placeWorkers() {
        //TODO implement this
        System.out.println(this.getCurrPlayer().getUsername());
    }

    /**
     * Asks currPlayer to select a worker, then sets it as current
     */
    private void selectWorker() {
        //TODO: implement this to choose a worker
        //Worker worker = ???
        //getCurrPlayer().setCurrWorker(worker);
    }

    /**
     * If no player is playing, moves currPlayer pointer forward and prepares the new player for the turn
     */
    public synchronized void newTurn() {
        for(Player player : getPlayers())
            if(player.getState().getType() != StateType.WAIT)
                return;
        skipPlayer();
        getCurrPlayer().newTurn();
    }

    /**
     * Runs the turn until the currPlayer's state is WAIT
     */
    public synchronized void runTurn() {
        while(getCurrPlayer().getState().getType() != StateType.WAIT) {
            if(!getCurrPlayer().isWorkerLocked())
                selectWorker();
            State nextState = getCurrPlayer().getState().performAction();
            getCurrPlayer().setState(nextState);
        }
        getCurrPlayer().endTurn();
    }

    @Override
    public void notifyObservers(Message message) {
        synchronized (observers) {
            for (Observer<Message> obs: observers) {
                obs.update(message);
            }
        }
    }

    @Override
    public void addObserver(Observer<Message> o) {
        synchronized (observers) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<Message> o) {
        synchronized (observers) {
            observers.remove(o);
        }
    }
}
