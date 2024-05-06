public class Node implements Comparable<Node> {
    private Board board;
    private Node parent;
    private int gValue; // Cost from start to current node
    private int hValue; // Heuristic value (Manhattan distance to goal)
    private String move; // Move made to reach this state from the parent state

    public Node(Board board, Node parent, int gValue, int hValue) {
        this.board = board;
        this.parent = parent;
        this.gValue = gValue;
        this.hValue = hValue;
    }

    // Getters and setters
    public Board getBoard() {
        return board;
    }

    public Node getParent() {
        return parent;
    }

    public int getGValue() {
        return gValue;
    }

    public void setGValue(int gValue) {
        this.gValue = gValue;
    }

    public int getHValue() {
        return hValue;
    }

    public void setHValue(int hValue) {
        this.hValue = hValue;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    // Calculate the total cost (fValue)
    public int getFValue() {
        return gValue + hValue;
    }

    // Implement compareTo method for priority queue ordering
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.getFValue(), other.getFValue());
    }
}