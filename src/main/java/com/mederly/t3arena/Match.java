package com.mederly.t3arena;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  A match between two players: a series of rounds, each having two games.
 */
public class Match {

    private Player player1;
    private Player player2;

    /**
     * How many rounds there should be.
     */
    private int rounds;

    /**
     * Score of the first player. Each win is 2 points, tie 1 point, loss 0 points.
     */
    private AtomicInteger score1;

    /**
     * Score of the second player.
     */
    private AtomicInteger score2;

    public Match(Player player1, Player player2, int rounds) {
        this.player1 = player1;
        this.player2 = player2;
        this.score1 = new AtomicInteger(0);
        this.score2 = new AtomicInteger(0);
        this.rounds = rounds;
    }

    public void run() {
        System.out.println("Starting match between " + player1 + " and " + player2 + " having " + rounds + " rounds");
        player1.beforeMatch();
        player2.beforeMatch();
        for (int round = 1; round <= rounds; round++) {
            runGame(round, 1, player1, player2, score1, score2);
            runGame(round, 2, player2, player1, score2, score1);
        }
        System.out.println(getResultAsString());
    }

    public String getResultAsString() {
        return "Result of the match: " + player1 + ": " + score1 + ", " + player2 + ": " + score2;
    }

    private void runGame(int round, int gameNumber, Player playerX, Player playerO, AtomicInteger scoreX, AtomicInteger scoreO) {
        System.out.println("Running game with X=" + playerX + ", O=" + playerO + " (round=" + round + ", game=" + gameNumber + ")");
        Game game = new Game(playerX, playerO);
        int winner = game.run();

        Player winnerPlayer;
        switch (winner) {
            case 1:
                winnerPlayer = playerX;
                scoreX.addAndGet(2);
                break;
            case 2:
                winnerPlayer = playerO;
                scoreO.addAndGet(2);
                break;
            default:
                winnerPlayer = null;
                scoreX.incrementAndGet();
                scoreO.incrementAndGet();
        }
        System.out.println("Winner: " + winnerPlayer + "; score is " + score1 + ":" + score2);
    }
}