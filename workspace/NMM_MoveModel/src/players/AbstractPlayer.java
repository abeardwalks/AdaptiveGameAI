package players;

import interfaces.BoardDetailsInterface;
import interfaces.IntPairInterface;
import interfaces.Player;

/**
 * The abstract player, providing the common functionality that all
 * players have. E.g. Their token colour and ID. 
 * 
 * @author Andy
 *
 */
public abstract class AbstractPlayer implements Player {
	
	private char tokenColour;

	@Override
	public abstract int placeToken(BoardDetailsInterface game);

	@Override
	public abstract int removeToken(BoardDetailsInterface game);

	@Override
	public abstract IntPairInterface moveToken(BoardDetailsInterface state);

	@Override
	public abstract String getName();

	@Override
	public void setTokenColour(char tokenColour) {
		this.tokenColour = tokenColour;
	}

	@Override
	public char getTokenColour() {
		return tokenColour;
	}
	
	@Override
	public int getPlayerID(){
		if(tokenColour == 'R'){
			return 1;
		}else{
			return 2;
		}
	}
	
	
}
