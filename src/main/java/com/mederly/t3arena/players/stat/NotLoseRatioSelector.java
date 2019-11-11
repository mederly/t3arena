package com.mederly.t3arena.players.stat;

/**
 *  Selector that looks on percentage of "not losing" situations.
 */
public class NotLoseRatioSelector implements StatisticsInterpreter {

    @Override
    public double getValue(byte side, Statistics statistics) {
        return 1.0-statistics.getWinRatio((byte) (3-side));
    }
}
