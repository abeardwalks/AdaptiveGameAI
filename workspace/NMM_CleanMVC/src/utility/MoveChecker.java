package utility;

import interfaces.BoardViewInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import model.Phase;
import move.AbstractMove;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;

/**
 * MoveChecker is a utility class used for checking the legality of moves before the model is 
 * updated. It provides methods for checking if mills have been made, whether players are trapped,
 * if placements/movements/removals are valid etc. 
 * 
 * @author Andrew White - BSc Software Engineering, 200939787.
 *
 */
public class MoveChecker {
	
	private String state;
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
		
		moveHistory = new Stack<AbstractMove>();
		
		moveAnywhere = false;

	}
	
	/**
	 * Constructs a new Move Checker, setting its values to that of a passed in game. 
	 * 
	 * @param game - the model to base the move checker details. 
	 */
	public MoveChecker(BoardViewInterface game){
		state = game.getState();
		moveHistory = new Stack<AbstractMove>();
		
		playerOneTokensToPlace = game.getPlayerOneToPlace();
		playerTwoTokensToPlace = game.getPlayerTwoToPlace();
		playerOneTokensRemaining = game.getPlayerOneRemaining();
		playerTwoTokensRemaining = game.getPlayerTwoRemaining();
		gamePhase = game.getPhase();
		
		moveAnywhere = false;
	}

	/* (non-Javadoc)
	 * @see utility.MoveUtilityInterface#placeToken(char, int)
	 */
	public int placeToken(char tokenColour, int position) {
		Integer result = 0;  		//for MVC
		char[] stateArray = state.toCharArray();
		
		if(stateArray[position] != 'N'){	//checks to ensure a token can be placed here...
			result = -1; 					//...if not return -1.
			return result;
		}
		
		if(tokenColour == 'R'){				//if the token colour is R (Red)...
			playerOneTokensToPlace--;		//decrement player ones tokens to place.
		}else{
			playerTwoTokensToPlace--;		//otherwise decrement player twos tokens to place.
		}
		
		if(playerOneTokensToPlace == 0 && playerTwoTokensToPlace == 0){		//if both players have no tokens remaining to place change phase.	
			gamePhase = Phase.TWO;
		}
		
		stateArray[position] = tokenColour;
		state = new String(stateArray);
		
		if(partOfMill(position)){			//check if a Mill Was Made.
			result = 1;			//for MVC
		}else if(gameWon()){				//check if the game was won.
			result = 2;			//for MVC
		}	
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see utility.MoveUtilityInterface#removeToken(char, int)
	 */
	public int removeToken(char token, int position) {
		Integer result = 0;			//for MVC
		char candidate = state.charAt(position);
		char[] stateArray = state.toCharArray();
		
		//Checks for all illegal removal scenarios related to the passed in position. 
		if(candidate == 'N' || candidate == token || ((partOfMill(position) && !millFreeToken(token, position)))){
			result = -1; 		//for MVC
			return result;
		}else{									//otherwise...
			if(token == 'R'){					//if the token was R (Red)
				playerTwoTokensRemaining--;		//reduce player Twos remaining tokens.
			}else{
				playerOneTokensRemaining--;		//otherwise reduce player Ones remaining tokens.
			}
			
			//Check whether the chase phase has stated (Phase Three)
			if(playerTwoTokensRemaining == 3 || playerOneTokensRemaining == 3){
				gamePhase = Phase.THREE;
			}
			stateArray[position] = 'N';
			state = new String(stateArray);
		}
		
		if(gameWon()){
			result = 2;				//for MVC
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see utility.MoveUtilityInterface#moveToken(char, int, int)
	 */
	public int moveToken(char token, int from, int to) {
		Integer result = 0;			//for MVC
		char[] stateArray = state.toCharArray();
		
		//determine whether the player can move anywhere.
		if(token == 'R' && playerOneTokensRemaining == 3){			
			moveAnywhere = true;
		}else if(token == 'B' && playerTwoTokensRemaining == 3){
			moveAnywhere = true;
		}
		
		if((stateArray[from] == token) && validMove(from, to)){
			stateArray[from] = 'N';
			stateArray[to] = token;
			state = new String(stateArray);
		}else{
			result = -1; 		
			return result;
		}
		
		if(partOfMill(to)){
			result = 1;				
		}
		
		if(gameWon()){
			result = 2;				
		}
		moveAnywhere = false;
		return result;
	}
	
	/**
	 * When removing tokens, players must select tokens that are NOT part of a mill
	 * first over those that are if any exist. millFreeToken checks whether or not
	 * there are any tokens on the board free of mills. 
	 * 
	 * @param    token - The colour of the player attempting to remove a token.
	 * @param position - The position of the removal. 
	 * @return - True if there are mill free tokens, false otherwise.
	 */
	private boolean millFreeToken(char token, int position) {
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
	
	public boolean gameWon() {
		if((playerOneTokensRemaining >= 3 && playerTwoTokensRemaining >= 3) && playersCanMove()){
			return false;
		}
		return true;
	}

	/**
	 * If one of the players is unable to move i.e. they are trapped, the game has been won 
	 * by the untrapped player. 
	 * 
	 * @return - true if players are able to move, false otherwise.
	 */
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
		}else if(!playerOne){			//if player one cannot move...
			trappedPlayer = 'R';		//...set it as the trapped player. 
		}else if(!playerTwo){			//if player two cannot move...
			trappedPlayer = 'B';		//...set it as the trapped player. 
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see utility.MoveUtilityInterface#trappedPlayer()
	 */
	public char trappedPlayer(){
		return trappedPlayer;
	}

	/* (non-Javadoc)
	 * @see utility.MoveUtilityInterface#partOfMill(int)
	 */
	public boolean partOfMill(int position) {
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

	/**
	 * Method used by the movement method to check whether or not it is valid. 
	 * 
	 * @param from - the position to move from.
	 * @param   to - the position to move to.
	 * @return     - True if valid, false otherwise.
	 */
	private boolean validMove(int from, int to) {
		
		char[] stateArray = state.toCharArray();
		
		
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

	public void setState(String state){
		this.state = state;
	}
	
	public Phase getPhase(){
		calculatePhase();
		return gamePhase;
	}
	
	/**
	 * Calculates the phase to ensure it is valid.
	 */
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
	
	/* (non-Javadoc)
	 * @see utility.MoveUtilityInterface#executeMove(move.AbstractMove)
	 */
	public AbstractMove executeMove(AbstractMove move){
		moveHistory.push(move);
		char action = move.getAction();
		char turn = move.getPlayerColour();
		AbstractMove newMove = null;
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
	
	/* (non-Javadoc)
	 * @see utility.MoveUtilityInterface#getNewAction()
	 */
	public char getNewAction(){
		return newAction;
	}
	
	/* (non-Javadoc)
	 * @see utility.MoveUtilityInterface#getNewPlayerTurn()
	 */
	public char getNewPlayerTurn(){
		return newPlayerTurn;
	}
	
	public void undo(){
		AbstractMove move = moveHistory.pop();		//pop the last executed move from the history stack. 
		
		state = move.getStateActedOn();
		char action = move.getAction();
		int playerID = move.getPlayerID();
		
		switch (action) {
		case 'P':
			if(playerID == 1){
				playerOneTokensToPlace++;
			}else{
				playerTwoTokensToPlace++;
			}
			break;
		case 'R':
			if(playerID == 1){
				playerTwoTokensRemaining++;
			}else{
				playerOneTokensRemaining++;
			}
			break;
		case 'M':
			break;
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see utility.MoveUtilityInterface#getAllPossibleMoves(char, char)
	 */
	public List<AbstractMove> getAllPossibleMoves(char action, char tokenColour) {
		
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

	/**
	 * Gets all possible placements for the passed in token colour. 
	 * 
	 * @param tokenColour - The colour that is to be placed.
	 * @return		      - The list of placement moves available. 
	 */
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
	
	/**
	 * Gets all possible removals for the passed in token colour.
	 * 
	 * @param tokenColour - The colour of the player carrying out the removals.
	 * @return			  - The list of removals available. 
	 */
	private List<AbstractMove> getAllPossibleRemovals(char tokenColour) {
		char[] stateArray = state.toCharArray();
		List<AbstractMove> moves = new ArrayList<AbstractMove>();
		if((tokenColour == 'R' && playerTwoTokensRemaining > 2) || (tokenColour == 'B' && playerOneTokensRemaining > 2)){
			for (int i = 0; i < stateArray.length; i++) {
				if(stateArray[i] != 'N' && stateArray[i] != tokenColour && (!partOfMill(i) || millFreeToken(tokenColour, i)) ){
					moves.add(new RemovalMove(state, tokenColour, i));
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
