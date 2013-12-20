package players;

import interfaces.AbstractPlayer;
import interfaces.GameStateInterface;
import interfaces.PlayerInterface;

public class SimpleAI extends AbstractPlayer {
	
	private final String name = "SimpleAI";
	private char playerChar;
	private boolean turn = false;
	
	public SimpleAI(){
		super();
	}

	@Override
	public int getTokensRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getChar() {
		return playerChar;
	}

	@Override
	public void removeToken() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setChar(char playerChar) {
		this.playerChar = playerChar;
	}

	@Override
	public void setGameState(GameStateInterface gs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTurn() {
		if(turn){
			turn = false;
		}else{
			turn = true;
		}
	}

	@Override
	public boolean getTurn() {
		return turn;
	}

	@Override
	public void setPlayerChar(char playerChar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void intializeCordinates() {
		// TODO Auto-generated method stub
		
	}

}
