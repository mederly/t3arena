package com.mederly.t3arena.players.stat;

import com.mederly.t3arena.Board;

/**
 *  Data source that can be updated.
 */
public interface AdaptiveDataSource extends DataSource {

    /**
     * Updates statistics for a given board.
     * @param board Board to be updated
     * @param deltaWinX Delta for wins of X
     * @param deltaWinO Delta for wins of O
     * @param deltaTie Delta for ties
     */
    void update(Board board, int deltaWinX, int deltaWinO, int deltaTie);

    /**
     * @return true if the store is locked for updates
     */
    boolean isLocked();
}
