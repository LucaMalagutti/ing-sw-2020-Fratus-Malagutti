package it.polimi.ingsw.PSP4.model;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.*;
import it.polimi.ingsw.PSP4.controller.turnStates.State;
import it.polimi.ingsw.PSP4.controller.turnStates.StateType;
import it.polimi.ingsw.PSP4.message.Message;
import it.polimi.ingsw.PSP4.message.requests.*;
import it.polimi.ingsw.PSP4.model.serializable.SerializableGameState;
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
    private static volatile GameState instance;                 //singleton instance
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
        if(instance != null)
            throw new RuntimeException("Use method getInstance() instead.");
        this.currPlayer = null;
        this.players = new ArrayList<>();
        for(int row=0; row<board.length; row++){
            for(int col=0; col<board[row].length; col++){
                board[row][col] = new Position(row, col);
            }
        }
        for(int row=0; row<board.length; row++){
            for(int col=0; col<board[row].length; col++){
                //Placed in reset() shouldn't need a reference to GameState as a parameter
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
        if(create && instance == null) {
            synchronized (GameState.class) {
                if(instance == null)
                    instance = new GameState();
            }
        }
        return instance;
    }

    /**
     * Puts the singleton in a "clean" state, used when a new game starts
     */
    //TODO: must be private (public for testing)
    private synchronized void reset() {
        //TODO: debug (important!)
        instance = null;
        getInstance(true);
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

    /**
     * @param username name of the player to get
     * @return reference to the player with that username or null if not exists
     */
    public Player getPlayerFromUsername (String username) {
        for (Player player:getPlayers()) {
            if (player.getUsername().equals(username))
                return player;
        }
        return null;
    }

    /**
     * Starts the game
     */
    public void startGame() {
        //TODO move this to Controller?
        System.out.println(MessageFormat.format(Message.GAME_STARTED, getNumPlayer()));
        chooseAllowedGods();
    }

    /**
     * Starts sending a ChooseAllowedGodMessage to the first player
     */
    public void chooseAllowedGods() {
        List<String> implementedGodList = GodType.getImplementedGodsList();
        notifyObservers(new ChooseAllowedGodsRequest(currPlayer.getUsername(), implementedGodList, getNumPlayer()));
    }

    /**
     * Sends to the next player the list of gods to choose from
     */
    public void assignGod() {
        skipPlayer();
        notifyObservers(new AssignGodRequest(currPlayer.getUsername(),
            getAllowedGods().stream().map(Enum::toString).collect(Collectors.toList())));
    }

    /**
     * Sends to the first player connected the list of players to choose who will make the first move
     */
    public void chooseStartingPlayer() {
        List<String> playerList = this.getPlayers().stream().map(Player::getUsername).collect(Collectors.toList());
        notifyObservers(new ChooseStartingPlayerRequest(this.getCurrPlayer().getUsername(), playerList));
    }

    public void firstWorkerPlacement(int numPl, int numWorker) {
        if (numPl == getNumPlayer() && numWorker == 0) {
            GameState.getInstance().newTurn();
        } else {
            if (numWorker == 0 && numPl != 0)
                skipPlayer();
            SerializableGameState board = new SerializableGameState();
            notifyObservers(new AssignFirstWorkerPlacementRequest(getCurrPlayer().getUsername(),board, numPl, numWorker));
        }
    }

    /**
     * Sets current player's active worker and wakes runTurn()
     * @param worker worker selected by the player
     */
    public synchronized void receiveWorker(Worker worker) {
        getCurrPlayer().setCurrWorker(worker);
        notifyAll();
    }

    /**
     * Asks currPlayer to select a worker, then sets it as current
     */
    private synchronized void selectWorker() {
        List<int[]> workers = new ArrayList<>();
        for(Worker worker : getCurrPlayer().getWorkers()) {
            Position position = worker.getCurrPosition();
            int[] coordinates = {position.getRow(), position.getCol()};
            workers.add(coordinates);
        }
        notifyObservers(new ChooseWorkerRequest(getCurrPlayer().getUsername(), workers));
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
            if(!getCurrPlayer().isWorkerLocked()) {
                getCurrPlayer().setCurrWorker(null);
                selectWorker();
                while(getCurrPlayer().getCurrWorker() == null) {
                    try {
                        wait();
                    } catch(InterruptedException e) {
                        //TODO: handle exception
                    }
                }
            }
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

    /**
     * Checks if there are enough players to continue the game
     * If so proceeds removing player from the game and informs all the players
     * @param player player which cannot continue the game
     * @param message reason for defeat
     */
    public void playerDefeat(Player player, String message) {
        //end player's turn
        player.endTurn();
        //move currentPlayer to next player
        newTurn();
        //remove player from the list
        removePlayer(player);
        if(getPlayers().size() == 1) {
            //the game cannot continue
            playerVictory(getPlayers().get(0));
            return;
        }
        //the game can continue
        //remove player's workers from the board
        ArrayList<Worker> workers = player.getWorkers();
        for(Position[] line : board) {
            for(Position position : line) {
                if(workers.contains(position.getWorker()))
                    position.setWorker(null);
            }
        }
        //unwrap enemies (useless if not ATHENA)
        player.getMechanics().playerDefeat(player);
        //close player's connection and inform other players
        notifyObservers(new RemovePlayerRequest(player.getUsername(), false));
    }

    /**
     * Informs all the players that the game is over, electing the winner
     * @param player winner of the game
     */
    public void playerVictory(Player player) {
        //close every connection notifying the players
        notifyObservers(new RemovePlayerRequest(player.getUsername(), true));
        //cleans the GameState singleton for a new game
        reset();
    }

    /**
     * Drops all client connection after a client left without surrendering
     */
    public void dropAllConnections() {
        notifyObservers(new RemovePlayerRequest("@", false));
        GameState.getInstance().reset();
    }
}
