package it.polimi.ingsw.PSP4.model;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents the state of cell inside the game board
 */
public class Position {
    //attributes
    private int height;
    private final int row;
    private final int col;
    private Worker worker;
    private boolean dome;
    private final ArrayList<Position> neighbour;
    private final GameState gameState;

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

    public void setDome() { this.dome = true; }

    public GameState getGameState() { return gameState; }

    /** @return a copy of neighbour.
     */
    public ArrayList<Position> getNeighbour() {
        return new ArrayList<>(neighbour);
    }

    //methods
    public Position (int row, int col, GameState gameState){
        this.height=0;
        this.row=row;
        this.col=col;
        this.worker=null;
        this.dome=false;
        this.gameState=gameState;
        this.neighbour = new ArrayList<>();
        Position[][] board = gameState.getBoard();
        for(int r=Math.max(0,row-1); r<=Math.min(row+1,board.length-1); r++){
            for(int c=Math.max(0,col-1); c<=Math.min(col+1,board.length-1); c++){
                if(!(c==col && r==row)){
                    neighbour.add(board[r][c]);
                }
            }
        }
    }

    /** Filters neighbor arrayList by the cells of an height reachable by the player
     * @return An ArrayList of the neighboring Positions respecting the above property
     */
    public ArrayList<Position> getReachableHeight(){
        return getNeighbour().stream().filter(position -> position.getHeight()<=height+1).collect(Collectors.toCollection(ArrayList::new));
    }

    /** Filters neighbor arrayList by the cells not occupied by a dome of by another worker
     * @return An ArrayList of the neighboring Positions respecting the above property
     */
    public ArrayList<Position> getFree (){
        return getNeighbour().stream().filter(position -> !position.hasDome() && position.getWorker()==null).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Filters neighbor arrayList by the cells occupied by an enemy worker
     * @return An ArrayList of the neighboring Positions respecting the above property
     */
    public ArrayList<Position> getOccupied (){
        return getNeighbour().stream().filter(position -> position.getWorker() != null && position.getWorker().getPlayer() != worker.getPlayer() ).collect(Collectors.toCollection(ArrayList::new));
    }
}
