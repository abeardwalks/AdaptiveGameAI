package players;

import interfaces.PlayerInterface;

public class SimpleAI implements PlayerInterface {
	
	private final String name = "SimpleAI";

	@Override
	public int getTokensRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getChar() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeToken() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return name;
	}

}
