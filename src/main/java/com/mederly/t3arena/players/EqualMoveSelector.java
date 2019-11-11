package com.mederly.t3arena.players;

import java.util.List;

/**
 *  Decides how to select from equal-valued candidate moves. Useful for various players (e.g. minimax, statistics-based).
 */
public interface EqualMoveSelector {

    int selectMove(List<Integer> candidates);
}
