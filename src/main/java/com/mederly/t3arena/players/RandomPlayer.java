package com.mederly.t3arena.players;

import java.util.List;

/**
 *  A player that plays randomly.
 */
public class RandomPlayer extends PlayerBase {

    public RandomPlayer(String name) {
        super(name);
    }

    @Override
    protected int selectMyMove(List<Integer> freeFields) {
        int selected = (int) (Math.random() * freeFields.size());
        return freeFields.get(selected);
    }
}
