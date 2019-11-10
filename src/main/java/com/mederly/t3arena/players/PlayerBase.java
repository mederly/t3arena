package com.mederly.t3arena.players;

import com.mederly.t3arena.Board;
import com.mederly.t3arena.Player;

import java.util.List;

/**
 * Skeleton of a player: provides a board to see the game state and registers opponent's moves on it.
 * Then offers an implementor of a player available fields to select from.
 */
public abstract class PlayerBase implements Player {

    /**
     * Internal representation of the board.
     */
    private Board board;

    public void beforeMatch() {
        // nothing to do here
    }

    public void beforeGame() {
        board = new Board();
    }

    public void onOpponentMove(int field) {
        board.registerMove(field);
    }

    public int move() {
        List<Integer> freeFields = board.getFreeFields();
        if (freeFields.isEmpty()) {
            throw new IllegalStateException("Why are you calling me? There's no field to take.");
        } else {
            int myMove = selectMyMove(freeFields);
            board.registerMove(myMove);
            return myMove;
        }
    }

    /**
     * Called with a (non-empty) list of fields from which the player has to select one.
     */
    protected abstract int selectMyMove(List<Integer> freeFields);
}
