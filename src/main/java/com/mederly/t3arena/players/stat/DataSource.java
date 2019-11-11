package com.mederly.t3arena.players.stat;

import com.mederly.t3arena.Board;

/**
 *  Data source for the statistics-based player.
 */
public interface DataSource {

    /**
     * Returns statistical information for given board position.
     */
    Statistics getStatistics(Board board);
}
