package players;

import interfaces.IntPairInterface;
import interfaces.PlayerInterface;

public class Human implements PlayerInterface {

	private char c;

	@Override
	public int placeToken(String game) {
		return 0;
	}

	@Override
	public int removeToken(String game) {
		return 0;
	}

	@Override
	public IntPairInterface moveToken(String gamee) {
		return null;
	}

	@Override
	public String getName() {
		return "Human";
	}

	@Override
	public void setTokenColour(char c) {
		this.c = c;
	}

	@Override
	public char getTokenColour() {
		return c;
	}

}
