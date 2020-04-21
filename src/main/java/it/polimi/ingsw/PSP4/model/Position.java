package it.polimi.ingsw.PSP4.model;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents the state of cell inside the game board
 */
public class Position {
    private int height;                             //level of the building in the position
    private final int row;                          //y coordinate of the position in the platform
    private final int col;                          //x coordinate of the position in the platform
    private Worker worker;                          //worker currently in the position, null if free
    private boolean dome;                           //if true the position has a dome
    private ArrayList<Position> neighbour;          //list of surrounding positions on the board

    //getter and setter
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) { this.height = height; }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Worker getWorker() {
        return worker;
    }
    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public boolean hasDome() { return dome; }
    public void setDome(boolean hasDome) { this.dome = hasDome; }

    /**
     * @return a copy of neighbour.
     */
    public ArrayList<Position> getNeighbour() {
        return new ArrayList<>(neighbour);
    }

    /**
     * Increases the height by 1 (up to 4, only if dome is false)
     * @return true if has reached the maximum level and built a dome (level 4)
     */
    public boolean increaseHeight() {
        if(hasDome() || getHeight() == 4){
            //exception
        }
        setHeight(getHeight() + 1);
        if(getHeight() == 4){
            setDome(true);
            return true;
        }
        return false;
    }

    /**
     * Creates neighbor ArrayList for a Position in the gameState.board
     * Should only be called in the GameState constructor
     * @param row row of the Position
     * @param col column of the Position
     */
    protected void setUpNeighbors(int row, int col, GameState gameState) {
        if (neighbour==null){
            neighbour = new ArrayList<>();
            Position[][] board = gameState.getBoard();
            for(int r=Math.max(0,row-1); r<=Math.min(row+1,board.length-1); r++){
                for(int c=Math.max(0,col-1); c<=Math.min(col+1,board.length-1); c++){
                    if(!(c==col && r==row)){
                        neighbour.add(board[r][c]);
                    }
                }
            }

        }
    }

    /**
     * Constructor of the class Position
     * Generates neighbour list based on row and col
     * @param row y coordinate of the position in the platform
     * @param col x coordinate of the position in the platform
     */
    public Position (int row, int col){
        this.height=0;
        this.row=row;
        this.col=col;
        this.worker=null;
        this.dome=false;
        this.neighbour = null;
    }

    /**
     * Filters neighbor arrayList by the cells of an height reachable by the player
     * @return An ArrayList of the neighboring Positions respecting the above property
     */
    public ArrayList<Position> getReachableHeight() {
        return getNeighbour().stream().filter(position -> position.getHeight()<=height+1).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Filters neighbor arrayList by the cells not occupied by a dome of by another worker
     * @return An ArrayList of the neighboring Positions respecting the above property
     */
    public ArrayList<Position> getFree() {
        return getNeighbour().stream().filter(position -> !position.hasDome() && position.getWorker()==null).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Filters neighbor arrayList by the cells occupied by an enemy worker
     * @param player reference to current player, used to know if a worker belongs to an enemy
     * @return An ArrayList of the neighboring Positions respecting the above property
     */
    public ArrayList<Position> getOccupied(Player player) {
        return getNeighbour().stream().filter(position -> position.getWorker() != null && !player.getWorkers().contains(position.getWorker())).collect(Collectors.toCollection(ArrayList::new));
    }
}
