package utility;

import interfaces.BoardFacadeInterface;

import java.util.ArrayList;
import java.util.List;
import model.Phase;
import move.AbstractMove;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;

public class BoardManagement {

	private BoardFacadeInterface board;
	private boolean moveAnywhere;
	
	public BoardManagement(BoardFacadeInterface board){
		this.board = board;
	}
	
	public int resultOfMove(AbstractMove move){
		char action = move.getAction();
		char turn = move.getPlayerColour();
		int result = -1;
		switch (action) {
		case 'P':
			result = placeToken(turn, ((PlacementMove) move).getPlacementIndex());
			break;
		case 'R':
			result = removeToken(turn, ((RemovalMove) move).getRemovalIndex());
			break;
		case 'M':
			result = moveToken(turn, ((MovementMove) move).getFrom(), ((MovementMove) move).getTo());
			break;
		default:
			break;
		}
		return result;
	}
	
	public AbstractMove resultOfMove1(AbstractMove move){
		char action = move.getAction();
		char turn = move.getPlayerColour();
		AbstractMove newMove = null;
		int playerOneToPlace = board.getPlayerOneToPlace();
		int playerTwoToPlace = board.getPlayerTwoToPlace();
		int playerOneRemaining = board.getPlayerOneRemaining();
		int playerTwoRemaining = board.getPlayerOneToPlace();
		int result = -1;
		Phase phase = board.getPhase();
		switch (action) {
		case 'P':
			result = placeToken(turn, ((PlacementMove) move).getPlacementIndex());
			if(turn == 'R' && result != -1){
				playerOneToPlace--;
			}else if(result != -1){
				playerTwoToPlace--;
			}
			break;
		case 'R':
			result = removeToken(turn, ((RemovalMove) move).getRemovalIndex());
			if(turn == 'R' && result != -1){
				playerTwoRemaining--;
			}else if(result != -1){
				playerOneRemaining--;
			}
			break;
		case 'M':
			result = moveToken(turn, ((MovementMove) move).getFrom(), ((MovementMove) move).getTo());
			break;
		default:
			break;
		}
		if(playerOneToPlace == 0 && playerTwoToPlace == 0){
			phase = Phase.TWO;
		}
		if(playerOneRemaining == 3 && playerTwoRemaining == 3){
			phase = Phase.THREE;
		}
		
		if(result == 0 && phase == Phase.ONE && ((turn == 'R' && board.getPlayerOneToPlace() > 0) || (turn == 'B' && board.getPlayerTwoToPlace() >0))){
			if(turn == 'R'){
				turn = 'B';
			}else{
				turn = 'R';
			}
			newMove = new PlacementMove(move.getStatePostAction(), turn, -1);
			
		}else if(result == 0){
			if(turn == 'R'){
				turn = 'B';
			}else{
				turn = 'R';
			}
			newMove = new MovementMove(move.getStatePostAction(), turn, -1, -1);
		}else if(result == 1){
			newMove = new RemovalMove(move.getStatePostAction(), turn, -1);
		}
		return newMove;
	}
	
	public AbstractMove nextMove(AbstractMove move, int result) {
		char turn = move.getPlayerColour();
		AbstractMove newMove = null;
		if(result == 0 && board.getPhase() == Phase.ONE){
			if(turn == 'R'){
				turn = 'B';
			}else{
				turn = 'R';
			}
			newMove = new PlacementMove(board.getState(), turn, -1);
			
		}else if(result == 0){
			if(turn == 'R'){
				turn = 'B';
			}else{
				turn = 'R';
			}
			newMove = new MovementMove(board.getState(), turn, -1, -1);
		}else if(result == 1){
			newMove = new RemovalMove(board.getState(), turn, -1);
		}
		return newMove;
	}
	
	private int placeToken(char tokenColour, int position) {
		Integer result = 0;  		//for MVC
		char[] stateArray = board.getState().toCharArray();
		
		if(stateArray[position] != 'N' || (tokenColour == 'R' && board.getPlayerOneToPlace() == 0) 
									   || (tokenColour == 'B' && board.getPlayerTwoToPlace() == 0)){	//checks to ensure a token can be placed here...
			result = -1; 					
			return result;
		}
		stateArray[position] = tokenColour;
		
		if(partOfMill(position, new String(stateArray))){			//check if a Mill Was Made.
			result = 1;			//for MVC
		}
		
		return result;
	}
	
	private int removeToken(char token, int position) {
		Integer result = 0;			//for MVC
		char candidate = board.getState().charAt(position);		
		//Checks for all illegal removal scenarios related to the passed in position. 
		if(candidate == 'N' || candidate == token || ((partOfMill(position, board.getState()) && !millFreeToken(token, position)))){
			result = -1; 		//for MVC
			return result;
		}
		
		return result;
	}
	
	private int moveToken(char token, int from, int to) {
		Integer result = 0;			//for MVC
		char[] stateArray = board.getState().toCharArray();
		
		//determine whether the player can move anywhere.
		if(token == 'R' && board.getPlayerOneRemaining() == 3){			
			moveAnywhere = true;
		}else if(token == 'B' && board.getPlayerTwoRemaining() == 3){
			moveAnywhere = true;
		}
		
		if((stateArray[from] == token) && validMove(from, to)){
			stateArray[from] = 'N';
			stateArray[to] = token;
		}else{
			result = -1; 		
			return result;
		}
		
		if(partOfMill(to, new String(stateArray))){
			result = 1;				
		}
		
		moveAnywhere = false;
		return result;
	}
	
	public boolean partOfMill(int position, String state) {
		char toMatch = state.charAt(position);

		switch(position){
			case 0:
				if((state.charAt(1) == toMatch && state.charAt(2) == toMatch)
						|| (state.charAt(9) == toMatch && state.charAt(21) == toMatch)){
							return true;
				}
							break;
					
			case 1:
				if((state.charAt(0) == toMatch && state.charAt(2) == toMatch)
						|| (state.charAt(4) == toMatch && state.charAt(7) == toMatch)){
							return true;
						}
							break;
			case 2:
				if((state.charAt(1) == toMatch && state.charAt(0) == toMatch)
						|| (state.charAt(14) == toMatch && state.charAt(23) == toMatch)){
							return true;
						}
							break;
			case 3:
				if((state.charAt(4) == toMatch && state.charAt(5) == toMatch)
						|| (state.charAt(10) == toMatch && state.charAt(18) == toMatch)){
							return true;
						}
							break;
			case 4:
				if((state.charAt(1) == toMatch && state.charAt(7) == toMatch)
						|| (state.charAt(3) == toMatch && state.charAt(5) == toMatch)){
							return true;
						}
							break;
			case 5:
				if((state.charAt(4) == toMatch && state.charAt(3) == toMatch)
						|| (state.charAt(13) == toMatch && state.charAt(20) == toMatch)){
							return true;
						}
							break;
			case 6:
				if((state.charAt(7) == toMatch && state.charAt(8) == toMatch)
						|| (state.charAt(11) == toMatch && state.charAt(15) == toMatch)){
							return true;
						}
							break;
			case 7:
				if((state.charAt(4) == toMatch && state.charAt(1) == toMatch)
						|| (state.charAt(6) == toMatch && state.charAt(8) == toMatch)){
							return true;
						}
							break;
			case 8:
				if((state.charAt(6) == toMatch && state.charAt(7) == toMatch)
						|| (state.charAt(12) == toMatch && state.charAt(17) == toMatch)){
							return true;
						}
							break;
			case 9:
				if((state.charAt(0) == toMatch && state.charAt(21) == toMatch)
						|| (state.charAt(10) == toMatch && state.charAt(11) == toMatch)){
							return true;
						}
							break;
			case 10:
				if((state.charAt(9) == toMatch && state.charAt(11) == toMatch)
						|| (state.charAt(3) == toMatch && state.charAt(18) == toMatch)){
							return true;
						}
							break;
			case 11:
				if((state.charAt(6) == toMatch && state.charAt(15) == toMatch)
						|| (state.charAt(9) == toMatch && state.charAt(10) == toMatch)){
							return true;
						}
							break;
			case 12:
				if((state.charAt(8) == toMatch && state.charAt(17) == toMatch)
						|| (state.charAt(13) == toMatch && state.charAt(14) == toMatch)){
							return true;
						}
							break;
			case 13:
				if((state.charAt(5) == toMatch && state.charAt(20) == toMatch)
						|| (state.charAt(12) == toMatch && state.charAt(14) == toMatch)){
							return true;
						}
							break;
			case 14:
				if((state.charAt(2) == toMatch && state.charAt(23) == toMatch)
						|| (state.charAt(12) == toMatch && state.charAt(13) == toMatch)){
							return true;
						}
							break;
			case 15:
				if((state.charAt(11) == toMatch && state.charAt(6) == toMatch)
						|| (state.charAt(16) == toMatch && state.charAt(17) == toMatch)){
							return true;
						}
							break;
			case 16:
				if((state.charAt(19) == toMatch && state.charAt(22) == toMatch)
						|| (state.charAt(15) == toMatch && state.charAt(17) == toMatch)){
							return true;
						}
							break;
			case 17:
				if((state.charAt(15) == toMatch && state.charAt(16) == toMatch)
						|| (state.charAt(8) == toMatch && state.charAt(12) == toMatch)){
							return true;
						}
							break;
			case 18:
				if((state.charAt(3) == toMatch && state.charAt(10) == toMatch)
						|| (state.charAt(19) == toMatch && state.charAt(20) == toMatch)){
							return true;
						}
							break;
			case 19:
				if((state.charAt(16) == toMatch && state.charAt(22) == toMatch)
						|| (state.charAt(18) == toMatch && state.charAt(20) == toMatch)){
							return true;
						}
							break;
			case 20:
				if((state.charAt(18) == toMatch && state.charAt(19) == toMatch)
						|| (state.charAt(5) == toMatch && state.charAt(13) == toMatch)){
							return true;
						}
							break;
			case 21:
				if((state.charAt(9) == toMatch && state.charAt(0) == toMatch)
						|| (state.charAt(22) == toMatch && state.charAt(23) == toMatch)){
							return true;
						}
							break;
			case 22:
				if((state.charAt(21) == toMatch && state.charAt(23) == toMatch)
						|| (state.charAt(19) == toMatch && state.charAt(16) == toMatch)){
							return true;
						}
							break;
			case 23:
				if((state.charAt(21) == toMatch && state.charAt(22) == toMatch)
						|| (state.charAt(2) == toMatch && state.charAt(14) == toMatch)){
							return true;
						}
							break;
		}
		return false;
	}
	
	private boolean validMove(int from, int to) {
		
		char[] stateArray = board.getState().toCharArray();
		
		if(stateArray[from] == 'N' || stateArray[to] != 'N'){
			return false;
		}
		
		if(moveAnywhere){
			return true;
		}

		switch(from){
		case 0:
			if((to != 1) && (to != 9)){
				return false;
			}
			break;
		case 1:
			if(to != 0 && to != 2 && to != 4){
				return false;
			}
			break;
		case 2:
			if(to != 1 && to != 14){
				return false;
			}
			break;
		case 3:
			if(to != 4 && to != 10){
				return false;
			}
			break;
		case 4:
			if(to != 1 && to != 3 && to != 5 && to != 7){
				return false;
			}
			break;
		case 5:
			if(to != 4 && to != 13){
				return false;
			}
			break;
		case 6:
			if(to != 7 && to != 11){
				return false;
			}
			break;
		case 7:
			if(to != 6 && to != 8 && to != 4){
				return false;
			}
			break;
		case 8:
			if(to != 7 && to != 12){
				return false;
			}
			break;
		case 9:
			if(to != 0 && to != 21 && to != 10){
				return false;
			}
			break;
		case 10:
			if(to != 3 && to != 9 && to != 11 && to != 18){
				return false;
			}
			break;
		case 11:
			if(to != 6 && to != 10 && to != 15){
				return false;
			}
			break;
		case 12:
			if(to != 8 && to != 17 && to != 13){
				return false;
			}
			break;
		case 13:
			if(to != 5 && to != 12 && to != 14 && to != 20){
				return false;
			}
			break;
		case 14:
			if(to != 13 && to != 2 && to != 23){
				return false;
			}
			break;
		case 15:
			if(to != 11 && to != 16){
				return false;
			}
			break;
		case 16:
			if(to != 15 && to != 17 && to != 19){
				return false;
			}
			break;
		case 17:
			if(to != 16 && to != 12){
				return false;
			}
			break;
		case 18:
			if(to != 10 && to != 19){
				return false;
			}
			break;
		case 19:
			if(to != 18 && to != 16 && to != 20 && to != 22){
				return false;
			}
			break;
		case 20:
			if(to != 19 && to != 13){
				return false;
			}
			break;
		case 21:
			if(to != 9 && to != 22){
				return false;
			}
			break;
		case 22:
			if(to != 21 && to != 19 && to != 23){
				return false;
			}
			break;
		case 23:
			if(to != 22 && to != 14){
				return false;
			}
			break;
		}
		return true;
	}
	
	private boolean millFreeToken(char token, int position) {
		char[] stateArray = board.getState().toCharArray();
		for (int i = 0; i < stateArray.length; i++) {
			if((stateArray[i] != token && stateArray[i] != 'N') && i != position){
				if(!partOfMill(i, board.getState())){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean playersCanMove() {
		if(board.getPlayerOneToPlace() > 0 && board.getPlayerTwoToPlace() > 0){
			return true;
		}
		int index = 0;
		int limit = 24;
		boolean playerOne = false;
		boolean playerTwo = false;

		while(index < limit){
			char token = board.getState().charAt(index);
			int i = 0; 
			while(i < limit){
				if(validMove(index, i)){
					if(token == 'R'){
						playerOne = true;
					}else if(token == 'B'){
						playerTwo = true;
					}
				}
				i++;
			}
			index++;
		}
		
		
		
		if((playerOne || board.getPlayerOneRemaining() == 3) && (playerTwo || board.getPlayerTwoRemaining() == 3)){
			board.setTrappedPlayer('N');
			return true;
		}else if(!playerOne && board.getPlayerOneRemaining() > 3){			//if player one cannot move...
			board.setTrappedPlayer('R');		//...set it as the trapped player. 
		}else if(!playerTwo && board.getPlayerTwoRemaining() > 3){			//if player two cannot move...
			board.setTrappedPlayer('B');		//...set it as the trapped player. 
		}
		return false;
	}
	
	

	public Phase calculatePhase() {

		if(!playersCanMove() && (board.getPlayerOneRemaining() > 3 && board.getPlayerTwoRemaining() > 3)){
			return Phase.FOUR;
		}else if(board.getPlayerOneToPlace() == 0 && board.getPlayerTwoToPlace() == 0){
			return Phase.TWO;
		}else if(board.getPlayerOneRemaining() <= 3 && board.getPlayerTwoRemaining() <= 3){
			return Phase.THREE;
		}
		return Phase.ONE;
	}
	
	/**
	 * Gets all possible placements for the passed in token colour. 
	 * 
	 * @param tokenColour - The colour that is to be placed.
	 * @return		      - The list of placement moves available. 
	 */
	public List<AbstractMove> getAllPossiblePlacements(char tokenColour) {
		char[] stateArray = board.getState().toCharArray();
		List<AbstractMove> moves = new ArrayList<AbstractMove>();
		if((tokenColour == 'R' && board.getPlayerOneToPlace() > 0) 
							   || (tokenColour == 'B' && board.getPlayerTwoToPlace() > 0)){
			for (int i = 0; i < stateArray.length; i++) {
				if(stateArray[i] == 'N'){
					moves.add(new PlacementMove(board.getState(), tokenColour, i));
				}
			}
		}
		return moves;
	}
	
	/**
	 * Gets all possible removals for the passed in token colour.
	 * 
	 * @param tokenColour - The colour of the player carrying out the removals.
	 * @return			  - The list of removals available. 
	 */
	public List<AbstractMove> getAllPossibleRemovals(char tokenColour) {
		char[] stateArray = board.getState().toCharArray();
		List<AbstractMove> moves = new ArrayList<AbstractMove>();
		if((tokenColour == 'R' && board.getPlayerTwoRemaining() > 2) 
				|| (tokenColour == 'B' && board.getPlayerOneRemaining() > 2)){
			for (int i = 0; i < stateArray.length; i++) {
				if(stateArray[i] != 'N' && stateArray[i] != tokenColour && 
					  (!partOfMill(i, board.getState()) || millFreeToken(tokenColour, i))){
					moves.add(new RemovalMove(board.getState(), tokenColour, i));
				}
			}
		}
		return moves;
	}
	
	/**
	 * Gets all possible movements for the passed in token colour. 
	 * 
	 * @param tokenColour - the colour to check for movements. 
	 * @return			  - The list of all movements available. 
	 */
	public List<AbstractMove> getAllPossibleMovements(char tokenColour) {
		char[] stateArray = board.getState().toCharArray();
		List<AbstractMove> moves = new ArrayList<AbstractMove>();
		for (int i = 0; i < stateArray.length; i++) {
			if(stateArray[i] == tokenColour){
				for (int j = 0; j < stateArray.length; j++) {
					if(validMove(i, j)){
						moves.add(new MovementMove(board.getState(), tokenColour, i, j));
					}
				}
			}
		}
		return moves;
	}


	
}
