package com.company;

/**
 * Created by user on 12/05/2016.
 */
public class Node {
    private boolean parenthesis;//0 for '{', 1 for '['
    private String currentFatherNode;

    public Node(boolean parenthesis, String currentFatherNode) {
        this.parenthesis = parenthesis;
        this.currentFatherNode = currentFatherNode;
    }

    public boolean isParenthesis() {
        return parenthesis;
    }

    public String getCurrentFatherNode() {
        return currentFatherNode;
    }
}
