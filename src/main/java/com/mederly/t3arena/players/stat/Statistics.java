package com.mederly.t3arena.players.stat;

import static com.mederly.t3arena.Board.PLAYER_O;
import static com.mederly.t3arena.Board.PLAYER_X;

/**
 *  Statistics for a given position.
 */
public class Statistics {

    private int winX;
    private int winO;
    private int ties;

    public int getWinX() {
        return winX;
    }

    public void setWinX(int winX) {
        this.winX = winX;
    }

    public int getWinO() {
        return winO;
    }

    public void setWinO(int winO) {
        this.winO = winO;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public void increment(int winner) {
        switch (winner) {
            case PLAYER_X: winX++; return;
            case PLAYER_O: winO++; return;
            default: ties++;
        }
    }

    public int getSamples() {
        return winX + winO + ties;
    }

    public double getWinRatio(byte side) {
        if (getSamples() == 0) {
            return 0;
        }
        switch (side) {
            case PLAYER_X: return (double) winX / getSamples();
            case PLAYER_O: return (double) winO / getSamples();
            default: throw new IllegalArgumentException("Illegal side: " + side);
        }
    }
}
