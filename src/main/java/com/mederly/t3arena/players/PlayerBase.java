package com.mederly.t3arena.players;

import com.mederly.t3arena.GameState;
import com.mederly.t3arena.Player;

import java.util.List;

/**
 * Skeleton of a player: provides a board to see the game state and registers opponent's moves on it.
 * Then offers an implementor of a player available fields to select from.
 */
public abstract class PlayerBase implements Player {

    /**
     * Player's name.
     */
    private final String name;

    /**
     * Internal representation of the game state.
     */
    private GameState gameState;

    protected PlayerBase(String name) {
        this.name = name;
    }

    public void beforeMatch() {
        // nothing to do here
    }

    public void beforeGame(byte side) {
        gameState = new GameState();
    }

    public void onOpponentMove(int field) {
        gameState.registerMove(field);
    }

    public int move() {
        List<Integer> freeFields = gameState.getBoard().getFreeFields();
        if (freeFields.isEmpty()) {
            throw new IllegalStateException("Why are you calling me? There's no field to take.");
        } else {
            int myMove = selectMyMove(freeFields);
            gameState.registerMove(myMove);
            return myMove;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Called with a (non-empty) list of fields from which the player has to select one.
     */
    protected abstract int selectMyMove(List<Integer> freeFields);
}
