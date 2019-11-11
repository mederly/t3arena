package com.mederly.t3arena.players.minimax;

import com.mederly.t3arena.GameState;
import com.mederly.t3arena.Player;
import com.mederly.t3arena.players.EqualMoveSelector;

import java.util.ArrayList;
import java.util.List;

import static com.mederly.t3arena.Board.PLAYER_X;

/**
 *  Implementation of a minimax player.
 */
public class MinimaxPlayer implements Player {

    /**
     * Pre-computed minimax tree.
     */
    private StateNode stateTreeRoot;

    /**
     * Where we are in the current game. On each move we simply move down the state tree.
     * On our move, we select the best option to proceed.
     * On opponent's move, we simply take the route he has chosen.
     */
    private StateNode currentNode;

    /**
     * For what side do we play in this game?
     */
    private byte side;

    /**
     * Our name.
     */
    private final String name;

    /**
     * How to select among equally-valued moves.
     */
    private final EqualMoveSelector equalMoveSelector;

    public MinimaxPlayer(String name, EqualMoveSelector equalMoveSelector) {
        this.name = name;
        this.equalMoveSelector = equalMoveSelector;
        computeStateTree();
    }

    @Override
    public void beforeMatch() {
        // nothing to do here
    }

    @Override
    public void beforeGame(byte side) {
        currentNode = stateTreeRoot;
        this.side = side;
    }

    /**
     * This method moves us down the state tree, based on the move taken. (Our or opponent's move.)
     */
    private void registerMove(int chosenField) {
        StateNode newChild = currentNode.getChildren()[chosenField - 1];
        if (newChild != null) {
            currentNode = newChild;
        } else {
            throw new IllegalStateException("Dead end! How's this possible? Current node = " + currentNode + ", chosen field: " + chosenField);
        }
    }

    /**
     * Called by the framework on opponent's move.
     */
    @Override
    public void onOpponentMove(int field) {
        // On the opponent's move we simply move down the three.
        registerMove(field);
    }

    /**
     * Called by the framework to get our move.
     */
    @Override
    public int move() {
        // First let's check the sanity of the game state.
        if (side != currentNode.getGameState().getTurn()) {
            throw new IllegalStateException("We play " + side + " but the current node indicates otherwise: " + currentNode);
        }

        // Now let's select the best child to go to. What "the best" is depends on whether we play X or O.
        int maxValue = Integer.MIN_VALUE;
        List<Integer> bestMoves = new ArrayList<>();
        StateNode[] children = currentNode.getChildren();
        for (int i = 0; i < children.length; i++) {
            StateNode child = children[i];
            if (child != null) {
                int currentValue = side == PLAYER_X ? child.getValueForX() : child.getValueForO();
                if (bestMoves.isEmpty() || currentValue > maxValue) {
                    bestMoves.clear();
                    bestMoves.add(i+1);
                    maxValue = currentValue;
                } else if (currentValue == maxValue) {
                    bestMoves.add(i+1);
                }
            }
        }
        if (!bestMoves.isEmpty()) {
            int myMove;
            if (bestMoves.size() > 1) {
                myMove = equalMoveSelector.selectMove(bestMoves);
            } else {
                myMove = bestMoves.get(0);
            }
            registerMove(myMove);
            return myMove;
        } else {
            // If there's no children, then it must be a terminal state. The framework should not have called us in such situation!
            throw new IllegalStateException("No children to choose from! Current state node = " + currentNode);
        }
    }

    @Override
    public void afterGame(GameState gameState, List<Integer> moves) {
        // nothing to do here
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Creates and evaluates the state tree.
     */
    private void computeStateTree() {
        stateTreeRoot = new StateNode(new GameState());
        stateTreeRoot.evaluate();
    }

    @Override
    public String toString() {
        return name;
    }
}
