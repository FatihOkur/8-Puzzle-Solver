import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


 // The Board class represents the state of a puzzle board.
 // It contains methods to perform moves, generate neighbors, calculate heuristic values, and more.
 
public class Board implements Comparable<Board> {
    // Constants for colors and line thickness
    private static final Color backgroundColor = new Color(145, 234, 255);
    private static final Color boxColor = new Color(31, 160, 239);
    private static final double lineThickness = 0.02;

    private int[][] tiles; // Represents the arrangement of tiles on the board
    private int emptyCellRow; // Row index of the empty cell
    private int emptyCellCol; // Column index of the empty cell
    private List<String> moves; // List to store the sequence of moves made
    private String move; // Represents the latest move made
    private Board parent;
    
    // Constructors

    
    // Constructs a new board with random tile arrangement.
     
    public Board() {
        tiles = new int[3][3];
        moves = new ArrayList<>();
        initializeRandomBoard();
    }

    // Constructs a board with the given tile arrangement.
     
    public Board(int[][] tiles) {
        this.tiles = tiles;
        moves = new ArrayList<>();
        findEmptyCellPosition();
    }
    
    // Private helper method to initialize a random board
    private void initializeRandomBoard() {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 0));
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int randomIndex = (int) (Math.random() * numbers.size());
                tiles[row][col] = numbers.get(randomIndex);
                if (numbers.get(randomIndex) == 0) {
                    emptyCellRow = row;
                    emptyCellCol = col;
                }
                numbers.remove(randomIndex);
            }
        }
    }

    // Methods for moving tiles

    // Moves the empty cell to the right if possible.
     
    public boolean moveRight() {
        if (emptyCellCol == 2)
            return false;

        swapTiles(emptyCellRow, emptyCellCol, emptyCellRow, emptyCellCol + 1);
        emptyCellCol++;
        moves.add("R");
        System.out.println("Moved Right");
        return true;
    }

    // Moves the empty cell to the left if possible.
     
    public boolean moveLeft() {
        if (emptyCellCol == 0)
            return false;

        swapTiles(emptyCellRow, emptyCellCol, emptyCellRow, emptyCellCol - 1);
        emptyCellCol--;
        moves.add("L");
        System.out.println("Moved Left");
        return true;
    }

    // Moves the empty cell up if possible.
     
    public boolean moveUp() {
        if (emptyCellRow == 0)
            return false;

        swapTiles(emptyCellRow, emptyCellCol, emptyCellRow - 1, emptyCellCol);
        emptyCellRow--;
        moves.add("U");
        System.out.println("Moved Up");
        return true;
    }

    // Moves the empty cell down if possible.
     
    public boolean moveDown() {
        if (emptyCellRow == 2)
            return false;

        swapTiles(emptyCellRow, emptyCellCol, emptyCellRow + 1, emptyCellCol);
        emptyCellRow++;
        moves.add("D");
        System.out.println("Moved Down");
        return true;
    }

    // Private helper method to swap tiles
    private void swapTiles(int row1, int col1, int row2, int col2) {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }

    // Methods for drawing the board

     // Draws the current state of the board.
     
    public void draw() {
        StdDraw.clear(backgroundColor);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (tiles[row][col] != 0) {
                    Point tilePosition = getTilePosition(row, col);
                    StdDraw.setPenColor(boxColor);
                    StdDraw.filledSquare(tilePosition.x, tilePosition.y, 0.5);
                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.text(tilePosition.x, tilePosition.y, Integer.toString(tiles[row][col]));
                }
            }
        }
        StdDraw.setPenColor(boxColor);
        StdDraw.setPenRadius(lineThickness);
        StdDraw.square(1.5, 1.5, 1);
        StdDraw.setPenRadius();
    }

    // Private helper method to calculate tile position for drawing
    private Point getTilePosition(int rowIndex, int colIndex) {
        int posX = colIndex + 1;
        int posY = 3 - rowIndex;
        return new Point(posX, posY);
    }

    // Getter methods

    // Gets the current tile arrangement.
    
    public int[][] getTiles() {
        return tiles;
    }

    // Gets the row index of the empty cell.
     
    public int getEmptyCellRow() {
        return emptyCellRow;
    }

    // Gets the column index of the empty cell.
     
    public int getEmptyCellCol() {
        return emptyCellCol;
    }

    // Heuristic calculation methods

    // Calculates the F-value of the board based on a given goal board.
     
    public int getFValue(Board goalBoard) {
        return getGValue() + hCalculatorManhattan(goalBoard);
    }

    // Gets the G-value, which represents the number of moves made so far.
     
    public int getGValue() {
        return moves.size();
    }

    // Calculates the Manhattan distance heuristic value based on a given goal board.
     
    public int hCalculatorManhattan(Board goalBoard) {
        if (goalBoard == null) {
            return 0; // Return 0 if goalBoard is null
        }

        int h = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int value = tiles[i][j];
                if (value != 0) {
                    int goalRow = goalBoard.getRow(value);
                    int goalCol = goalBoard.getColumn(value);
                    h += Math.abs(i - goalRow) + Math.abs(j - goalCol);
                }
            }
        }

        return h;
    }
    
    // Calculates the number of misplaced tiles heuristic value based on a given goal board.
    public int hCalculatorMisplaced(Board goalBoard) {
        if (goalBoard == null) {
            return 0; // Return 0 if goalBoard is null
        }

        int misplaced = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tiles[i][j] != goalBoard.getTiles()[i][j]) {
                    misplaced++;
                }
            }
        }
        return misplaced;
    }

    // Method for generating neighboring boards

    // Generates neighboring boards by making possible moves.
     
    public List<Board> generateNeighbors() {
        List<Board> neighbors = new ArrayList<>();
        
        if (canMoveRight()) {
            Board neighbor = createNeighbor();
            neighbor.moveRight();
            neighbor.setParent(this); // Set the parent of the new board
            neighbor.setMove("R"); // Set the move for the neighbor board
            neighbors.add(neighbor);
        }
        
        if (canMoveLeft()) {
            Board neighbor = createNeighbor();
            neighbor.moveLeft();
            neighbor.setParent(this); // Set the parent of the new board
            neighbor.setMove("L"); // Set the move for the neighbor board
            neighbors.add(neighbor);
        }
        
        if (canMoveUp()) {
            Board neighbor = createNeighbor();
            neighbor.moveUp();
            neighbor.setParent(this); // Set the parent of the new board
            neighbor.setMove("U"); // Set the move for the neighbor board
            neighbors.add(neighbor);
        }
        
        if (canMoveDown()) {
            Board neighbor = createNeighbor();
            neighbor.moveDown();
            neighbor.setParent(this); // Set the parent of the new board
            neighbor.setMove("D"); // Set the move for the neighbor board
            neighbors.add(neighbor);
        }

        return neighbors;
    }

    // Private helper methods for checking possible moves

    private boolean canMoveRight() {
        return emptyCellCol < 2;
    }

    private boolean canMoveLeft() {
        return emptyCellCol > 0;
    }

    private boolean canMoveUp() {
        return emptyCellRow > 0;
    }

    private boolean canMoveDown() {
        return emptyCellRow < 2;
    }

    // Private helper method to create a neighbor board

    private Board createNeighbor() {
        int[][] neighborTiles = Arrays.stream(tiles).map(int[]::clone).toArray(int[][]::new);
        return new Board(neighborTiles);
    }

    // Methods for accessing specific tile positions

    public int getRow(int value) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tiles[i][j] == value) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getColumn(int value) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tiles[i][j] == value) {
                    return j;
                }
            }
        }
        return -1;
    }

    // Methods for checking solvability and goal state

    // Checks if the current board is solvable.
     
    public boolean isSolvable() {
        int inversions = countInversions();
        return inversions % 2 == 0;
    }

    // Private helper method to count inversions

    private int countInversions() {
        int[] array = new int[9];
        int k = 0;
        for (int[] row : tiles) {
            for (int value : row) {
                array[k++] = value;
            }
        }

        int inversions = 0;
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] > array[j] && array[i] != 0 && array[j] != 0) {
                    inversions++;
                }
            }
        }
        return inversions;
    }

    // Checks if the current board is the goal state.
     
    public boolean isGoal(Board goalBoard) {
        return Arrays.deepEquals(this.tiles, goalBoard.tiles);
    }

    // Methods for accessing move history

    // Gets the list of moves made so far.
     
    public List<String> getMoves() {
        return moves;
    }

    // Gets the last move made.
    
    public String getLastMove() {
        if (moves.isEmpty()) {
            return "";
        }
        return moves.get(moves.size() - 1);
    }

    // Setter methods

    // Sets the G-value based on the number of moves.
    
    public void setGValue(int gValue) {
        moves = new ArrayList<>(moves.subList(0, Math.min(gValue, moves.size())));
    }
    
    // Sets the parent board.
    public void setParent(Board parent) {
        this.parent = parent;
    }

    // Gets the parent board.
    public Board getParent() {
        return parent;
    }

    // Method for comparing boards based on their F-values

    @Override
    public int compareTo(Board other) {
        return Integer.compare(this.getFValue(null), other.getFValue(null));
    }

    // Private helper method to find the position of the empty cell
    private void findEmptyCellPosition() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tiles[i][j] == 0) {
                    emptyCellRow = i;
                    emptyCellCol = j;
                    return;
                }
            }
        }
    }

    // Getter and setter for the move field

    // Gets the latest move made.    
    public String getMove() {
        return move;
    }

    
    //Sets the latest move made.
    public void setMove(String move) {
        this.move = move;
    }
}
