package interfaces;

import model.Phase;

/**
 * Interface to allow the view, and players, to ascertain the state of the current game.
 * It decouples it from the rest of the program and hides functionality not required by 
 * the view and the players. 
 * 
 * @author Andrew White - 200939787, BSc software Engineering
 *
 */
public interface BoardDetailsInterface {
	
	/**
	 * Returns a String of 24 characters that represents the board.
	 * 
	 * @return - The state of the board.
	 */
	String getState();
	
	/**
	 * @return - The number of tokens player one has left to place on the board.
	 */
	int getPlayerOneToPlace();
	
	/**
	 * @return - The number of tokens player two has left to place on the board.
	 */
	int getPlayerTwoToPlace();
	
	/**
	 * @return - The number of tokens player one has left in the game.
	 */
	int getPlayerOneRemaining();
	
	/**
	 * @return - The number of tokens player two has left in the game.
	 */
	int getPlayerTwoRemaining();
	
	/**
	 * @return - The current phase of the game.
	 */
	Phase getPhase();
	
	/**
	 * @return - the player ID number of whose turn it currently is. 
	 */
	int getTurn();
	
	/**
	 * @return - true if the game has been won, false otherwise.
	 */
	boolean gameWon();

}
