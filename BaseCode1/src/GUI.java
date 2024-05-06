import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

// A program that partially implements the 8 puzzle.
public class GUI {
    // The main method is the entry point where the program starts execution.
    public static void main(String[] args) {
        // StdDraw setup
        // -----------------------------------------------------------------------
        // set the size of the canvas (the drawing area) in pixels
        StdDraw.setCanvasSize(500, 500);
        // set the range of both x and y values for the drawing canvas
        StdDraw.setScale(0.5, 3.5);
        // enable double buffering to animate moving the tiles on the board
        StdDraw.enableDoubleBuffering();

        // create the initial and goal boards for the 8 puzzle
        int[][] initialTiles = {
        		{0, 1, 3},
                {4, 2, 5},
                {7, 8, 6} 
        };
        int[][] goalTiles = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };

        // Show the initial state
        System.out.println("Initial State:");
        // if we want to create a random board we can basically use the Board() constructor
        Board initialBoard = new Board(initialTiles);
        initialBoard.draw();
        StdDraw.show();
        StdDraw.pause(2000); // Pause for 2 seconds

        // Find the solution
        System.out.println("Finding solution...");
        Board goalBoard = new Board(goalTiles);
        List<Board> solutionPath = AStarSolver.solve(initialBoard, goalBoard);

        // Animate the solution
        System.out.println("Animating solution...");
        for (Board board : solutionPath) {
            StdDraw.clear();
            board.draw();
            StdDraw.show();
            StdDraw.pause(500); // Pause for 0.5 seconds between frames
        }

        // Display the solution in another window
        displaySolution(solutionPath);
    }

    // Method to display the solution in another window
    private static void displaySolution(List<Board> solutionPath) {
        JFrame frame = new JFrame("Solution");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JLabel label;
        
        // Check if the solution path is empty (indicating the state is not solvable)
        if (solutionPath.isEmpty()) {
            label = new JLabel("The initial state is not solvable.");
        } else {
            label = new JLabel(getSolutionString(solutionPath));
        }

        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
        frame.getContentPane().add(panel);

        frame.setPreferredSize(new Dimension(400, 200));
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }


    // Method to generate the solution string
    private static String getSolutionString(List<Board> solutionPath) {
        StringBuilder solution = new StringBuilder();
        for (Board board : solutionPath) {
            solution.append(board.getLastMove()).append(" ");
        }
        return solution.toString().trim();
    }
}
