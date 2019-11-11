package com.mederly.t3arena.players.stat;

import com.mederly.t3arena.Board;
import com.mederly.t3arena.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *  Data source providing complete statistics - gathered by playing all possible games with myself.
 */
public class CompleteStatisticsDataSource implements DataSource {

    private Map<Board, Statistics> statisticsMap = new TreeMap<>();

    public CompleteStatisticsDataSource() {
        long start = System.currentTimeMillis();
        generateStatistics();
        System.out.println("Statistics for " + statisticsMap.size() + " boards obtained in " + (System.currentTimeMillis() - start) + " ms");
    }

    private void generateStatistics() {
        List<GameState> states = new ArrayList<>();
        states.add(new GameState());
        computeAllGames(states);
    }

    /**
     * Computes all games starting at given sequence of states.
     * For all finished game updates the statisticsMap with information on states from this particular game.
     */
    private void computeAllGames(List<GameState> states) {
        assert !states.isEmpty();

        GameState currentState = states.get(states.size() - 1);
        Integer winner = currentState.determineWinner();
        if (winner != null) {
            updateStatistics(states, winner);
        } else {
            for (int i = 1; i <= 9; i++) {
                if (currentState.getBoard().getAt(i) == 0) {
                    // This field is free, let's try this move.
                    GameState newState = new GameState(currentState, i);
                    states.add(newState);
                    computeAllGames(states);
                    states.remove(states.size() - 1);       // remove the last state
                } else {
                    // This field is already occupied, so no try here.
                }
            }
        }
    }

    private void updateStatistics(List<GameState> states, int winner) {
        //System.out.println("Updating statistics (winner: " + winner + ")");
        for (GameState state : states) {
            //System.out.println(" - " + state.getBoard().getStringRepresentation());
            Board board = state.getBoard();
            Statistics statistics = statisticsMap.get(board);
            if (statistics == null) {
                statistics = new Statistics();
                statisticsMap.put(board, statistics);
            }
            statistics.increment(winner);
        }
    }

    @Override
    public Statistics getStatistics(Board board) {
        Statistics statistics = statisticsMap.get(board);
        if (statistics != null) {
            return statistics;
        } else {
            System.out.println("No statistics for " + board);
            return new Statistics();
        }
    }
}
