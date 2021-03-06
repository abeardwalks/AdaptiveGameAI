package model.board;

import java.util.Collection;
import java.util.Observable;
import java.util.Stack;

import model.Phase;

import interfaces.GameStateInterface;

public class MorrisBoard extends Observable implements GameStateInterface {
	
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
	
	public MorrisBoard(){
	
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

	
	public void addToken(char token, int position) {
		char[] stateArray = state.toCharArray();
		stateArray[position] = token;
		state = new String(stateArray);
		BoardDetails details = new BoardDetails(state, result, playerOneTokensRemaining, playerTwoTokensRemaining, playerOneTokensToPlace, playerTwoTokensToPlace, turn, gamePhase); //for MVC
		setChanged();
		notifyObservers(details);//for MVC
	}
	
	
	public void removeToken(int position) {
		char[] stateArray = state.toCharArray();
		if(stateArray[position] == 'R'){
			playerOneTokensRemaining--;
		}else if(stateArray[position] == 'B'){
			playerTwoTokensRemaining--;
		}
		stateArray[position] = 'N';
		state = new String(stateArray);
		BoardDetails details = new BoardDetails(state, result, playerOneTokensRemaining, playerTwoTokensRemaining, playerOneTokensToPlace, playerTwoTokensToPlace, turn, gamePhase);  //for MVC
		setChanged();
		notifyObservers(details);	//for MVC
	}



	public void moveToken(int x, int y) {
		char[] stateArray = state.toCharArray();
		char moving = stateArray[x];
		stateArray[x] = 'N';
		stateArray[y] = moving;
		state = new String(stateArray);
		BoardDetails details = new BoardDetails(state, result, playerOneTokensRemaining, playerTwoTokensRemaining, playerOneTokensToPlace, playerTwoTokensToPlace, turn, gamePhase);  //for MVC
		setChanged();
		notifyObservers(details);	//for MVC
	}

	public void setState(String state){
		this.state = state;
	}

	@Override
	public void reset() {
		state = "NNNNNNNNNNNNNNNNNNNNNNNN";
		history = new Stack<String>();
		history.add(state);
		
		gamePhase = Phase.ONE;
		result = -2;
		playerOneTokensToPlace = 9;
		playerTwoTokensToPlace = 9;
		playerOneTokensRemaining = 9;
		playerTwoTokensRemaining = 9;
		won = false;
		turn = 'R';
		BoardDetails details = new BoardDetails(state, result, playerOneTokensRemaining, playerTwoTokensRemaining, playerOneTokensToPlace, playerTwoTokensToPlace, turn, gamePhase);  //for MVC
		setChanged();
		notifyObservers(details);	//for MVC
	}

	@Override
	public void undo() {
	}

	@Override
	public void redo() {
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public void setPhase(Phase phase) {
		gamePhase = phase;
	}
	
	public Phase getPhase(){
		return gamePhase;
	}


	@Override
	public void lowerPlayerOneCount() {
		playerOneTokensToPlace--;
	}


	@Override
	public void lowerPlayerTwoCount() {
		playerTwoTokensToPlace--;
	}

	public void setResult(int result){
		this.result = result;
	}

	@Override
	public void setTurn() {
		if(turn == 'R'){
			turn = 'B';
		}else{
			turn = 'R';
		}
		System.out.println("Turn: " + turn);
		BoardDetails details = new BoardDetails(state, result, playerOneTokensRemaining, playerTwoTokensRemaining, playerOneTokensToPlace, playerTwoTokensToPlace, turn, gamePhase);  //for MVC
		setChanged();
		notifyObservers(details);	//for MVC
	}

}