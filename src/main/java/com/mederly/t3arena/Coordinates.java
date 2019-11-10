package com.mederly.t3arena;

/**
 *  A pair of coordinates.
 */
public class Coordinates {

    /**
     * Row: 0..2, starting from the uppermost.
     */
    private int row;

    /**
     * Column: 0..2, starting from the leftmost.
     */
    private int column;

    public Coordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Creates the coordinates based on absolute field number (1..9), see description in the Board class.
     */
    public Coordinates(int fieldNumber) {
        if (fieldNumber < 1 || fieldNumber > 9) {
            throw new IllegalArgumentException("Illegal field number: " + fieldNumber);
        }
        row = (fieldNumber-1) / 3;
        column = (fieldNumber-1) % 3;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getFieldNumber() {
        return row*3 + column + 1;
    }
}
