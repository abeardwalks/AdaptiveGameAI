package model.board;

import java.util.Observable;
import java.util.Stack;

import model.Phase;
import move.AbstractMove;
import interfaces.BoardDetailsInterface;
import interfaces.BoardFacadeInterface;
import interfaces.BoardMutatorInterface;

public class BoardModel extends Observable implements BoardFacadeInterface {
	
	private String state;
	private Stack<AbstractMove> history;
	private int playerOneToPlace, playerTwoToPlace;
	private int playerOneRemaining, playerTwoRemaining;
	private Phase phase;
	private char turn;
	
	public BoardModel(){
		
		state = "NNNNNNNNNNNNNNNNNNNNNNNN";
		history = new Stack<AbstractMove>();
		
		playerOneToPlace = 9;
		playerTwoToPlace = 9;
		playerOneRemaining = 9;
		playerTwoRemaining = 9;
		
		phase = Phase.ONE;
		
		turn = 'R';
		
	}
	
	public BoardModel(String state, int playerOneToPlace, int playerTwoToPlace, int playerOneRemianing, int playerTwoRemaining, Phase phase, int turn){
		
		this.state = state;
		history = new Stack<AbstractMove>();
		
		this.playerOneToPlace = playerOneToPlace;
		this.playerTwoToPlace = playerTwoToPlace;
		this.playerOneRemaining = playerOneRemianing;
		this.playerTwoRemaining = playerTwoRemaining;
		
		this.phase = phase;
		
		if(turn == 1){
			this.turn = 'R';
		}else{
			this.turn = 'B';
		}
		
	}

	@Override
	public void executeMove(AbstractMove move) {
		history.push(move);
		state = move.getStatePostAction();
		char action = move.getAction();
		
		switch (action) {
		case 'P':
			if(turn == 'R'){
				playerOneToPlace--;
			}else{
				playerTwoToPlace--;
			}
			break;
		case 'R':
			if(turn == 'R'){
				playerTwoRemaining--;
			}else{
				playerOneRemaining--;
			}
			break;
		case 'M':
			break;
		default:
			break;
		}
		
		setChanged();
		notifyObservers();
	}

	@Override
	public void undo() {
		AbstractMove move = history.pop();
		
		state = move.getStateActedOn();
		char action = move.getAction();
		int playerID = move.getPlayerID();
		
		switch (action) {
		case 'P':
			if(playerID == 1){
				playerOneToPlace++;
				turn = move.getPlayerColour();
			}else{
				playerTwoToPlace++;
				turn = move.getPlayerColour();
			}
			break;
		case 'R':
			if(playerID == 1){
				playerTwoRemaining++;
				turn = move.getPlayerColour();
			}else{
				playerOneRemaining++;
				turn = move.getPlayerColour();
			}
			break;
		case 'M':
			turn = move.getPlayerColour();
			break;
		default:
			break;
		}
		
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void setTurn() {
		if(turn == 'R'){
			turn = 'B';
		}else{
			turn = 'R';
		}
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	@Override
	public void reset() {
		
		state = "NNNNNNNNNNNNNNNNNNNNNNNN";
		history = new Stack<AbstractMove>();
		
		playerOneToPlace = 9;
		playerTwoToPlace = 9;
		playerOneRemaining = 9;
		playerTwoRemaining = 9;
		
		phase = Phase.ONE;
		
		turn = 'R';
		
		setChanged();
		notifyObservers();
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public int getPlayerOneToPlace() {
		return playerOneToPlace;
	}

	@Override
	public int getPlayerTwoToPlace() {
		return playerTwoToPlace;
	}

	@Override
	public int getPlayerOneRemaining() {
		return playerOneRemaining;
	}

	@Override
	public int getPlayerTwoRemaining() {
		return playerTwoRemaining;
	}

	@Override
	public Phase getPhase() {
		return phase;
	}

	@Override
	public int getTurn() {
		if(turn == 'R'){
			return 1;
		}else{
			return 2;
		}
	}

	@Override
	public boolean gameWon() {
		if(playerOneRemaining == 2 || playerTwoRemaining == 2 || phase == Phase.FOUR){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public double[] getRewards() {
		
		double[] rewards = new double[2];
	
		if(playerOneRemaining < 3){
			rewards[0] = 0.0;
			rewards[1] = 1.0;
		}else if(playerTwoRemaining < 3){
			rewards[0] = 1.0;
			rewards[1] = 0.0;
		}else if(playerOneRemaining == playerTwoRemaining){
			rewards[0] = 0.5;
			rewards[1] = 0.5;
		}else if(playerOneRemaining > playerTwoRemaining){
			rewards[0] = 0.5;
			rewards[1] = 0.0;
		}else if(playerOneRemaining < playerTwoRemaining){
			rewards[0] = 0.0;
			rewards[1] = 0.5;
		}
		
		if(trappedPlayer == 'R'){
			rewards[0] = 0.0;
			rewards[1] = 1.0;
		}else if(trappedPlayer == 'B'){
			rewards[0] = 1.0;
			rewards[1] = 0.0;
		}
		
		return rewards;
	}

	@Override
	public void printDetails() {
		System.out.println("-------------- Board Details ---------------------");
		System.out.println("State: " + state);
		System.out.println("P1 TP: " + playerOneToPlace);
		System.out.println("P2 TP: " + playerTwoToPlace);
		System.out.println("P1 TR: " + playerOneRemaining);
		System.out.println("P2 TR: " + playerTwoRemaining);
		System.out.println("Phase: " + phase);
		System.out.println("--------------------------------------------------");
	}

	private char trappedPlayer;
	
	@Override
	public void setTrappedPlayer(char c) {
		trappedPlayer = c;
	}

}
