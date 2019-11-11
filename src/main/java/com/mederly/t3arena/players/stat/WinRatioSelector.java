package com.mederly.t3arena.players.stat;

/**
 *  Selector that looks for "win" situations.
 */
public class WinRatioSelector implements StatisticsInterpreter {

    @Override
    public double getValue(byte side, Statistics statistics) {
        return statistics.getWinRatio(side);
    }
}
