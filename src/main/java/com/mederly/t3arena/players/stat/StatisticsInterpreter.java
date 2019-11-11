package com.mederly.t3arena.players.stat;

/**
 * Extracts relevant number from the statistics.
 */
public interface StatisticsInterpreter {

    /**
     * @return Value relevant for the given side.
     */
    double getValue(byte side, Statistics statistics);
}
