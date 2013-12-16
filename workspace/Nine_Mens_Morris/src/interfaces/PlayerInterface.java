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
	
	int getTokensRemaining();
	char getChar();
	void removeToken();
	String getName();
	

}
