package com.mederly.t3arena.players.minimax;

import com.mederly.t3arena.Board;
import com.mederly.t3arena.GameState;

import java.util.ArrayList;
import java.util.List;

import static com.mederly.t3arena.Board.PLAYER_O;
import static com.mederly.t3arena.Board.PLAYER_X;

/**
 *
 */
public class StateNode {

    /**
     * State of the game.
     */
    private final GameState gameState;

    /**
     * Child states. At index 0 there is a child obtained after selecting move "1".
     * At index 1 a child obtained after selecting move "2", and so on.
     */
    private final StateNode[] children = new StateNode[9];

    /**
     * Minimax evaluation of this state for X.
     * - null means no value yet.
     * - positive values mean that this state is good for "X"
     * - negative values mean that this state is bad for "X"
     */
    private Integer valueForX;

    /**
     * Minimax evaluation of this state for O.
     * - null means no value yet.
     * - positive values mean that this state is good for "O"
     * - negative values mean that this state is bad for "O"
     */
    private Integer valueForO;

    /**
     * Creates the node and its children (recursively).
     *
     * @param gameState state of the game for this node
     */
    public StateNode(GameState gameState) {
        this.gameState = gameState;
        createChildren();
    }

    /**
     * Creates the children.
     */
    private void createChildren() {
        if (gameState.determineWinner() == null) {
            // No result yet, so let's create the children.
            for (int i = 0; i < 9; i++) {
                int fieldNumber = i+1;
                if (gameState.getBoard().getAt(fieldNumber) == 0) {
                    // This field is free, let's record move here.
                    children[i] = new StateNode(new GameState(gameState, fieldNumber));
                } else {
                    // This field is already occupied, so no child here.
                }
            }
        } else {
            // We have the winner information (X, O, tie). So no children will be created for this node.
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public StateNode[] getChildren() {
        return children;
    }

    public Integer getValueForX() {
        return valueForX;
    }

    public Integer getValueForO() {
        return valueForO;
    }

    /**
     * Evaluate this node and its children (recursively), for both X and O players.
     */
    public void evaluate() {
        List<Integer> valuesForX = new ArrayList<>();       // children values for X (for non-null children)
        List<Integer> valuesForO = new ArrayList<>();       // children values for O (for non-null children)
        for (StateNode child : children) {
            if (child != null) {
                child.evaluate();
                valuesForX.add(child.valueForX);
                valuesForO.add(child.valueForO);
            }
        }
        if (valuesForX.isEmpty()) {
            // No relevant children - this must be a leaf node then.
            Integer winner = gameState.getWinner();
            if (winner != null) {
                switch (winner.byteValue()) {
                    case PLAYER_X: valueForX = 100; valueForO = -100; break;        // X won
                    case PLAYER_O: valueForX = -100; valueForO = 100; break;        // O won
                    default: valueForX = 0; valueForO = 0; break;                   // a tie
                }
            } else {
                throw new AssertionError("How could be the winner state null? The game is at its end!");
            }
        } else {
            // There are some children - let's compute the values for X and O players for this node.
            valueForX = gameState.isTurnX() ?
                    getMax(valuesForX) :            // if we play X and this is our turn, we select the maximum
                    getMin(valuesForX);             // if we play X but this is opponent's turn, we select the minimum (worst for us) - assuming O plays optimally
            valueForO = gameState.isTurnO() ?
                    getMax(valuesForO) :            // if we play O and this is our turn, we select the maximum
                    getMin(valuesForO);             // if we play O but this is opponent's turn, we select the minimum (worst for us) - assuming X plays optimally
        }
    }

    // assuming values is non empty
    private Integer getMax(List<Integer> values) {
        int max = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            max = Math.max(max, values.get(i));
        }
        return max;
    }

    // assuming values is non empty
    private Integer getMin(List<Integer> values) {
        int min = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            min = Math.min(min, values.get(i));
        }
        return min;
    }

    public void dumpTree(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(gameState + " -> X: " + valueForX + ", O: " + valueForO);
        for (int i = 0; i < 9; i++) {
            if (children[i] != null) {
                children[i].dumpTree(level + 1);
            }
        }
    }

    @Override
    public String toString() {
        return "StateNode{" +
                "gameState=" + gameState +
                ", valueForX=" + valueForX +
                ", valueForO=" + valueForO +
                '}';
    }

    public List<StateNode> getAllNodes() {
        List<StateNode> allNodes = new ArrayList<>();
        collectAllNodes(allNodes);
        return allNodes;
    }

    private void collectAllNodes(List<StateNode> allNodes) {
        allNodes.add(this);
        for (StateNode child : children) {
            if (child != null) {
                child.collectAllNodes(allNodes);
            }
        }
    }

    public Board getBoard() {
        return gameState.getBoard();
    }
}
