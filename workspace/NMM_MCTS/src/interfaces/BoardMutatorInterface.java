package interfaces;

/**
 * Interface to allow the controller to modify the game state. It decouples it 
 * from the rest of the program and hides methods not required by the controller.
 * 
 * @author Andrew White - 200939787, BSc software Engineering
 *
 */
public interface BoardMutatorInterface {
	
	/**
	 * Adds a token to the board i.e. the model.
	 * 
	 * @param playerColour - The colour of the players who is performing the action 
	 * 						 i.e. the colour of token to be placed..
	 * @param     position - The index (0-23) of where the token should be placed.
	 */
	void addToken(char playerColour, int position);
	
	/**
	 * Removes a token from the board i.e. the model.
	 * 
	 * @param playerColour - The colour of the player who is performing the action
	 * @param     position - The index (0-23) of where the token should be removed from.
	 */
	void removeToken(char playerColour, int position);
	
	/**
	 * Moves a token from one position to another.
	 * 
	 * @param from - The index (0-23) of where the token currently resides.
	 * @param   to - The index (0-23) of where the token is moving to. 
	 */
	void moveToken(int from, int to);
	
	/**
	 * Resets the entire state of the game. 
	 */
	void reset();

}
