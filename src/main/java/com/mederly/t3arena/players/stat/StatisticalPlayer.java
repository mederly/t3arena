package com.mederly.t3arena.players.stat;

import com.mederly.t3arena.Board;
import com.mederly.t3arena.GameState;
import com.mederly.t3arena.players.EqualMoveSelector;
import com.mederly.t3arena.players.PlayerBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mederly.t3arena.Board.PLAYER_O;
import static com.mederly.t3arena.Board.PLAYER_X;

/**
 *  Player that bases its moves on statistical information about various positions.
 */
public class StatisticalPlayer extends PlayerBase {

    /**
     * Source of statistical data.
     */
    private DataSource dataSource;

    /**
     * Interpreter of the data. E.g. should we decide based on winning percentage? Winning+tie percentage? Something other?
     */
    private StatisticsInterpreter interpreter;

    /**
     * How to select among equally good moves.
     */
    private EqualMoveSelector equalMoveSelector;

    public StatisticalPlayer(String name, DataSource dataSource, StatisticsInterpreter interpreter, EqualMoveSelector equalMoveSelector) {
        super(name);
        this.dataSource = dataSource;
        this.interpreter = interpreter;
        this.equalMoveSelector = equalMoveSelector;
    }

    @Override
    protected int selectMyMove(List<Integer> freeFields) {
        Board board = gameState.getBoard();

        double maxValue = Double.MIN_VALUE;
        List<Integer> bestMoves = new ArrayList<>();

        for (int i = 0; i < freeFields.size(); i++) {
            Integer field = freeFields.get(i);
            Board boardAfter = new Board(board);
            boardAfter.registerMove(side, field);
            double value = interpreter.getValue(side, dataSource.getStatistics(boardAfter));

            // We should do some fractional arithmetic here. Comparing doubles for equality is a bit unreliable.
            if (bestMoves.isEmpty() || value > maxValue) {
                bestMoves.clear();
                bestMoves.add(i);
                maxValue = value;
            } else if (value == maxValue) {
                bestMoves.add(i);
            }
        }
        if (bestMoves.isEmpty()) {
            throw new IllegalStateException("No fields? Really? Board = " + board);
        }
        return freeFields.get(equalMoveSelector.selectMove(bestMoves));
    }

    @Override
    public void afterGame(GameState gameState, List<Integer> moves) {
        if (dataSource instanceof AdaptiveDataSource) {
            AdaptiveDataSource adaptiveDataSource = (AdaptiveDataSource) dataSource;
            if (!adaptiveDataSource.isLocked()) {
                updateData(adaptiveDataSource, gameState, moves);
            }
        }
    }

    private void updateData(AdaptiveDataSource source, GameState gameState, List<Integer> moves) {
        Integer winner = gameState.getWinner();
        if (winner != null) {
            Board board = new Board();
            byte player = PLAYER_X;
            Iterator<Integer> moveIterator = moves.iterator();
            int deltaWinX = winner == PLAYER_X ? 1 : 0;
            int deltaWinO = winner == PLAYER_O ? 1 : 0;
            int deltaTie = winner == 0 ? 1 : 0;
            for (;;) {
                source.update(board, deltaWinX, deltaWinO, deltaTie);
                if (moveIterator.hasNext()) {
                    Integer nextMove = moveIterator.next();
                    board = new Board(board);
                    board.registerMove(player, nextMove);
                    player = GameState.getOtherPlayer(player);
                } else {
                    // nothing more to do
                    break;
                }
            }
        } else {
            throw new IllegalStateException("How to update the data when there's no winner resolution? state = " + gameState);
        }
    }
}
