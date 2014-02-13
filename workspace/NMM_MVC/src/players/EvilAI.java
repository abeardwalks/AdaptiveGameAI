package players;

import interfaces.IntPairInterface;
import interfaces.PlayerInterface;

public class EvilAI implements PlayerInterface {

	private char colour;
	
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
		return "Evil AI";
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPlayerID(int id) {
		// TODO Auto-generated method stub
		
	}
	
}
