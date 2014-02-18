package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import players.MCTS.Move;

import model.Phase;
import model.board.BoardDetails;

public class MoveChecker {
	
	private String state;
	private Stack<BoardDetails> history;
	@SuppressWarnings("unused")
	private static final int STRING_LENGTH = 23;
	private Phase gamePhase;
	private int playerOneTokensToPlace, playerTwoTokensToPlace, playerOneTokensRemaining, playerTwoTokensRemaining;
	private boolean moveAnywhere;
	private BoardDetails preservedGame;
	private char turn;
	
	public MoveChecker(){
	
		state = "NNNNNNNNNNNNNNNNNNNNNNNN";
		history = new Stack<BoardDetails>();
		
		gamePhase = Phase.ONE;
		
		playerOneTokensToPlace = 9;
		playerTwoTokensToPlace = 9;
		playerOneTokensRemaining = 9;
		playerTwoTokensRemaining = 9;
		
		moveAnywhere = false;
		
		preservedGame = null;

	}


	public int addToken(char token, int position) {
		Integer result = 0;  		//for MVC
		char[] stateArray = state.toCharArray();
		
		if(stateArray[position] != 'N'){
			result = -1; 		//for MVC
			return result;
		}
		if(token == 'R'){
			playerOneTokensToPlace--;
		}else{
			playerTwoTokensToPlace--;
		}
		
		if(playerOneTokensToPlace == 0 && playerTwoTokensToPlace == 0){
			gamePhase = Phase.TWO;
		}
		
		
		stateArray[position] = token;
		state = new String(stateArray);
		
		if(partOfMill(position)){
			result = 1;			//for MVC
		}else if(gameWon()){
			result = 2;			//for MVC
		}	
		if(result == 0){
			if(turn == 'R'){
				turn = 'B';
			}else{
				turn = 'R';
			}
		}
		
		return result;
	}
	
	public int removeToken(char token, int position) {
		Integer result = 0;			//for MVC
		char candidate = state.charAt(position);
		char[] stateArray = state.toCharArray();
		
		if(candidate == 'N' || candidate == token || ((partOfMill(position) && !onlyAvailable(token, position)))){
			result = -1; 		//for MVC
			return result = -1;
		}else{
			if(token == 'R'){
				playerTwoTokensRemaining--;
			}else{
				playerOneTokensRemaining--;
			}
			
			if(playerTwoTokensRemaining == 3 || playerOneTokensRemaining == 3){
				gamePhase = Phase.THREE;
			}
			stateArray[position] = 'N';
			state = new String(stateArray);
		}
		
		if(gameWon()){
			result = 2;				//for MVC
		}
		if(result == 0){
			if(turn == 'R'){
				turn = 'B';
			}else{
				turn = 'R';
			}
		}

		return result;
	}

	private boolean onlyAvailable(char token, int position) {
		
		char[] stateArray = state.toCharArray();
		for (int i = 0; i < stateArray.length; i++) {
			if((stateArray[i] != token && stateArray[i] != 'N') && i != position){
				if(!partOfMill(i)){
					return false;
				}
			}
		}
		return true;
	}


	public int moveToken(char token, int x, int y) {
		Integer result = 0;			//for MVC
		char[] stateArray = state.toCharArray();
		
		if(token == 'R' && playerOneTokensRemaining == 3){
			moveAnywhere = true;
		}else if(token == 'B' && playerTwoTokensRemaining == 3){
			moveAnywhere = true;
		}
		
		if((stateArray[x] == token) && validMove(x, y)){
			stateArray[x] = 'N';
			stateArray[y] = token;
			state = new String(stateArray);
		}else{
			result = -1; 		
			return result;
		}
		
		if(partOfMill(y)){
			result = 1;				
		}
		
		if(gameWon()){
			result = 2;				
		}
		if(result == 0){
			if(turn == 'R'){
				turn = 'B';
			}else{
				turn = 'R';
			}
		}
		moveAnywhere = false;
		return result;
	}
	
	public boolean gameWon() {
		if((playerOneTokensRemaining >= 3 && playerTwoTokensRemaining >= 3) && playersCanMove()){
			return false;
		}
		return true;
	}

	public boolean playersCanMove() {
		if(gamePhase == Phase.ONE){
			return true;
		}
		int index = 0;
		int limit = state.length();
		boolean playerOne = false;
		boolean playerTwo = false;
		
		while(index < limit && (playerOne == false || playerTwo == false)){
			char token = state.charAt(index);
			int i = 0; 
			while(i < limit){
				if(validMove(index, i)){
					if(token == 'R'){
						playerOne = true;
					}else{
						playerTwo = true;
					}
				}
				i++;
			}
			index++;
		}
		
		if(playerOne && playerTwo){
			return true;
		}
		return false;
	}

	private boolean partOfMill(int position) {
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



	private boolean validMove(int x, int y) {
		
		char[] stateArray = state.toCharArray();
		
		
		if(stateArray[y] != 'N'){
			return false;
		}
		
		if(gamePhase == Phase.THREE && moveAnywhere){
			return true;
		}

		switch(x){
		case 0:
			if((y != 1) && (y != 9)){
				return false;
			}
			break;
		case 1:
			if(y != 0 && y != 2 && y != 4){
				return false;
			}
			break;
		case 2:
			if(y != 1 && y != 14){
				return false;
			}
			break;
		case 3:
			if(y != 4 && y != 10){
				return false;
			}
			break;
		case 4:
			if(y != 1 && y != 3 && y != 5 && y != 7){
				return false;
			}
			break;
		case 5:
			if(y != 4 && y != 13){
				return false;
			}
			break;
		case 6:
			if(y != 7 && y != 11){
				return false;
			}
			break;
		case 7:
			if(y != 6 && y != 8 && y != 4){
				return false;
			}
			break;
		case 8:
			if(y != 7 && y != 12){
				return false;
			}
			break;
		case 9:
			if(y != 0 && y != 21 && y != 10){
				return false;
			}
			break;
		case 10:
			if(y != 3 && y != 9 && y != 11 && y != 18){
				return false;
			}
			break;
		case 11:
			if(y != 6 && y != 10 && y != 15){
				return false;
			}
			break;
		case 12:
			if(y != 8 && y != 17 && y != 13){
				return false;
			}
			break;
		case 13:
			if(y != 5 && y != 12 && y != 14 && y != 20){
				return false;
			}
			break;
		case 14:
			if(y != 13 && y != 2 && y != 23){
				return false;
			}
			break;
		case 15:
			if(y != 11 && y != 16){
				return false;
			}
			break;
		case 16:
			if(y != 15 && y != 17 && y != 19){
				return false;
			}
			break;
		case 17:
			if(y != 16 && y != 12){
				return false;
			}
			break;
		case 18:
			if(y != 10 && y != 19){
				return false;
			}
			break;
		case 19:
			if(y != 18 && y != 16 && y != 20 && y != 22){
				return false;
			}
			break;
		case 20:
			if(y != 19 && y != 13){
				return false;
			}
			break;
		case 21:
			if(y != 9 && y != 22){
				return false;
			}
			break;
		case 22:
			if(y != 21 && y != 19 && y != 23){
				return false;
			}
			break;
		case 23:
			if(y != 22 && y != 14){
				return false;
			}
			break;
		}
		return true;
	}

	public void setState(String state){
		this.state = state;
	}
	
	public Phase getPhase(){
		return gamePhase;
	}
	
	public String getState(){
		return state;
	}


	public void reset() {
		state = "NNNNNNNNNNNNNNNNNNNNNNNN";
		history = new Stack<BoardDetails>();
		
		gamePhase = Phase.ONE;
		
		playerOneTokensToPlace = 9;
		playerTwoTokensToPlace = 9;
		playerOneTokensRemaining = 9;
		playerTwoTokensRemaining = 9;
		
		moveAnywhere = false;
	}
	
	public void preserveCurrentState(){
		BoardDetails bd = new BoardDetails(state, -2, playerOneTokensRemaining, playerTwoTokensRemaining, playerOneTokensToPlace, playerTwoTokensToPlace, turn, gamePhase);
		history.push(bd);
	}
	
	public void returnToPreservedState(){
		BoardDetails bd = history.pop();
		if(bd != null){
			state = bd.getGS();
			playerOneTokensRemaining = bd.getPlayerOneRemaining();
			playerTwoTokensRemaining = bd.getPlayerTwoRemaining();
			playerOneTokensToPlace = bd.getPlayerOneToPlace();
			playerTwoTokensToPlace = bd.getPlayerTwoToPlace();
			gamePhase = bd.getPhase();
		}
	}
	
	public void setDetails(BoardDetails bd){
		state = bd.getGS();
		playerOneTokensRemaining = bd.getPlayerOneRemaining();
		playerTwoTokensRemaining = bd.getPlayerTwoRemaining();
		playerOneTokensToPlace = bd.getPlayerOneToPlace();
		playerTwoTokensToPlace = bd.getPlayerTwoToPlace();
		gamePhase = bd.getPhase();
		turn = bd.getTurn();
	}


	public List<Move> getAvailableMoves(char action, char turn) {
		List<Move> moves = new ArrayList<Move>();
		switch (action) {
		case 'P':
			moves = getAllAvailablePlacements(turn);
			break;
		case 'M': 
			moves = getAllAvailableMovements(turn);
			break;
		case 'R':
			moves = getAllAvailableRemovals(turn);
			break;
		default:
			break;
		}
		return moves;
	}


	private List<Move> getAllAvailablePlacements(char turn) {
		List<Move> moves = new ArrayList<Move>();
		
		char[] stateArray = state.toCharArray();
		if(turn == 'R' && playerOneTokensToPlace <= 0){
			return moves;
		}
		
		if(turn == 'B' && playerTwoTokensToPlace <= 0){
			return moves;
		}
		
		for (int i = 0; i < stateArray.length; i++) {
			if(stateArray[i] == 'N'){
				Move m = new Move(state, 'P', turn);
				m.setPlacementIndex(i);
				moves.add(m);
			}
		}
		
		return moves;
	}
	
	
	private List<Move> getAllAvailableMovements(char turn) {
		List<Move> moves = new ArrayList<Move>();
		
		char[] stateArray = state.toCharArray();
		
		for (int i = 0; i < stateArray.length; i++) {
			if(stateArray[i] == turn){
				for (int j = 0; j < stateArray.length; j++) {
					if(validMove(i, j)){
						Move m = new Move(state, 'M', turn);
						m.setMoveIndexs(i, j);
						moves.add(m);
					}
				}
			}
		}
		
		return moves;
	}
	
	
	private List<Move> getAllAvailableRemovals(char turn) {
		List<Move> moves = new ArrayList<Move>();
		
		char[] stateArray = state.toCharArray();
		
		for (int i = 0; i < stateArray.length; i++) {
			if(stateArray[i] != turn && stateArray[i] != 'N' && !partOfMill(i)){
				Move m = new Move(state, 'R', turn);
				m.setRemovalIndex(i);
				moves.add(m);
			}else if(stateArray[i] != turn && stateArray[i] != 'N' && partOfMill(i) && onlyAvailable(stateArray[i], i)){
				Move m = new Move(state, 'R', turn);
				m.setRemovalIndex(i);
				moves.add(m);
			}
		}
		
		return moves;
	}


	public double[] getRewards() {
		double[] rewards = new double[2];
		if(playerOneTokensRemaining == 2){
			rewards[0] = 1.0;
			rewards[1] = 0.0;
		}else if(playerTwoTokensRemaining == 2){
			rewards[0] = 0.0;
			rewards[1] = 1.0;
		}
		return rewards;
	}
	
	public char getTurn(){
		return turn;
	}
	
	public int getPlayerID(){
		if(turn == 'R'){
			return 1;
		}else{
			return 2;
		}
	}
	
	public void printDetails(){
		System.out.println("----- Move Check Details ------");
		System.out.println("Player One To Place: " + playerOneTokensToPlace);
		System.out.println("Player Two To Place: " + playerTwoTokensToPlace);
		System.out.println("Player One Remaining: " + playerOneTokensRemaining);
		System.out.println("Player Two Remaining: " + playerTwoTokensRemaining);
		System.out.println("Phase: " + gamePhase);
		System.out.println("-------------------------------");
	}
	
	public int getPlayerOneTokensToPlace(){
		return playerOneTokensToPlace;
	}
	
	public int getPlayerTwoTokensToPlace(){
		return playerTwoTokensRemaining;
	}
	
	public void setTurn(char turn){
		this.turn = turn;
	}

}
