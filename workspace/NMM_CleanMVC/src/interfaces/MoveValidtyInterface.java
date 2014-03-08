package interfaces;

public interface MoveValidtyInterface {

	/**
	 * Checkes a placement of a token to the board, returning the result of the placement.
	 * 
	 * @param tokenColour - The colour of the token to be placed.
	 * @param    position - The index (0-23) that the token is to be placed.
	 * @return - The success of the move: -1 - Illegal placement.
	 * 									   0 - Valid Placement.
	 * 									   1 - Valid Placement, Mill Created.
	 * 									   2 - Valid Placement, Game Won. 
	 */
	void placeToken(char tokenColour, int position);
	
	/**
	 * Checks the removal of a token from the board, returning the result of the removal.
	 * 
	 * @param tokenColour - The colour of the player removing the token.
	 * @param    position - The index (0-23) that the token is to be removed from.
	 * @return - The success of the move: -1 - Illegal Removal.
	 * 									   0 - Valid Removal.
	 * 									   2 - Valid Removal, Game Won. 
	 */
	void removeToken(char token, int position);
	
	/**
	 * Checks the move of a token on the board, returning the result of the movement.
	 * 
	 * @param token - The colour of the player moving the token.
	 * @param  from - The position the token is moving from.
	 * @param    to - The position the token is moving to.
	 * @return - The success of the move: -1 - Illegal movement.
	 * 									   0 - Valid movement.
	 * 									   1 - Valid movement, Mill Created.
	 * 									   2 - Valid movement, Game Won. 
	 */
	void moveToken(char token, int from, int to);
	
	boolean wasMoveLegal();
	
	boolean wasMillMade();
	
}
