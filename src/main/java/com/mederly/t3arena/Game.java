package com.mederly.t3arena;

/**
 *  A single game between two Tic Tac Toe algorithms.
 */
public class Game {

    private Player playerX;
    private Player playerO;

    private Board board;

    public Game(Player playerX, Player playerO) {
        this.playerX = playerX;
        this.playerO = playerO;
    }

    /**
     * @return The winner. Or 0 if there's a tie.
     */
    public int run() {
        board = new Board();
        playerX.beforeGame();
        playerO.beforeGame();
        for (;;) {
            board.determineWinner();
            if (board.isFinished()) {
                System.out.println("Winner: " + board.getWinner());
                return board.getWinner();
            } else {
                Player currentPlayer = getCurrentPlayer();
                int move = currentPlayer.move();
                System.out.println("Player " + board.getTurnDescription() + " played: " + move);
                board.registerMove(move);
                getCurrentPlayer().onOpponentMove(move);
            }
        }
    }

    private Player getCurrentPlayer() {
        if (board.isTurnX()) {
            return playerX;
        } else if (board.isTurnO()) {
            return playerO;
        } else {
            throw new IllegalStateException("X nor O not on the turn! turn = " + board.getTurn());
        }
    }
}
