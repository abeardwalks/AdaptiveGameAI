package players;

import interfaces.GameStateInterface;
import interfaces.IntPairInterface;
import interfaces.MCTSInterface;
import interfaces.PlayerInterface;

public class MCTSAI implements MCTSInterface {
	
	private int nPlayers;
	private int playerID;
	private GameStateInterface game;

	@Override
	public int placeToken(String state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeToken(String state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IntPairInterface moveToken(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTokenColour(char c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public char getTokenColour() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlayerID() {
		return playerID;
	}

	@Override
	public void setPlayerID(int id) {
		playerID = id;
	}

	@Override
	public void initialise(GameStateInterface game) {
		this.game = game;
	}

}
