package com.mederly.t3arena;

import com.mederly.t3arena.players.RandomPlayer;
import com.mederly.t3arena.players.SequentialPlayer;

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
        Player sequential1 = new SequentialPlayer();
        Player sequential2 = new SequentialPlayer();
        Player random1 = new RandomPlayer();
        Player random2 = new RandomPlayer();

        // Matches
        matches = new ArrayList<>();
        runMatch(sequential1, random1);
        runMatch(sequential1, sequential2);
        runMatch(random1, random2);

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
