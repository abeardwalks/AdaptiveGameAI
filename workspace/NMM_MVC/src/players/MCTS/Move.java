package players.MCTS;

import players.IntPair;
import interfaces.IntPairInterface;

public class Move {
	
	private String gameStateBeforeAction;
	private String gameStateAfterAction;
	private char action;
	private char player;
	private int placementIndex;
	private int removalIndex;
	private IntPairInterface movementIndexs;
	private int result;

	public Move(String stateBefore, char action, char player){
		gameStateBeforeAction = stateBefore;
		this.action = action;
		this.player = player;
	}
	
	public void setPlacementIndex(int placement){
		placementIndex = placement;
		if(action == 'P'){
			char[] stateString = gameStateBeforeAction.toCharArray();
			stateString[placementIndex] = player;
			gameStateAfterAction = stateString.toString();
		}
	}
	
	public void setRemovalIndex(int removal){
		removalIndex = removal;
		if(action == 'R'){
			char[] stateString = gameStateBeforeAction.toCharArray();
			stateString[removalIndex] = 'N';
			gameStateAfterAction = stateString.toString();
		}
	}
	
	public void setMoveIndexs(int from, int to){
		movementIndexs = new IntPair(from, to);
		if(action == 'M'){
			char[] stateString = gameStateBeforeAction.toCharArray();
			stateString[movementIndexs.getFirstInt()] = 'N';
			stateString[movementIndexs.getSecondInt()] = player;
			gameStateAfterAction = stateString.toString();
		}
	}
	
	public void setResult(int result){
		this.result = result;
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
	
	public String getGameStateBeforeAction(){
		return gameStateBeforeAction;
	}
	
	public String getGameStateAfterAction(){
		return gameStateAfterAction;
	}
	
	public char getAction(){
		return action;
	}
	
	public char getPlayer(){
		return player;
	}
	
	public int getResult(){
		return result;
	}
}