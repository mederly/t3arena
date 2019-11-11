package com.mederly.t3arena.players;

import java.util.List;

/**
 *  Selects random move among equal candidates.
 */
public class RandomMoveSelector implements EqualMoveSelector {

    @Override
    public int selectMove(List<Integer> candidates) {
        return candidates.get((int) (Math.random() * candidates.size()));
    }
}
