package interfaces;

import model.Phase;

public interface GameStateInterface {
	

	void addToken(char token, int position);
	
	void removeToken(int position);
	
	void moveToken(int x, int y);
	
	void lowerPlayerOneCount();
	
	void lowerPlayerTwoCount();
	
	void setTurn();
	
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

	void setResult(int result);

}
