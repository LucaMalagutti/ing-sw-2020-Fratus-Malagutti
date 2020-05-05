package it.polimi.ingsw.PSP4.model.serializable;

import it.polimi.ingsw.PSP4.model.Position;

import java.io.Serializable;

/**
 * Serializable "light" copy of Position
 */
public final class SerializablePosition implements Serializable {
    private static final long serialVersionUID = 6324941972978326656L;

    private final int height;               //level of the building in the position
    private final int row;                  //y coordinate of the position in the platform
    private final int col;                  //x coordinate of the position in the platform
    private final String worker;            //universally unique identifier of the worker in the position, null if free
    private final boolean dome;             //if true the position has a dome

    /**
     * Constructor of the class SerializablePosition
     * @param position Position to serialize
     */
    public SerializablePosition(Position position) {
        this.height = position.getHeight();
        this.row = position.getRow();
        this.col = position.getCol();
        if(position.getWorker() == null)
            this.worker = null;
        else
            this.worker = position.getWorker().getId();
        this.dome = position.hasDome();
    }

    //getter and setter
    public int getHeight() { return height; }

    public int getRow() { return row; }

    public int getCol() { return col; }

    public String getWorker() { return worker; }

    public boolean hasDome() { return dome; }

    //returns true if they have same coordinates
    public boolean equals (SerializablePosition position){
        return position.getRow() == row && position.getCol() == col;
    }
}
