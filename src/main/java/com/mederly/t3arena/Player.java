package com.mederly.t3arena;

/**
 * Representation of a Tic-Tac-Toe player.
 *
 * T3 Arena calls an instance of this class in order to make its moves.
 */
public interface Player {

    /**
     * This method is called before a match (i.e. a series of games) starts.
     * The player can use it for its global initialization.
     */
    void beforeMatch();

    /**
     * This method is called before a single game starts.
     * @param side Which side we are playing for? 1 = PLAYER_X, 2 = PLAYER_O
     */
    void beforeGame(byte side);

    /**
     * Called on opponent move.
     *
     * @param field An absolute number of a field that has been marked by the opponent (1 = upper left,
     *              2 = upper center, 3 = upper right, 4 = center left, 5 = center, 6 = center right,
     *              7 = down left, 8 = down center, 9 = down right)
     */
    void onOpponentMove(int field);

    /**
     * Obtains our move.
     *
     * @return The move to be made (1..9)
     */
    int move();

    /**
     * @return The name of this player.
     */
    String getName();
}
