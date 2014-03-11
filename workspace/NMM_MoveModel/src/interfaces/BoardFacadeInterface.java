package interfaces;

import java.util.List;
import java.util.Stack;

import move.AbstractMove;

/**
 * This is the Facade interface used by MCTS based players. It allows it access to all methods
 * within the board model and the additional methods only required by MCTS type players. 
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 *
 */
public interface BoardFacadeInterface extends BoardDetailsInterface, BoardControllerInterface {
	
	/**
	 * Evaluates the current board state and returns the relevant scores.
	 * 
	 * @return The rewards of the board. 
	 */
	double[] getRewards();
	
	void printDetails();
	
	/**
	 * Sets the trapped player of this game. 
	 * 
	 * @param trappedPlayer - The Trapped Player. 
	 */
	void setTrappedPlayer(char trappedPlayer);
	
	char getTrappedPlayer();

	List<AbstractMove> getAllPossibleMoves();
	
	AbstractMove lastMovePlayed();
	
	Stack<AbstractMove> getHistory();

}
