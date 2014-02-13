package players.MCTS;

import java.util.Collection;
import java.util.Stack;

import model.Phase;
import model.board.Move;
import interfaces.GameStateInterface;

public class SimpleGameState implements GameStateInterface {

	private String state;
	private Collection<String> history;
	@SuppressWarnings("unused")
	private static final int STRING_LENGTH = 23;
	private Phase gamePhase;
	@SuppressWarnings("unused")
	private int playerOneTokensToPlace, playerTwoTokensToPlace, playerOneTokensRemaining, playerTwoTokensRemaining;
	private char turn;
	private int result;
	@SuppressWarnings("unused")
	private boolean won;
	
	public SimpleGameState(){
		state = "NNNNNNNNNNNNNNNNNNNNNNNN";
		history = new Stack<String>();
		history.add(state);
		
		gamePhase = Phase.ONE;
		
		playerOneTokensToPlace = 9;
		playerTwoTokensToPlace = 9;
		playerOneTokensRemaining = 9;
		playerTwoTokensRemaining = 9;
		result = -2;
		won = false;
		turn = 'R';
	}
	
	@Override
	public void addToken(char token, int position) {
		char[] stateArray = state.toCharArray();
		stateArray[position] = token;
		state = new String(stateArray);
		if(token == 'R'){
			playerOneTokensToPlace--;
		}else{
			playerTwoTokensToPlace--;
		}
		setTurn();
	}

	@Override
	public void removeToken(int position) {
		char[] stateArray = state.toCharArray();
		stateArray[position] = 'N';
		state = new String(stateArray);
		if(turn == 'R'){
			playerTwoTokensRemaining--;
		}else{
			playerOneTokensRemaining--;
		}
		setTurn();
	}

	@Override
	public void moveToken(int x, int y) {
		char[] stateArray = state.toCharArray();
		char moving = stateArray[x];
		stateArray[x] = 'N';
		stateArray[y] = moving;
		state = new String(stateArray);
		setTurn();
	}
	
	public void makeMove(Move move){
		char action = move.getAction();
		
		switch (action) {
		case 'P':
			addToken(move.getPlayer(), move.getPlacement());
			break;
		case 'M':
			moveToken(move.getMovement().getFirstInt(), move.getMovement().getSecondInt());
			break;
		case 'R':
			removeToken(move.getRemoval());
			break;
		default:
			break;
		}
	}

	@Override
	public void lowerPlayerOneCount() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void lowerPlayerTwoCount() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTurn() {
		if(turn == 'R'){
			turn = 'B';
		}else{
			turn = 'R';
		}
	}

	@Override
	public char getTurn() {
		return turn;
	}

	@Override
	public int getPlayerID() {
		if(turn == 'R'){
			return 1;
		}else{
			return 2;
		}
	}
	
	

	@Override
	public String getState() {
		return state;
	}

	@Override
	public void setState(String state) {
		this.state = state;
	}

	@Override
	public void setPhase(Phase phase) {
		gamePhase = phase;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResult(int result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPlayerOneTokensToPlace() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlayerTwoTokensToPlace() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlayerOneTokensRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlayerTwoTokensRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setPlayerOneTokensRemaining(int n){
		playerOneTokensRemaining = n;
	}
	
	public void setPlayerTwoTokensRemaining(int n){
		playerTwoTokensRemaining = n;
	}
	
	public void setPlayerOneTokensToPlace(int n){
		playerOneTokensToPlace = n;
	}
	
	public void setPlayerTwoTokensToPlace(int n){
		playerTwoTokensToPlace = n;
	}

	public Phase getPhase() {
		return gamePhase;
	}

	public double[] rewards() {
		// TODO Auto-generated method stub
		return null;
	}


	
}
