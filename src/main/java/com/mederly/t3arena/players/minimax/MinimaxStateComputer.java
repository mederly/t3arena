package com.mederly.t3arena.players.minimax;

import com.mederly.t3arena.Board;
import com.mederly.t3arena.GameState;

import java.util.*;

/**
 *  Provides computed minimax values. Used for diagnostics.
 */
public class MinimaxStateComputer {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        StateNode stateTreeRoot = new StateNode(new GameState());
        stateTreeRoot.evaluate();
        System.out.println("State tree computed in " + (System.currentTimeMillis() - start) + " ms");

//        stateTreeRoot.dumpTree(0);
//        dumpStates(stateTreeRoot.getAllNodes());

        Map<Board, StateNode> uniqueStates = getUniqueStates(stateTreeRoot);
        dumpStates(uniqueStates.values());
    }

    /**
     * Selects unique states i.e. states that have different boards. Checks that states with the same boards have the same
     * evaluations for X and O.
     */
    private static Map<Board, StateNode> getUniqueStates(StateNode stateTreeRoot) {
        Map<Board, StateNode> uniqueStates = new TreeMap<>();
        for (StateNode stateNode : stateTreeRoot.getAllNodes()) {
            Board board = stateNode.getBoard();
            StateNode existingNode = uniqueStates.get(board);
            if (existingNode != null) {
                if (stateNode.getValueForX().intValue() != existingNode.getValueForX().intValue() ||
                        stateNode.getValueForO().intValue() != existingNode.getValueForO().intValue()) {
                    throw new IllegalStateException("Same board, different evaluations: " + stateNode + " vs " + existingNode);
                }
            } else {
                uniqueStates.put(board, stateNode);
            }
        }
        return uniqueStates;
    }

    private static void dumpStates(Collection<StateNode> allStates) {
        System.out.println(String.format("%7s\t%9s\t%9s\t%4s\t%4s\t%4s", "#", "Board", "Num", "Turn", "ValX", "ValO"));
        Iterator<StateNode> iterator = allStates.iterator();
        for (int i = 0; i < allStates.size(); i++) {
            StateNode state = iterator.next();
            Board board = state.getGameState().getBoard();
            System.out.println(String.format("%7s\t%9s\t%9d\t%4d\t%4d\t%4d", i+1, board.getStringRepresentation(),
                    board.getNumericRepresentation(), state.getGameState().getTurn(), state.getValueForX(),
                    state.getValueForO()));
        }
    }
}
