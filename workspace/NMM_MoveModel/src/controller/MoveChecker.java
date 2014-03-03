package controller;

import interfaces.BoardDetailsInterface;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Stack;


import model.Phase;
import move.AbstractMove;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;

public class MoveChecker {
	private String state;
//	private Collection<String> history;
	private Stack<AbstractMove> moveHistory;
	@SuppressWarnings("unused")
	private static final int STRING_LENGTH = 23;
	private Phase gamePhase;
	private int playerOneTokensToPlace, playerTwoTokensToPlace, playerOneTokensRemaining, playerTwoTokensRemaining;
	private boolean moveAnywhere;
	private char trappedPlayer;

	
	public MoveChecker(){
	
		state = "NNNNNNNNNNNNNNNNNNNNNNNN";
		
		gamePhase = Phase.ONE;
		
		playerOneTokensToPlace = 9;
		playerTwoTokensToPlace = 9;
		playerOneTokensRemaining = 9;
		playerTwoTokensRemaining = 9;
		
		moveAnywhere = false;

	}
	
	public MoveChecker(BoardDetailsInterface game){
		state = game.getState();
		moveHistory = new Stack<AbstractMove>();
		
		playerOneTokensToPlace = game.getPlayerOneToPlace();
		playerTwoTokensToPlace = game.getPlayerTwoToPlace();
		playerOneTokensRemaining = game.getPlayerOneRemaining();
		playerTwoTokensRemaining = game.getPlayerTwoRemaining();
		gamePhase = game.getPhase();
		
		moveAnywhere = false;
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
		
		return result;
	}
	
	public int removeToken(char token, int position) {
		Integer result = 0;			//for MVC
		char candidate = state.charAt(position);
		char[] stateArray = state.toCharArray();
		
		if(candidate == 'N' || candidate == token || ((partOfMill(position) && !onlyAvailable(token, position)))){
			result = -1; 		//for MVC
			return result;
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
			System.err.println("Game Won");
			result = 2;				//for MVC
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
		
		while(index < limit || (playerOne == false && playerTwo == false)){
			char token = state.charAt(index);
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
		
		if(playerOne && playerTwo){
			return true;
		}else if(!playerOne){
			trappedPlayer = 'R';
		}else if(!playerTwo){
			trappedPlayer = 'B';
		}
		return false;
	}
	
	public char trappedPlayer(){
		return trappedPlayer;
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
		
		
		if(stateArray[x] == 'N' || stateArray[y] != 'N'){
			return false;
		}
		
		if(moveAnywhere){
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
		calculatePhase();
		return gamePhase;
	}
	
	private void calculatePhase(){
		
		gamePhase = Phase.ONE;
		
		if(playerOneTokensToPlace == 0 && playerTwoTokensToPlace == 0){
			gamePhase = Phase.TWO;
		}
		
		if(playerOneTokensRemaining <= 3 && playerTwoTokensRemaining <= 3){
			gamePhase = Phase.THREE;
		}
		
		if(!playersCanMove()){
			gamePhase = Phase.FOUR;
		}
		
		
	}
	
	public String getState(){
		return state;
	}


	public void reset() {
		state = "NNNNNNNNNNNNNNNNNNNNNNNN";

		
		gamePhase = Phase.ONE;
		
		playerOneTokensToPlace = 9;
		playerTwoTokensToPlace = 9;
		playerOneTokensRemaining = 9;
		playerTwoTokensRemaining = 9;
		
		moveAnywhere = false;
	}
	

	
	
//	public List<Move> getAllPossibleMoves(char action, char turn, BoardDetails bd){
//		setDetails(bd);
//		List<Move> moves = new ArrayList<Move>();
//		
//		switch (action) {
//		case 'P':
//			moves = getAllPossiblePlacements(turn);
//			break;
//		case 'R':
//			moves = getAllPossibleRemovals(turn);
//			break;
//		case 'M':
//			moves = getAllPossibleMovements(turn);
//			break;
//		default:
//			break;
//		}
//		
//		return moves;
//	}


//	private List<Move> getAllPossiblePlacements(char turn) {
//		
//		
//		List<Move> moves = new ArrayList<Move>();
//		System.out.println("calculating placements...");
//		System.out.println("Turn is: " + turn);
//		System.out.println("P1 TP: " + playerOneTokensToPlace);
//		System.out.println("P2 TP: " + playerTwoTokensToPlace);
//		if(turn == 'R' && playerOneTokensToPlace <= 0){
//			return moves;
//		}else if(turn == 'B' && playerTwoTokensToPlace <= 0){
//			return moves;
//		}
//		
//		char[] stateArray = state.toCharArray();
//		
//		for (int i = 0; i < stateArray.length; i++) {
//			if(stateArray[i] == 'N'){
//				Move m = new Move('P', turn, state);
//				m.setPlacementIndex(i);
//				moves.add(m);
//			}
//		}
//		
//		return moves;
//		
//	}
//	
//	
//	private List<Move> getAllPossibleRemovals(char turn) {
//		List<Move> moves = new ArrayList<Move>();
//		
//		char toRemove = 'R';
//		if(turn == 'R'){
//			toRemove = 'B';
//		}
//		
//		char[] stateArray = state.toCharArray();
//		
//		for (int i = 0; i < stateArray.length; i++) {
//			if(stateArray[i] == toRemove){
//				Move m = new Move('R', turn, state);
//				m.setRemovalIndex(i);
//				moves.add(m);
//			}
//		}
//		
//		return moves;
//	}
//	
//	public void setTokensToPlace(int p1, int p2){
//		playerOneTokensToPlace = p1;
//		playerTwoTokensToPlace = p2;
//	}
//
//	private List<Move> getAllPossibleMovements(char turn) {
//		List<Move> moves = new ArrayList<Move>();
//		
//		if(turn == 'R' && playerOneTokensRemaining < 4){
//			moveAnywhere = true;
//		}else if(turn == 'B' && playerTwoTokensRemaining < 4){
//			moveAnywhere = true;
//		}
//		
//		char[] stateArray = state.toCharArray();
//		
//		for(int i = 0; i < stateArray.length; i++){
//			if(stateArray[i] == turn){
//				for(int j = 0; j < stateArray.length; j++){
//					if(validMove(i, j)){
//						Move m = new Move('M', turn, state);
//						m.setMovementIndexs(i, j);
//						moves.add(m);
//					}
//				}
//			}
//		}
//		
//		return moves;
//		
//	}
	
	public void printDetails(){
		System.out.println("-------------- Move Checker Details --------------");
		System.out.println("State: " + state);
		System.out.println("P1 TP: " + playerOneTokensToPlace);
		System.out.println("P2 TP: " + playerTwoTokensToPlace);
		System.out.println("P1 TR: " + playerOneTokensRemaining);
		System.out.println("P2 TR: " + playerTwoTokensRemaining);
		calculatePhase();
		System.out.println("Phase: " + gamePhase);
		System.out.println("--------------------------------------------------");
	}
	
	private char newAction;
	private char newPlayerTurn;
	
	public AbstractMove executeMove(AbstractMove move){
		char action = move.getAction();
		char turn = move.getPlayerColour();
		AbstractMove newMove = null;
		int result = -1;
		switch (action) {
		case 'P':
			result = addToken(turn, ((PlacementMove) move).getPlacementIndex());
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
		if(result == 0 && gamePhase == Phase.ONE && ((turn == 'R' && playerTwoTokensToPlace > 0) || (turn == 'B' && playerOneTokensToPlace >0))){
			newAction = 'P';
			if(turn == 'R'){
				turn = 'B';
				newPlayerTurn = 'B';
			}else{
				turn = 'R';
				newPlayerTurn = 'R';
			}
			newMove = new PlacementMove(state, turn, -1);
			
		}else if(result == 0){
			newAction = 'M';
			if(turn == 'R'){
				turn = 'B';
				newPlayerTurn = 'B';
			}else{
				turn = 'R';
				newPlayerTurn = 'R';
			}
			newMove = new MovementMove(state, turn, -1, -1);
		}else if(result == 1){
			newAction = 'R';
			newPlayerTurn = turn;
			newMove = new RemovalMove(state, turn, -1);
		}
		return newMove;
	}
	
	public char getNewAction(){
		return newAction;
	}
	
	public char getNewPlayerTurn(){
		return newPlayerTurn;
	}

	public List<AbstractMove> getAllPossibleMoves(char action,
			char tokenColour) {
		
		switch (action) {
		case 'P':
			return getAllPossiblePlacements(tokenColour);
		case 'R':
			return getAllPossibleRemovals(tokenColour);
		case 'M':
			return getAllPossibleMovements(tokenColour);
		default:
			break;
		}
		
		return null;
	}

	private List<AbstractMove> getAllPossiblePlacements(char tokenColour) {
		char[] stateArray = state.toCharArray();
		List<AbstractMove> moves = new ArrayList<AbstractMove>();
		if((tokenColour == 'R' && playerOneTokensToPlace > 0) || (tokenColour == 'B' && playerTwoTokensToPlace > 0)){
			for (int i = 0; i < stateArray.length; i++) {
				if(stateArray[i] == 'N'){
					moves.add(new PlacementMove(state, tokenColour, i));
				}
			}
		}
		return moves;
	}
	
	private List<AbstractMove> getAllPossibleRemovals(char tokenColour) {
		char[] stateArray = state.toCharArray();
		List<AbstractMove> moves = new ArrayList<AbstractMove>();
		if((tokenColour == 'R' && playerTwoTokensRemaining > 2) || (tokenColour == 'B' && playerOneTokensRemaining > 2)){
			for (int i = 0; i < stateArray.length; i++) {
				if(stateArray[i] != 'N' && stateArray[i] != tokenColour && (!partOfMill(i) || onlyAvailable(tokenColour, i)) ){
					moves.add(new RemovalMove(state, tokenColour, i));
				}
			}
		}
		
		return moves;
	}
	
	private List<AbstractMove> getAllPossibleMovements(char tokenColour) {
		char[] stateArray = state.toCharArray();
		List<AbstractMove> moves = new ArrayList<AbstractMove>();
		for (int i = 0; i < stateArray.length; i++) {
			if(stateArray[i] == tokenColour){
				for (int j = 0; j < stateArray.length; j++) {
					if(validMove(i, j)){
						moves.add(new MovementMove(state, tokenColour, i, j));
					}
				}
			}
		}
		return moves;
	}


}
