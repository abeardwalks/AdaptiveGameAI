package players;

import java.util.Random;

import interfaces.IntPairInterface;
import interfaces.PlayerInterface;

public class RandomAI implements PlayerInterface {

	private Random rdm;
	private char colour;

	@Override
	public int placeToken(String state) {
		rdm = new Random();
		boolean found = false;
		int position = 0;
		while (!found) {
			position = rdm.nextInt(24);
			if (state.charAt(position) == 'N') {
				found = true;
			}
		}
		return position;
	}

	@Override
	public int removeToken(String state) {
		rdm = new Random();
		boolean found = false;
		int position = 0;
		while (!found) {
			position = rdm.nextInt(24);
			if (state.charAt(position) != colour
					&& state.charAt(position) != 'N') {
				found = true;
			}
		}
		return position;
	}

	@Override
	public IntPairInterface moveToken(String state) {
		rdm = new Random();
		boolean found = false;
		int positionFrom = 0;
		int positionToo = 0;
		while (!found) {
			positionFrom = rdm.nextInt(24);
			if (state.charAt(positionFrom) == colour) {
				found = true;
			}
			positionToo = rdm.nextInt(24);
			if (state.charAt(positionToo) == 'N')
				;
		}
		return new IntPair(positionFrom, positionToo);
	}

	@Override
	public String getName() {
		return "Random AI";
	}

	@Override
	public void setTokenColour(char c) {
		colour = c;
	}

	@Override
	public char getTokenColour() {
		return colour;
	}

}
