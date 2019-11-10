package com.mederly.t3arena;

import static com.mederly.t3arena.Board.PLAYER_O;
import static com.mederly.t3arena.Board.PLAYER_X;

/**
 *  Representation of the state of a game: (1) a board, (2) whose turn is it and (3) optionally a winner.
 */
public class GameState {

    /**
     * The playing board.
     */
    private final Board board;

    /**
     * Whose player's turn we are currently at. Either PLAYER_X (1) or PLAYER_O (2).
     */
    private byte turn;

    /**
     * Who won? 1 = PLAYER_X, 2 = PLAYER_O, 0 = tie, null = game is in progress.
     */
    private Integer winner;

    /**
     * Default constructor: creates a new game.
     */
    public GameState() {
        this.board = new Board();
        this.turn = PLAYER_X;
    }

    /**
     * Derives a new game state from existing one.
     */
    public GameState(GameState previous, int move) {
        this.board = new Board(previous.board);
        this.turn = previous.turn;
        registerMove(move);
    }

    public Board getBoard() {
        return board;
    }

    public byte getTurn() {
        return turn;
    }

    public Integer getWinner() {
        return winner;
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
            throw new IllegalStateException("X nor O not on the turn! turn = " + turn);
        }
    }

    public void registerMove(int move) {
        board.registerMove(turn, move);
        changeTurn();
    }

    private void changeTurn() {
        if (isTurnX()) {
            turn = PLAYER_O;
        } else if (isTurnO()) {
            turn = PLAYER_X;
        } else {
            throw new IllegalStateException("X nor O not on the turn! turn = " + turn);
        }
    }

    public Integer determineWinner() {
        winner = board.getWinner();
        return winner;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "board=" + board +
                ", turn=" + turn +
                ", winner=" + winner +
                '}';
    }
}
