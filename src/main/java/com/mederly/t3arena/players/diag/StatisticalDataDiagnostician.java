package com.mederly.t3arena.players.diag;

import com.mederly.t3arena.Board;
import com.mederly.t3arena.GameState;
import com.mederly.t3arena.players.minimax.MinimaxStateComputer;
import com.mederly.t3arena.players.minimax.StateNode;
import com.mederly.t3arena.players.stat.*;

import java.util.*;

import static com.mederly.t3arena.Board.PLAYER_O;
import static com.mederly.t3arena.Board.PLAYER_X;

/**
 * A class used to diagnose the correctness of a statistical data.
 */
public class StatisticalDataDiagnostician {

    public void diagnose(DataSource dataSource, boolean dumpAllStates) {

        System.out.println(" *** Diagnosing " + dataSource + " ***");

        long start = System.currentTimeMillis();
        StateNode stateTreeRoot = new StateNode(new GameState());
        stateTreeRoot.evaluate();
        System.out.println("State tree computed in " + (System.currentTimeMillis() - start) + " ms");

        Map<Board, StateNode> uniqueMinimaxStates = MinimaxStateComputer.getUniqueStates(stateTreeRoot);

        if (dumpAllStates) {
            dumpStates(uniqueMinimaxStates.values(), dataSource);
        }

        checkAllStates(uniqueMinimaxStates.values(), dataSource, new NotLoseRatioSelector());
    }

    private void dumpStates(Collection<StateNode> allStates, DataSource dataSource) {
        System.out.println();
        System.out.println(String.format("%7s\t%9s\t%9s\t%4s\t%4s\t%4s\t%10s\t%10s\t%10s\t%10s\t%s", "#", "Board", "Num", "Turn", "ValX", "ValO", "StatWinX", "StatWinO", "StatTie", "Samples", "Comment"));
        Iterator<StateNode> iterator = allStates.iterator();
        int noSamplesBoards = 0;
        for (int i = 0; i < allStates.size(); i++) {
            StateNode state = iterator.next();
            Board board = state.getGameState().getBoard();
            Statistics statistics = dataSource.getStatistics(board);
            if (statistics == null) {
                System.out.println("Warning: no statistics for " + board);
                statistics = new Statistics();
            }

            // minimax values
            int valX = state.getValueForX();
            int valO = state.getValueForO();

            // statistics values
            int samples = statistics.getSamples();
            double winX = statistics.getWinRatio(PLAYER_X);
            double winO = statistics.getWinRatio(PLAYER_O);
            double tie = 1.0 - winX - winO;

            String comment;

            if (samples == 0) {
                comment = "No samples";
                noSamplesBoards++;
            } else if (valX > 0 && winO > 0.5) {
                comment = "Winning for X but statistics show winO = " + winO;
            } else if (valO > 0 && winX > 0.5) {
                comment = "Winning for O but statistics show winX = " + winX;
            } else {
                comment = "";
            }

            System.out.println(String.format("%7s\t%9s\t%9d\t%4d\t%4d\t%4d\t%10.3f\t%10.3f\t%10.3f\t%10d\t%s", i+1, board.getStringRepresentation(),
                    board.getNumericRepresentation(), state.getGameState().getTurn(), valX,
                    valO, winX, winO, tie, samples, comment));
        }

        System.out.println("Boards: " + allStates.size() + "; no samples boards: " + noSamplesBoards);
    }

    private void checkAllStates(Collection<StateNode> allStates, DataSource statisticsData,
            StatisticsInterpreter interpreter) {
        System.out.println("\nChecking all states for statistics vs. minimax consistency using " + interpreter.getClass().getSimpleName() + "\n");

        int warnings = 0;
        int warningsWithSamples = 0;

        for (StateNode state : allStates) {

            byte side = state.getGameState().getTurn();
            StateNode[] children = state.getChildren();

            double maxValueStat = Double.MIN_VALUE;
            List<Integer> bestMovesStat = new ArrayList<>();
            List<Integer> samplesList = new ArrayList<>();

            for (int i = 0; i < children.length; i++) {
                StateNode child = children[i];
                if (child != null) {
                    Statistics statistics = statisticsData.getStatistics(child.getGameState().getBoard());
                    double value = interpreter.getValue(side, statistics);
                    // We should do some fractional arithmetic here. Comparing doubles for equality is a bit unreliable.
                    if (bestMovesStat.isEmpty() || value > maxValueStat) {
                        bestMovesStat.clear();
                        bestMovesStat.add(i+1);
                        samplesList.clear();
                        samplesList.add(statistics.getSamples());
                        maxValueStat = value;
                    } else if (value == maxValueStat) {
                        bestMovesStat.add(i+1);
                        samplesList.add(statistics.getSamples());
                    }
                }
            }

            // problem is if there's a losing move among ones selected by the statistics

            int currentValue = side == PLAYER_X ? state.getValueForX() : state.getValueForO();
            if (currentValue >= 0) {
                for (int i = 0; i < bestMovesStat.size(); i++) {
                    int bestMoveStat = bestMovesStat.get(i);
                    int samples = samplesList.get(i);
                    StateNode child = children[bestMoveStat - 1];
                    int newValue = side == PLAYER_X ? child.getValueForX() : child.getValueForO();
                    if (newValue < 0) {
                        System.out.println(String.format(
                                "BEWARE: in %7s position %s a recommended move of %d leads to losing position for %d: %s (all recommended moves: %s, stat evaluation = %.3f); samples = %d",
                                (currentValue > 0 ? "winning" : "tie"),
                                state.getGameState().getBoard(), bestMoveStat, side, child.getGameState().getBoard(),
                                bestMovesStat, maxValueStat, samples));
                        warnings++;
                        if (samples > 0) {
                            warningsWithSamples++;
                        }
                    }
                }
            }
        }
        System.out.println("\nWarnings: " + warnings);
        System.out.println("Warnings when there are some samples: " + warningsWithSamples);
    }
}
