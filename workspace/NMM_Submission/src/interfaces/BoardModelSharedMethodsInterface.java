package interfaces;

import java.util.Stack;

import model.Phase;
import move.AbstractMove;

/**
 * Group of methods used by multiple parts of the program, this interfaces helps to prevent 
 * duplication. 
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 *
 */
public interface BoardModelSharedMethodsInterface {
	
	boolean gameWon();
	
	/**
	 * @return True if the last move executed was valid, false otherwise.
	 */
	boolean validMove();
	
	/**
	 * @return True if the last move executed created a mill, false otherwise.
	 */
	boolean millMade();
	
	
	int getTurn();
	
	/**
	 * @return - The current phase of the game.
	 */
	Phase getPhase();
	
	/**
	 * @return - did player one win? (True/False)
	 */
	boolean playerOneWin();
	
	/**
	 * @return - did player two win? (True/False)
	 */
	boolean playerTwoWin();
	
	/**
	 * @return - the stack of moves executed on the game.
	 */
	Stack<AbstractMove> getHistory();
	
	/**
	 * @return - is the game over? (True/False). 
	 */
	boolean gameOver();
}
