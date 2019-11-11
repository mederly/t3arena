package com.mederly.t3arena;

import java.util.ArrayList;
import java.util.List;

import static com.mederly.t3arena.Board.PLAYER_O;
import static com.mederly.t3arena.Board.PLAYER_X;

/**
 *  A single game between two specific Tic Tac Toe algorithms (players).
 */
public class Game {

    /**
     * The first player - i.e. the one that puts X signs on the board.
     */
    private final Player playerX;

    /**
     * The second player - i.e. the one that puts O signs on the board.
     */
    private final Player playerO;

    /**
     * The state of the game.
     */
    private GameState gameState;

    public Game(Player playerX, Player playerO) {
        this.playerX = playerX;
        this.playerO = playerO;
    }

    /**
     * Executes the game by round-robin invocations of players' methods.
     *
     * @return The winner. Or 0 if there's a tie.
     */
    public int run() {
        playerX.beforeGame(PLAYER_X);
        playerO.beforeGame(PLAYER_O);
        List<Integer> moves = new ArrayList<>();

        gameState = new GameState();
        for (;;) {
            Integer winner = gameState.determineWinner();
            if (winner != null) {
                System.out.println("Winner: " + winner);
                playerX.afterGame(gameState, moves);
                playerO.afterGame(gameState, moves);
                return winner;
            } else {
                int move = getCurrentPlayer().move();
                System.out.println("Player " + gameState.getTurnDescription() + " played: " + move);
                moves.add(move);
                gameState.registerMove(move);
                getCurrentPlayer().onOpponentMove(move);
            }
        }
    }

    private Player getCurrentPlayer() {
        if (gameState.isTurnX()) {
            return playerX;
        } else if (gameState.isTurnO()) {
            return playerO;
        } else {
            throw new IllegalStateException("X nor O not on the turn! turn = " + gameState.getTurn());
        }
    }

}
