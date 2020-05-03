package it.polimi.ingsw.PSP4.utils;

import it.polimi.ingsw.PSP4.model.Position;
import it.polimi.ingsw.PSP4.model.serializable.SerializablePosition;

/**
 * Class used to store the coordinates of a position
 */
public class Coordinates {
    private final int row;
    private final int col;

    public int getCol() { return col; }
    public int getRow() { return row; }

    public Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Coordinates(Coordinates coord) {
        this.row = coord.getRow();
        this.col = coord.getCol();
    }

    public Coordinates(Position position) {
        this.row = position.getRow();
        this.col = position.getCol();
    }

    public Coordinates(SerializablePosition position) {
        this.row = position.getRow();
        this.col = position.getCol();
    }

    public boolean equals(Coordinates coord) {
        return coord.getRow() == row && coord.getCol() == col;
    }

    public boolean equals(Position position) {
        return position.getRow() == row && position.getCol() == col;
    }

    public boolean equals(SerializablePosition position) {
        return position.getRow() == row && position.getCol() == col;
    }
}
