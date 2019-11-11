package com.mederly.t3arena.players.stat;

import com.mederly.t3arena.players.diag.StatisticalDataDiagnostician;

/**
 *
 */
public class TestCompleteStatisticsDataSource {

    public static void main(String[] args) {
        CompleteStatisticsDataSource dataSource = new CompleteStatisticsDataSource();

        StatisticalDataDiagnostician diagnostician = new StatisticalDataDiagnostician();
        diagnostician.diagnose(dataSource, true);
    }
}
