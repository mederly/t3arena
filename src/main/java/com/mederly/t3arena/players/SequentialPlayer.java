package com.mederly.t3arena.players;

import java.util.List;

/**
 *  A player that always selects the first available field.
 */
public class SequentialPlayer extends PlayerBase {

    public SequentialPlayer(String name) {
        super(name);
    }

    @Override
    protected int selectMyMove(List<Integer> freeFields) {
        return freeFields.get(0);
    }
}
