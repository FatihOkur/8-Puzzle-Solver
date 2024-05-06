import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;

public class AStarSolver {
	// Solves how to get the initial state to the final state with minimum moves
	public static List<Board> solve(Board initialBoard, Board goalBoard) {
		
		// Check if the initial board state is solvable
	    if (!initialBoard.isSolvable()) {
	        System.out.println("The initial board state is not solvable.");
	        return new ArrayList<>(); // Return an empty list
	    }
	    
        PriorityQueue<Node> openList = new PriorityQueue<>();
        HashSet<Board> closedList = new HashSet<>();
        List<Board> solutionPath = new ArrayList<>();

        openList.add(new Node(initialBoard, null, 0, initialBoard.hCalculatorManhattan(goalBoard)));

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            Board currentBoard = currentNode.getBoard();
            closedList.add(currentBoard);

            if (currentBoard.isGoal(goalBoard)) {
                solutionPath = getSolutionPath(currentNode);
                break;
            }

            for (Board neighbor : currentBoard.generateNeighbors()) {
                if (closedList.contains(neighbor))
                    continue;

                int newG = currentNode.getGValue() + 1;
                int newH = neighbor.hCalculatorManhattan(goalBoard);
                int newF = newG + newH;

                boolean isOpen = false;
                for (Node openNode : openList) {
                    if (openNode.getBoard().equals(neighbor)) {
                        isOpen = true;
                        if (newF < openNode.getFValue()) {
                            openNode.setGValue(newG);
                            openNode.setHValue(newH);
                            openNode.setParent(currentNode);
                        }
                        break;
                    }
                }

                if (!isOpen) {
                    openList.add(new Node(neighbor, currentNode, newG, newH));
                }
            }
        }

        return solutionPath;
    }

    private static List<Board> getSolutionPath(Node goalNode) {
        List<Board> solutionPath = new ArrayList<>();
        Node currentNode = goalNode;

        while (currentNode != null) {
            solutionPath.add(0, currentNode.getBoard()); // Add boards to the front of the list
            currentNode = currentNode.getParent();
        }

        return solutionPath;
    }

    public static void printSolution(Board goalBoard) {
        System.out.println("Solution moves:");
        List<Board> solutionPath = new ArrayList<>();
        
        // Trace back from the goal board to the initial board
        while (goalBoard != null) {
            solutionPath.add(goalBoard);
            goalBoard = goalBoard.getParent();
        }
        
        // Print the moves in reverse order
        for (int i = solutionPath.size() - 1; i > 0; i--) {
            Board currentBoard = solutionPath.get(i);
            Board nextBoard = solutionPath.get(i - 1);
            
            // Determine the move from currentBoard to nextBoard
            String move = determineMove(currentBoard, nextBoard);
            System.out.print(move + " ");
        }
        System.out.println();
    }

    private static String determineMove(Board currentBoard, Board nextBoard) {
        // Retrieve the move from currentBoard to nextBoard
        return nextBoard.getMove();
    }

}