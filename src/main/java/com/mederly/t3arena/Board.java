package com.mederly.t3arena;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Tic-Tac-Toe board.
 */
public class Board implements Comparable<Board> {

    public static final byte PLAYER_X = 1;
    public static final byte PLAYER_O = 2;

    /**
     * Board: [0][0] (#1)    [0][1] (#2)     [0][2] (#3)
     *        [1][0] (#4)    [1][1] (#5)     [1][2] (#6)
     *        [2][0] (#7)    [2][1] (#8)     [2][2] (#9)
     *
     * Each field has a value of either 0 (empty), 1 (X), or 2 (O).
     */
    private final byte[][] board;

    /**
     * Default constructor: creates a new board.
     */
    public Board() {
        this.board = new byte[][] { new byte[3], new byte[3], new byte[3] };
    }

    /**
     * Creates a new board by copying existing one.
     */
    public Board(Board original) {
        this();
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                board[i][j] = original.board[i][j];
            }
        }
    }

    /**
     * Registers a move done at this board by the specific player.
     */
    public void registerMove(byte player, int field) {
        Coordinates coords = new Coordinates(field);
        int row = coords.getRow();
        int column = coords.getColumn();

        if (board[row][column] != 0) {
            throw new IllegalStateException("Board at field " + field + " ([" + row + "][" + column + "]) has already a value of " + board[row][column]);
        } else {
            board[row][column] = player;
        }
    }

    /**
     * Returns a value of the given field.
     * @param fieldNumber Numeric value from 1 to 9.
     * @return 0 (empty), 1 (X) or 2 (O)
     */
    public byte getAt(int fieldNumber) {
        Coordinates c = new Coordinates(fieldNumber);
        return board[c.getRow()][c.getColumn()];
    }

    /**
     * List of free fields e.g. to choose from when doing our move.
     */
    public List<Integer> getFreeFields() {
        List<Integer> freeFields = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            if (getAt(i) == 0) {
                freeFields.add(i);
            }
        }
        return freeFields;
    }

    /**
     * Who is the winner, if any?
     *
     * @return 1 (PLAYER_X), 2 (PLAYER_O), 0 (tie) or null (game is still in progress)
     */
    public Integer getWinner() {
        if (isWinner(PLAYER_X)) {
            return (int) PLAYER_X;
        } else if (isWinner(PLAYER_O)) {
            return (int) PLAYER_O;
        } else if (getFreeFields().isEmpty()) {
            return 0;
        } else {
            return null;
        }
    }

    /**
     * Checks if "player" is the winner by looking at his signs in rows, columns and diagonals.
     */
    private boolean isWinner(byte player) {
        for (int row = 0; row <= 2; row++) {
            if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
                return true;
            }
        }
        for (int column = 0; column <= 2; column++) {
            if (board[0][column] == player && board[1][column] == player && board[2][column] == player) {
                return true;
            }
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        } else if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return String representation of the board, e.g. XO-XXO-OOX
     */
    public String getStringRepresentation() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 9; i++) {
            byte value = getAt(i);
            switch (value) {
                case PLAYER_X: sb.append("X"); break;
                case PLAYER_O: sb.append("O"); break;
                default: sb.append("-");
            }
        }
        return sb.toString();
    }

    /**
     * @return Numeric representation of the board, giving 0 at empty fields, 1 on Xs and 2 on Os.
     */
    public int getNumericRepresentation() {
        int returnValue = 0;
        for (int i = 1; i <= 9; i++) {
            returnValue = returnValue*10;
            byte value = getAt(i);
            switch (value) {
                case PLAYER_X: returnValue += 1; break;
                case PLAYER_O: returnValue += 2; break;
            }
        }
        return returnValue;
    }

    @Override
    public String toString() {
        return getStringRepresentation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Board)) {
            return false;
        } else {
            return Arrays.equals(board[0], ((Board) o).board[0])
                    && Arrays.equals(board[1], ((Board) o).board[1])
                    && Arrays.equals(board[1], ((Board) o).board[1]);
        }
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash*31 + Arrays.hashCode(board[0]);
        hash = hash*31 + Arrays.hashCode(board[1]);
        hash = hash*31 + Arrays.hashCode(board[2]);
        return hash;
    }

    @Override
    public int compareTo(Board o) {
        return getNumericRepresentation() - o.getNumericRepresentation();
    }
}
