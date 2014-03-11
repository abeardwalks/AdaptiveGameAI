package players;

import interfaces.BoardDetailsInterface;
import interfaces.IntPairInterface;

public class Human extends AbstractPlayer {


	@Override
	public int placeToken(BoardDetailsInterface game) {
		return 0;
	}

	@Override
	public int removeToken(BoardDetailsInterface game) {
		return 0;
	}

	@Override
	public IntPairInterface moveToken(BoardDetailsInterface gamee) {
		return null;
	}

	@Override
	public String getName() {
		return "Human";
	}
	
	public void reset(){
	}

}
