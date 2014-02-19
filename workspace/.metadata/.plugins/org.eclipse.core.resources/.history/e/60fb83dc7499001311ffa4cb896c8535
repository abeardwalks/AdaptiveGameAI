package players.MCTS;

import players.IntPair;
import interfaces.IntPairInterface;

public class Move {
	
	private char action;
	private char turn;
	private String state;
	private String stateAfterMove;
	
	private Integer placementIndex;
	private Integer removalIndex;
	private IntPairInterface movementIndexs;
	
	public Move(char action, char turn, String state){
		this.action = action;
		this.turn = turn;
		this.state = state;
	}
	
	public char getAction(){
		return action;
	}
	
	public char getTurn(){
		return turn;
	}
	
	public void setPlacementIndex(int placementIndex){
		this.placementIndex = placementIndex;
		executeMove();
	}
	
	public void setRemovalIndex(int removalIndex){
		this.removalIndex = removalIndex;
		executeMove();
	}
	
	public void setMovementIndexs(int from, int to){
		movementIndexs = new IntPair(from, to);
		executeMove();
	}
	
	public int getPlacementIndex(){
		return placementIndex;
	}
	
	public int getRemovalIndex(){
		return removalIndex;
	}
	
	public IntPairInterface getMovementIndexs(){
		return movementIndexs;
	}
	
	public String getState(){
		return state;
	}
	
	public String getStateAfterMove(){
		return stateAfterMove;
	}
	
	
	private void executeMove(){
		
		char[] stateArray = state.toCharArray();
		
		switch (action) {
		case 'P':
			stateArray[placementIndex] = turn;
			break;
		case 'R':
			stateArray[removalIndex] = 'N';
			break;
		case 'M':
			stateArray[movementIndexs.getFirstInt()] = 'N';
			stateArray[movementIndexs.getSecondInt()] = turn;
			break;
		default:
			break;
		}
		
		stateAfterMove = new String(stateArray);
	}

}
