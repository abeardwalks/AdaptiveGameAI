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
				playerOneRemaining--;
			}else{
				playerTwoRemaining--;
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
				turn = 'R';
			}else{
				playerTwoToPlace++;
				turn = 'B';
			}
			break;
		case 'R':
			if(playerID == 1){
				playerOneRemaining++;
				turn = 'R';
			}else{
				playerTwoRemaining++;
				turn = 'B';
			}
			break;
		case 'M':
			if(playerID == 1){
				turn = 'R';
			}else{
				turn = 'B';
			}
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
		if(playerOneRemaining == 2 || playerTwoRemaining == 2){
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
		
		return rewards;
	}

}
