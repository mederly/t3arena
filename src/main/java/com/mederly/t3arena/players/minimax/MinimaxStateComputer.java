package com.mederly.t3arena.players.minimax;

import com.mederly.t3arena.Board;
import com.mederly.t3arena.GameState;

import java.util.List;

/**
 *  Provides computed minimax values. Used for diagnostics.
 */
public class MinimaxStateComputer {

    public static void main(String[] args) {
        StateNode stateTreeRoot = new StateNode(new GameState());
        stateTreeRoot.evaluate();

//        stateTreeRoot.dumpTree(0);
//        System.out.println("--------------------------------------------------------");

        System.out.println(String.format("%7s\t%9s\t%9s\t%4s\t%4s\t%4s", "#", "Board", "Num", "Turn", "ValX", "ValO"));
        List<StateNode> allStates = stateTreeRoot.getAllNodes();
        for (int i = 0; i < allStates.size(); i++) {
            StateNode state = allStates.get(i);
            Board board = state.getGameState().getBoard();
            System.out.println(String.format("%7s\t%9s\t%9d\t%4d\t%4d\t%4d", i+1, board.getStringRepresentation(),
                    board.getNumericRepresentation(), state.getGameState().getTurn(), state.getValueForX(),
                    state.getValueForO()));
        }
    }
}
