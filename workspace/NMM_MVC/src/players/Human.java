package players;

import interfaces.IntPairInterface;
import interfaces.PlayerInterface;

public class Human implements PlayerInterface {

	private char colour;
	private int id;

	@Override
	public int placeToken(String state) {
		return 0;
	}

	@Override
	public int removeToken(String state) {
		return 0;
	}

	@Override
	public IntPairInterface moveToken(String state) {
		return null;
	}

	@Override
	public String getName() {
		return "Human";
	}

	@Override
	public void setTokenColour(char c) {
		colour = c;
	}

	@Override
	public char getTokenColour() {
		return colour;
	}

	@Override
	public int getPlayerID() {
		return id;
	}

	@Override
	public void setPlayerID(int id) {
		this.id = id;
	}

}
