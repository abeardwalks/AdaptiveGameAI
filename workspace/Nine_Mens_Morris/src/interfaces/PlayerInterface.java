package interfaces;

import Player.Token;

/**
 * The Player Interface is used for the creation of players and is critical for
 * adding different AI implementations.
 * 
 * @author Andy
 *
 */
public interface PlayerInterface {
	
	Token makeMove(GameStateInterface gs);

}
