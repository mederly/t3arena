package com.mederly.t3arena;

import java.util.ArrayList;
import java.util.List;

/**
 * A Tic-Tac-Toe board.
 */
public class Board {

    public static final byte PLAYER_X = 1;
    public static final byte PLAYER_O = 2;

    /**
     * Board: [0][0] (#1)    [0][1] (#2)     [0][2] (#3)
     *        [1][0] (#4)    [1][1] (#5)     [1][2] (#6)
     *        [2][0] (#7)    [2][1] (#8)     [2][2] (#9)
     *
     * Each field has a value of either 0 (empty), 1 (X), or 2 (O).
     */
    private byte[][] board;

    /**
     * Whose player's turn we are currently at. Either PLAYER_X (1) or PLAYER_O (2).
     */
    private byte turn;

    /**
     * Has the game finished?
     */
    private boolean finished;

    /**
     * Who won? 1 = PLAYER_X, 2 = PLAYER_O, 0 = tie.
     */
    private int winner;

    public Board() {
        initialize();
    }

    private void initialize() {
        board = new byte[][] { new byte[3], new byte[3], new byte[3] };
        turn = PLAYER_X;
    }

    public void registerMove(int field) {
        checkState();

        Coordinates coords = new Coordinates(field);
        int row = coords.getRow();
        int column = coords.getColumn();

        //System.out.println(String.format("field: %d --> row: %d, column: %d", field, row, column));

        if (board[row][column] != 0) {
            throw new IllegalStateException("Board at field " + field + " ([" + row + "][" + column + "]) has already a value of " + board[row][column]);
        } else {
            board[row][column] = turn;
        }
        changeTurn();
    }

    private void changeTurn() {
        if (turn == PLAYER_X) {
            turn = PLAYER_O;
        } else if (turn == PLAYER_O) {
            turn = PLAYER_X;
        } else {
            throw new IllegalStateException("Invalid turn: " + turn);
        }
    }

    private void checkState() {
        if (board == null) {
            throw new IllegalStateException("Board is not initialized");
        }
        if (turn != PLAYER_X && turn != PLAYER_O) {
            throw new IllegalStateException("Illegal turn value: " + turn);
        }
    }

    public byte getAt(int fieldNumber) {
        Coordinates c = new Coordinates(fieldNumber);
        return board[c.getRow()][c.getColumn()];
    }

    public List<Integer> getFreeFields() {
        List<Integer> freeFields = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            if (getAt(i) == 0) {
                freeFields.add(i);
            }
        }
        return freeFields;
    }

    public int getTurn() {
        return turn;
    }

    public boolean isTurnX() {
        return turn == PLAYER_X;
    }

    public boolean isTurnO() {
        return turn == PLAYER_O;
    }

    public String getTurnDescription() {
        if (isTurnX()) {
            return "X";
        } else if (isTurnO()) {
            return "O";
        } else {
            throw new IllegalStateException("Illegal turn value: " + turn);
        }
    }

    public void determineWinner() {
        if (isWinner(PLAYER_X)) {
            winner = PLAYER_X;
            finished = true;
        } else if (isWinner(PLAYER_O)) {
            winner = PLAYER_O;
            finished = true;
        } else if (getFreeFields().isEmpty()) {
            winner = 0;
            finished = true;
        } else {
            finished = false;
        }
    }

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

    public int getWinner() {
        return winner;
    }

    public boolean isFinished() {
        return finished;
    }
}
