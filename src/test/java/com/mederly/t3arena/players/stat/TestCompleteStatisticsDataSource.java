package com.mederly.t3arena.players.stat;

import com.mederly.t3arena.Board;
import com.mederly.t3arena.GameState;
import com.mederly.t3arena.players.minimax.MinimaxStateComputer;
import com.mederly.t3arena.players.minimax.StateNode;

import java.util.*;

import static com.mederly.t3arena.Board.PLAYER_O;
import static com.mederly.t3arena.Board.PLAYER_X;

/**
 *
 */
public class TestCompleteStatisticsDataSource {

    public static void main(String[] args) {
        CompleteStatisticsDataSource statisticsData = new CompleteStatisticsDataSource();

        long start = System.currentTimeMillis();
        StateNode stateTreeRoot = new StateNode(new GameState());
        stateTreeRoot.evaluate();
        System.out.println("State tree computed in " + (System.currentTimeMillis() - start) + " ms");

        Map<Board, StateNode> uniqueMinimaxStates = MinimaxStateComputer.getUniqueStates(stateTreeRoot);
        dumpStates(uniqueMinimaxStates.values(), statisticsData);

        checkAllStates(uniqueMinimaxStates.values(), statisticsData, new NotLoseRatioSelector());
    }

    private static void dumpStates(Collection<StateNode> allStates, CompleteStatisticsDataSource statisticsData) {
        System.out.println();
        System.out.println(String.format("%7s\t%9s\t%9s\t%4s\t%4s\t%4s\t%10s\t%10s\t%10s\t%s", "#", "Board", "Num", "Turn", "ValX", "ValO", "StatWinX", "StatWinO", "StatTie", "Comment"));
        Iterator<StateNode> iterator = allStates.iterator();
        for (int i = 0; i < allStates.size(); i++) {
            StateNode state = iterator.next();
            Board board = state.getGameState().getBoard();
            Statistics statistics = statisticsData.getStatistics(board);
            if (statistics == null) {
                System.out.println("Warning: no statistics for " + board);
                statistics = new Statistics();
            }

            // minimax values
            int valX = state.getValueForX();
            int valO = state.getValueForO();

            // statistics values
            double winX = statistics.getWinRatio(PLAYER_X);
            double winO = statistics.getWinRatio(PLAYER_O);
            double tie = 1.0 - winX - winO;

            String comment = "";

            if (valX > 0 && winO > 0.5) {
                comment += "Winning for X but statistics show winO = " + winO;
            } else if (valO > 0 && winX > 0.5) {
                comment += "Winning for O but statistics show winX = " + winX;
            }

            System.out.println(String.format("%7s\t%9s\t%9d\t%4d\t%4d\t%4d\t%10.3f\t%10.3f\t%10.3f\t%s", i+1, board.getStringRepresentation(),
                        board.getNumericRepresentation(), state.getGameState().getTurn(), valX,
                    valO, winX, winO, tie, comment));
        }
    }

    private static void checkAllStates(Collection<StateNode> allStates, CompleteStatisticsDataSource statisticsData,
            StatisticsInterpreter interpreter) {
        System.out.println("\nChecking all states for statistics vs. minimax consistency using " + interpreter.getClass().getSimpleName() + "\n");

        for (StateNode state : allStates) {

            byte side = state.getGameState().getTurn();
            StateNode[] children = state.getChildren();

            double maxValueStat = Double.MIN_VALUE;
            List<Integer> bestMovesStat = new ArrayList<>();

            for (int i = 0; i < children.length; i++) {
                StateNode child = children[i];
                if (child != null) {
                    double value = interpreter.getValue(side, statisticsData.getStatistics(child.getGameState().getBoard()));
                    // We should do some fractional arithmetic here. Comparing doubles for equality is a bit unreliable.
                    if (bestMovesStat.isEmpty() || value > maxValueStat) {
                        bestMovesStat.clear();
                        bestMovesStat.add(i+1);
                        maxValueStat = value;
                    } else if (value == maxValueStat) {
                        bestMovesStat.add(i+1);
                    }
                }
            }

            // problem is if there's a losing move among ones selected by the statistics

            int currentValue = side == PLAYER_X ? state.getValueForX() : state.getValueForO();
            if (currentValue >= 0) {
                for (int bestMoveStat : bestMovesStat) {
                    StateNode child = children[bestMoveStat - 1];
                    int newValue = side == PLAYER_X ? child.getValueForX() : child.getValueForO();
                    if (newValue < 0) {
                        System.out.println(String.format(
                                "BEWARE: in %7s position %s a recommended move of %d leads to losing position for %d: %s (all recommended moves: %s, stat evaluation = %.3f)",
                                (currentValue > 0 ? "winning" : "tie"),
                                state.getGameState().getBoard(), bestMoveStat, side, child.getGameState().getBoard(),
                                bestMovesStat, maxValueStat));
                    }
                }
            }
        }
    }
}
