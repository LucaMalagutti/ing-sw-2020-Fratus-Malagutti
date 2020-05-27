package it.polimi.ingsw.PSP4.model;

import it.polimi.ingsw.PSP4.controller.cardsMechanics.GodType;
import it.polimi.ingsw.PSP4.controller.turnStates.StateType;
import it.polimi.ingsw.PSP4.message.requests.*;
import it.polimi.ingsw.PSP4.model.serializable.SerializableGameState;
import it.polimi.ingsw.PSP4.observer.Observable;
import it.polimi.ingsw.PSP4.observer.Observer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains information about the game being played, its state and its board.
 * It is a singleton.
 */
public class GameState implements Observable<Request> {
    private static volatile GameState instance = null;          //singleton instance
    private final ArrayList<Observer<Request>> observers = new ArrayList<>();

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
        this.players = new ArrayList<>();
        this.currPlayer = null;
        this.numPlayer = 0;
        this.allowedGods = new ArrayList<>();
    }

    /**
     * @return a serialized copy of GameState or null if not implemented yet
     */
    public static SerializableGameState getSerializedInstance() {
        if(GameState.getInstance(false) != null)
            return new SerializableGameState();
        return null;
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
    private synchronized void reset() {
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
     * Wrap each player's mechanics with origin's evil mechanics
     * @param origin player from which the event started
     */
    public void wrapPlayers(Player origin) {
        getPlayers().forEach(p -> p.wrapMechanics(origin));
    }

    /**
     * Unwrap each player's mechanics from origin's evil mechanics
     * @param origin player from which the event started
     */
    public void unwrapPlayers(Player origin) {
        getPlayers().forEach(p -> p.unwrapMechanics(origin));
    }

    /**
     * Starts the game
     */
    public void startGame() {
//        System.out.println(MessageFormat.format(Message.GAME_STARTED, getNumPlayer()));
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
        Map<String, String> map = new LinkedHashMap<>();
        for (Player pl: players) {
            map.put(pl.getUsername(), pl.getMechanics().getType().getName());
        }
        notifyObservers(new ChooseStartingPlayerRequest(this.getCurrPlayer().getUsername(), playerList, map));
    }

    /**
     * Sends first worker placement request
     * @param numPl number of the player to send the request to
     * @param numWorker number of the worker to be placed on the board
     */
    public void firstWorkerPlacement(int numPl, int numWorker) {
        if (numWorker == 0 && numPl != 0)
            skipPlayer();
        notifyObservers(new AssignFirstWorkerPlacementRequest(getCurrPlayer().getUsername(), numPl, numWorker));
    }

    /**
     * Sets current player's active worker and wakes runTurn()
     * @param worker worker selected by the player
     */
    public void receiveWorker(Worker worker) {
        getCurrPlayer().setCurrWorker(worker);
        getCurrPlayer().getState().runState();
    }

    /**
     * Asks currPlayer to select a worker, then sets it as current
     */
    private void selectWorker() {
        List<int[]> workers = new ArrayList<>();
        for (Worker worker : getCurrPlayer().getWorkers()) {
//            if (!getCurrPlayer().getStuckWorkers().contains(worker)) {
                Position position = worker.getCurrPosition();
                int[] coordinates = {position.getRow(), position.getCol()};
                workers.add(coordinates);
//            }
        }
        notifyObservers(new ChooseWorkerRequest(getCurrPlayer().getUsername(), workers));
    }

    /**
     * Setup each player's mechanics and start the game
     */
    public void setupGameMechanics() {
        for(Player player : players)
            player.getMechanics().setupMechanics(player);
        newTurn();
    }

    /**
     * If no player is playing, moves currPlayer pointer forward, prepares and notifies the new player for the turn
     */
    private void newTurn() {
        newTurn(true);
    }

    /**
     * If no player is playing, moves currPlayer pointer forward and prepares the new player for the turn
     * @param notify if true notifies the new player
     */
    private void newTurn(boolean notify) {
        for(Player player : getPlayers())
            if(player.getState().getType() != StateType.WAIT)
                return;
        getCurrPlayer().endTurn();
        skipPlayer();
        getCurrPlayer().newTurn();
        if(notify)
            notifyObservers(new StartTurnRequest(getCurrPlayer().getUsername()));
    }

    /**
     * Runs a piece of the turn, unless the player is in wait
     */
    public void runTurn() {
        if (getCurrPlayer().getState().getType() == StateType.WAIT)
            newTurn();
        else if(!getCurrPlayer().isWorkerLocked())
            selectWorker();
        else
            getCurrPlayer().getState().runState();
    }

    @Override
    public void notifyObservers(Request request) {
        synchronized (observers) {
            for (Observer<Request> obs: observers) {
                obs.update(request);
            }
        }
    }

    @Override
    public void addObserver(Observer<Request> o) {
        synchronized (observers) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer<Request> o) {
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
    public synchronized void playerDefeat(Player player, String message) {
        //prepares for a possible new turn
        newTurn(false);
        if(getPlayers().size() == 2) {
            //the game cannot continue
            for (Player p: players) {
                if (!p.getUsername().equals(player.getUsername())) {
                    playerVictory(p, message);
                    break;
                }
            }
            return;
        }
        //remove player's workers from the board
        ArrayList<Worker> workers = player.getWorkers();
        for(Position[] line : board) {
            for(Position position : line) {
                if(workers.contains(position.getWorker()))
                    position.setWorker(null);
            }
        }
        //close player's connection and inform other players
        notifyObservers(new RemovePlayerRequest(player.getUsername(), message, false));
        //remove player from the list
        removePlayer(player);
        //the game can continue
        setNumPlayer(getPlayers().size());
        //unwrap enemies (useless if not ATHENA)
        unwrapPlayers(player);
        notifyObservers(new StartTurnRequest(getCurrPlayer().getUsername()));
    }

    /**
     * Informs all the players that the game is over, electing the winner
     * @param player winner of the game
     * @param message reason for (enemy) defeat
     */
    public void playerVictory(Player player, String message) {
        //close every connection notifying the players
        notifyObservers(new RemovePlayerRequest(player.getUsername(), message, true));
        //cleans the GameState singleton for a new game
        reset();
    }

    /**
     * Drops all client connection after a client left without surrendering
     */
    public void dropAllConnections() {
        notifyObservers(new RemovePlayerRequest("@","", false));
        GameState.getInstance().reset();
    }
}
