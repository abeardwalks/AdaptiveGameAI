package interfaces;

import model.Phase;

public interface BoardModelSharedMethodsInterface {
	
	boolean gameWon();
	
	boolean millMade();
	
	boolean validMove();
	
	int getTurn();
	
	/**
	 * @return - The current phase of the game.
	 */
	Phase getPhase();
	
	/**
	 * Returns a String of 24 characters that represents the board.
	 * 
	 * @return - The state of the board.
	 */
	String getState();
	
	int getGamesPlayed();
	
	int getGamesToPlay();
	
	int getPlayerOneWins();
	
	int getPlayerTwoWins();
	
	void setGamesToPlay(int gamesToPlay);
	
	int getNumberOfDraws();
}
