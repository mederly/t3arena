package com.mederly.t3arena;

import com.mederly.t3arena.players.RandomPlayer;
import com.mederly.t3arena.players.SequentialPlayer;
import com.mederly.t3arena.players.FirstMoveSelector;
import com.mederly.t3arena.players.minimax.MinimaxPlayer;
import com.mederly.t3arena.players.RandomMoveSelector;
import com.mederly.t3arena.players.stat.CompleteStatisticsDataSource;
import com.mederly.t3arena.players.stat.NotLoseRatioSelector;
import com.mederly.t3arena.players.stat.StatisticalPlayer;
import com.mederly.t3arena.players.stat.WinRatioSelector;

import java.util.ArrayList;
import java.util.List;

/**
 *  A class that provides a place where a couple of playing algorithms (i.e. players) meet to match.
 */
public class Arena {

    private List<Match> matches;

    private static final int ROUNDS_IN_MATCH = 1000;

    private void play() {

        // Players initialization
        Player sequential1 = new SequentialPlayer("Sequential1");
        Player sequential2 = new SequentialPlayer("Sequential2");
        Player random1 = new RandomPlayer("Random1");
        Player random2 = new RandomPlayer("Random2");
        Player minimaxFirst1 = new MinimaxPlayer("MinimaxFirst1", new FirstMoveSelector());
        Player minimaxFirst2 = new MinimaxPlayer("MinimaxFirst2", new FirstMoveSelector());
        Player minimaxRandom1 = new MinimaxPlayer("MinimaxRandom1", new RandomMoveSelector());

        CompleteStatisticsDataSource completeStatisticsDataSource = new CompleteStatisticsDataSource();
        Player completeStatisticsWithWinRatio = new StatisticalPlayer("CompleteStatistics-Win",
                completeStatisticsDataSource, new WinRatioSelector(), new RandomMoveSelector());
        Player completeStatisticsWithNotLoseRatio = new StatisticalPlayer("CompleteStatistics-NotLose",
                completeStatisticsDataSource, new NotLoseRatioSelector(), new RandomMoveSelector());

        // Matches
        matches = new ArrayList<>();
        runMatch(sequential1, random1);
        runMatch(sequential1, sequential2);
        runMatch(random1, random2);
        runMatch(minimaxFirst1, random1);
        runMatch(minimaxFirst1, sequential1);
        runMatch(minimaxFirst1, minimaxFirst2);
        runMatch(minimaxFirst1, minimaxRandom1);
        runMatch(completeStatisticsWithWinRatio, sequential1);
        runMatch(completeStatisticsWithWinRatio, random1);
        runMatch(completeStatisticsWithWinRatio, minimaxFirst1);
        runMatch(completeStatisticsWithWinRatio, minimaxRandom1);
        runMatch(completeStatisticsWithNotLoseRatio, sequential1);
        runMatch(completeStatisticsWithNotLoseRatio, random1);
        runMatch(completeStatisticsWithNotLoseRatio, minimaxFirst1);
        runMatch(completeStatisticsWithNotLoseRatio, minimaxRandom1);
        runMatch(completeStatisticsWithWinRatio, completeStatisticsWithNotLoseRatio);

        // Final results
        System.out.println("----------------------------------------------------------------------");
        System.out.println("Results of the matches:\n");
        for (Match match : matches) {
            System.out.println(match.getResultAsString());
        }
    }

    private void runMatch(Player player1, Player player2) {
        Match match = new Match(player1, player2, ROUNDS_IN_MATCH);
        match.run();
        matches.add(match);
    }

    public static void main(String[] args) {
        Arena arena = new Arena();
        arena.play();
    }
}
