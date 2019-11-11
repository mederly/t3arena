package com.mederly.t3arena.players;

import java.util.List;

/**
 *  Always selects first move among equal candidates.
 */
public class FirstMoveSelector implements EqualMoveSelector {

    @Override
    public int selectMove(List<Integer> candidates) {
        return candidates.get(0);
    }
}
