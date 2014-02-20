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
	
	public Move(char action, char turn, String state, String stateAfterMove, Integer placementIndex, Integer removalIndex, IntPairInterface movementIndexs){
		this.action = action;
		this.turn = turn;
		this.state = state;
		this.stateAfterMove = stateAfterMove;
		this.placementIndex = placementIndex;
		this.removalIndex = removalIndex;
		this.movementIndexs = movementIndexs;
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
	
	public Object clone(){
		if(placementIndex == null && removalIndex == null && movementIndexs == null){
			return new Move(action, turn, state, stateAfterMove, null, null, null);
		}else if(placementIndex == null && removalIndex == null && movementIndexs != null){
			return new Move(action, turn, state, stateAfterMove, null, null, new IntPair(movementIndexs.getFirstInt(), movementIndexs.getSecondInt()));
		}else if(placementIndex == null && removalIndex != null && movementIndexs == null){
			return new Move(action, turn, state, stateAfterMove, null, removalIndex.intValue(), null);
		}else if(placementIndex != null && removalIndex == null && movementIndexs == null){
			return new Move(action, turn, state, stateAfterMove, placementIndex.intValue(), null, null);
		}
		
		return new Move(action, turn, state, stateAfterMove, placementIndex.intValue(), removalIndex.intValue(), new IntPair(movementIndexs.getFirstInt(), movementIndexs.getSecondInt()));
	}

}
