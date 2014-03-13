package players;

import interfaces.BoardViewInterface;
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
	public abstract int placeToken(BoardViewInterface game);

	@Override
	public abstract int removeToken(BoardViewInterface game);

	@Override
	public abstract IntPairInterface moveToken(BoardViewInterface state);

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
	
	public abstract void reset();
	
	
}
