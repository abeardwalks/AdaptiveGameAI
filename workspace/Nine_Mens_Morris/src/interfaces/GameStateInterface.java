package interfaces;

import Player.Token;

/**
 * Interface that represents the state of the game.
 * 
 * @author Andy
 *
 */
public interface GameStateInterface {
	
	/**
	 * Adds a players token to the game and updates the phase of
	 * game if necessary.
	 * 
	 * @param token, the Token from the player. 
	 * @return will return a number between -1 & 2.
	 *  > -1 signifies an invalid placement.
	 *  >  0 signifies a valid placement.
	 *  >  1 signifies a valid placement and the creation of a mill.
	 *  >  2 Game won.
	 */
	int addToken(Token token);
	
	/**
	 * Removes a players token from the game and updates the phase of 
	 * game if necessary.
	 * 
	 * @param token, the token to remove from the game.
	 * @return will return a number between -1 & 2.
	 * 	> -1 signifies an invalid selection.
	 *  >  0 signifies a valid removal.
	 *  >  2 Game won.
	 */
	int removeToken(Token token);
	
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
	int moveToken(Token token, int x, int y);
	
	/**
	 * Returns the current state of the game. 
	 * 
	 * @return
	 */
	GameStateInterface getState();
	
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
	
}
