package model.board;

import java.util.Observable;

import model.Phase;
import interfaces.BoardDetailsInterface;
import interfaces.BoardMutatorInterface;

public class BoardModel extends Observable implements BoardDetailsInterface, BoardMutatorInterface {
	
	private String state;
	private 

	@Override
	public void addToken(char playerColour, int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeToken(char playerColour, int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveToken(int from, int to) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlayerOneToPlace() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlayerTwoToPlace() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlayerOneRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlayerTwoRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Phase getPhase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTurn() {
		// TODO Auto-generated method stub
		return 0;
	}

}
