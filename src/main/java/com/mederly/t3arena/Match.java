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
     * Wins of the first player.
     */
    private AtomicInteger wins1;

    /**
     * Wins of the second player.
     */
    private AtomicInteger wins2;

    /**
     * Ties.
     */
    private AtomicInteger ties;

    public Match(Player player1, Player player2, int rounds) {
        this.player1 = player1;
        this.player2 = player2;
        this.wins1 = new AtomicInteger(0);
        this.wins2 = new AtomicInteger(0);
        this.ties = new AtomicInteger(0);
        this.rounds = rounds;
    }

    /**
     * Executes the match. The result is remembered in wins1, wins2, and ties variables.
     */
    public void run() {
        System.out.println("Starting match between " + player1 + " and " + player2 + " having " + rounds + " rounds");
        player1.beforeMatch();
        player2.beforeMatch();
        for (int round = 1; round <= rounds; round++) {
            runGame(round, 1, player1, player2, wins1, wins2);
            runGame(round, 2, player2, player1, wins2, wins1);
        }
        System.out.println(getResultAsString());
    }

    public String getResultAsString() {
        return "Result of the match: " + player1 + ": " + wins1 + " wins, " + player2 + ": " + wins2 + " wins, " + ties + " ties";
    }

    private void runGame(int round, int gameNumber, Player playerX, Player playerO, AtomicInteger winsX, AtomicInteger winsO) {
        System.out.println("Running game with X=" + playerX + ", O=" + playerO + " (round=" + round + ", game=" + gameNumber + ")");
        Game game = new Game(playerX, playerO);
        int winner = game.run();

        Player winnerPlayer;
        switch (winner) {
            case 1:
                winnerPlayer = playerX;
                winsX.incrementAndGet();
                break;
            case 2:
                winnerPlayer = playerO;
                winsO.incrementAndGet();
                break;
            default:
                winnerPlayer = null;
                ties.incrementAndGet();
        }
        System.out.println("Winner: " + winnerPlayer + "; score is " + wins1 + ":" + ties + ":" + wins2);
    }
}