package players;

import interfaces.BoardViewInterface;
import interfaces.IntPairInterface;

public class Human extends AbstractPlayer {


	@Override
	public int placeToken(BoardViewInterface game) {
		return 0;
	}

	@Override
	public int removeToken(BoardViewInterface game) {
		return 0;
	}

	@Override
	public IntPairInterface moveToken(BoardViewInterface gamee) {
		return null;
	}

	@Override
	public String getName() {
		return "Human";
	}
	
	public void reset(){
	}

}
