package interfaces;

import java.util.Observer;




/**
 * The Player Interface is used for the creation of players and is critical for
 * adding different AI implementations.
 * 
 * @author Andy
 *
 */
public interface PlayerInterface extends Observer{
	
	String getName();
	char getTokenColour();
	void setTokenColour(char c);
	void intialize(GameStateInterface gs);
	void makeMove();

}
