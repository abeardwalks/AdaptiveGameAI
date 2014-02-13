package model.board;

import players.IntPair;
import interfaces.IntPairInterface;

public class Move {
	
	private int placementPosition;
	private IntPairInterface movement;
	private int removalPosition;
	
	private String state;
	private char action, token;

	public Move(String state, char action, char token) {
		this.state = state;
		this.action = action;
		this.token = token;
	}

	public void setPlacement(int i) {
		placementPosition = i;
	}

	public void setMovement(int i, int j) {
		movement = new IntPair(i, j);
	}

	public void setRemoval(int i) {
		removalPosition = i;
	}
	
	public int getPlacement(){
		return placementPosition;
	}
	
	public IntPairInterface getMovement(){
		return movement;
	}
	
	public int getRemoval(){
		return removalPosition;
	}
	
	public String getStateActedOn(){
		return state;
	}
	
	public char getAction(){
		return action;
	}
	
	public char getPlayer(){
		return token;
	}
	
	public String getStateAfterMove(){
		
		char[] stateArray = state.toCharArray();
		
		switch (action) {
		case 'P':
			stateArray[getPlacement()] = getPlayer();
			break;
		case 'M': 
			IntPairInterface ip = getMovement();
			stateArray[ip.getFirstInt()] = 'N';
			stateArray[ip.getSecondInt()] = getPlayer();
			break;
		case 'R':
			stateArray[getRemoval()] = 'N';
			break;
		default:
			break;
		}
		
		String newState = stateArray.toString();
		
		return newState;
	}

}
