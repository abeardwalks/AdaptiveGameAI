package interfaces;


import board.Phase;

/**
 * Interface that represents the state of the game.
 * 
 * @author Andy
 *
 */
public interface GameStateInterface{
	
	/**
	 * Adds a players token to the game and updates the phase of
	 * game if necessary.
	 * 
	 * @param token, the callers token colour. 
	 * @return will return a number between -1 & 2.
	 *  > -1 signifies an invalid placement.
	 *  >  0 signifies a valid placement.
	 *  >  1 signifies a valid placement and the creation of a mill.
	 *  >  2 Game won.
	 */
	int addToken(char token, int position);
	
	/**
	 * Removes a players token from the game and updates the phase of 
	 * game if necessary.
	 * 
	 * @param token, the callers token colour.
	 * @return will return a number between -1 & 2.
	 * 	> -1 signifies an invalid selection.
	 *  >  0 signifies a valid removal.
	 *  >  2 Game won.
	 */
	int removeToken(char token, int position);
	
	/**
	 * Moves a selected Token from one point to another.
	 * 
	 * @param token, the token to move.
	 * @param x to move to.
	 * @param y to move to.
	 * @return will return a number between -1 & 2.
	 *  > -1 signifies an invalid move.
	 *  >  0 signifies a valid move.
	 *  >  1 signifies a valid move an a mill created.
	 *  >  2 signifies game won.
	 */
	int moveToken(char token, int x, int y);
	
	/**
	 * Returns the current state of the game. 
	 * 
	 * @return
	 */
	GameStateInterface getGame();
	
	/**
	 * Returns the string representation of the game state.
	 * 
	 * @return - the game state string.
	 */
	String getState();
	
	/**
	 * For testing purposes.
	 * 
	 * @param state - allows the game state to be set for testing purposes. 
	 */
	void setState(String state);
	
	/**
	 * Only public for testing purposes.
	 */
	boolean playersCanMove();
	
	/**
	 * For testing purposes.
	 * @param phase
	 */
	void setPhase(Phase phase);
	
	/**
	 * Resets the game.
	 */
	void reset();
	
	/**
	 * Undoes the previous action.
	 */
	void undo();
	
	/**
	 * Re-does the previous undone action.
	 */
	void redo();

	Phase getPhase();

	
}
