package interfaces;

import java.util.Stack;

import model.Phase;
import move.AbstractMove;

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
	
	
	int getGamesPlayed();
	
	int getGamesToPlay();
	
	int getPlayerOneWins();
	
	int getPlayerTwoWins();
	
	void setGamesToPlay(int gamesToPlay);
	
	int getNumberOfDraws();
	
	boolean playerOneWin();
	
	boolean playerTwoWin();
	
	Stack<AbstractMove> getHistory();
	
	boolean gameOver();
}
