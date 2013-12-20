package interfaces;

import board.MorrisBoard;



/**
 * The Player Interface is used for the creation of players and is critical for
 * adding different AI implementations.
 * 
 * @author Andy
 *
 */
public interface PlayerInterface {
	
	void setTurn();
	boolean getTurn();
	int getTokensRemaining();
	char getChar();
	void removeToken();
	String getName();
	void setChar(char c);
	public void setGameState(GameStateInterface gs);
	
	public void setPlayerChar(char playerChar);
	public void intializeCordinates();
	

}
