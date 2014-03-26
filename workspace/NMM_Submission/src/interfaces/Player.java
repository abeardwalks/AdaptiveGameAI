package interfaces;

/**
 * The Player Interface, to be implemented by any player class. Provides the 
 * methods for ascertaining where to place/remove/move tokens.
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 * 
 */
public interface Player {
	
	/**
	 * Calculates where to place a token on the board.
	 * 
	 * @param game - The current model of the game. 
	 * @return - The position (0-23) that the token should be placed on.
	 */
	public int placeToken(BoardViewInterface game);

	/**
	 * Calculates which opponents token to remove.
	 * 
	 * @param game - The current model of the game.
	 * @return - The position (0-23) that the token should be removed from.
	 */
	public int removeToken(BoardViewInterface game);

	/**
	 * Calculates the movement indexes of the token that should be moved.
	 * 
	 * @param game - The current model of the game.
	 * @return - The Integer Pair that holds the indexes that it should be moved 
	 * 	         from and to. 
	 */
	public IntPairInterface moveToken(BoardViewInterface game);

	/**
	 * Returns the name of the player e.g. Human or Simple AI. 
	 * 
	 * @return - The player name. 
	 */
	public String getName();

	/**
	 * Sets the colour of the players tokens.
	 * 
	 * @param tokenColour - The tokoen colour either: R - Red,
	 * 												  B - Blue. 
	 */
	public void setTokenColour(char tokenColour);

	/**
	 * @return - The token colour of the player.
	 */
	public char getTokenColour();
	
	/**
	 * @return- The player ID. 
	 */
	public int getPlayerID();
	
	/**
	 * Called when the game resets, notifying the player that the game has been reset. 
	 */
	public void reset();

}
