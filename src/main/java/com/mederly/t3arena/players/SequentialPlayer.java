package com.mederly.t3arena.players;

import java.util.List;

/**
 *  A player that always selects the first available field.
 */
public class SequentialPlayer extends PlayerBase {

    @Override
    protected int selectMyMove(List<Integer> freeFields) {
        return freeFields.get(0);
    }

    @Override
    public String toString() {
        return "SequentialPlayer";
    }
}
