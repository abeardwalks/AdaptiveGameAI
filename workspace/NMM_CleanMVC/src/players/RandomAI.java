package players;

import java.util.Random;

import interfaces.BoardDetailsInterface;
import interfaces.IntPairInterface;

public class RandomAI extends AbstractPlayer {

	private Random rdm = new Random();
	
	@Override
	public int placeToken(BoardDetailsInterface game) {
		char[] stateArray = game.getState().toCharArray();
		boolean found = false;
		int index = 0;
		while(!found){
			index = rdm.nextInt(24);
			if(stateArray[index] == 'N'){
				found = true; 
			}
		}
		return index;
	}
	@Override
	public int removeToken(BoardDetailsInterface game) {
		char[] stateArray = game.getState().toCharArray();
		boolean found = false;
		int index = 0;
		while(!found){
			index = rdm.nextInt(24);
			if(stateArray[index] != this.getTokenColour() && stateArray[index] != 'N'){
				found = true; 
			}
		}
		return index;
	}
	@Override
	public IntPairInterface moveToken(BoardDetailsInterface game) {
		char[] stateArray = game.getState().toCharArray();
		boolean found = false;
		int positionFrom = 0;
		int positionToo = 0;
		while (!found) {
			positionFrom = rdm.nextInt(24);
			positionToo = rdm.nextInt(24);
			if (stateArray[positionFrom] == this.getTokenColour() && stateArray[positionToo] == 'N' ) {
				found = true;
			}
		}
		return new IntPair(positionFrom, positionToo);
	}
	@Override
	public String getName() {
		return "Random AI";
	}

	

}
