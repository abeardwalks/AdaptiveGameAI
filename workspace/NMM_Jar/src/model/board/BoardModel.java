package model.board;

import java.util.List;
import java.util.Observable;
import java.util.Stack;
import model.Phase;
import move.AbstractMove;
import interfaces.BoardFacadeInterface;
import interfaces.Player;

/**
 * The Concrete class that represents the board, i.e. the model. This class extends
 * the Observable class and implements the BoardFacadeInterface.
 * 
 * Note: This is the old version of the model, that is still used by some of players.
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 *
 */
public class BoardModel extends Observable implements BoardFacadeInterface {
	
	private String state;
	private Stack<AbstractMove> history;				//the history of the game, this allows for moves to be undone. 
	private int playerOneToPlace, playerTwoToPlace;
	private int playerOneRemaining, playerTwoRemaining;
	private Phase phase;
	private char turn;
	private char nextAction;
	private boolean valid;
	private boolean millMade;
	
	
	/**
	 * Constructs a new BoardModel with data representative of a new game.
	 */
	public BoardModel(){
		state = "NNNNNNNNNNNNNNNNNNNNNNNN";
		history = new Stack<AbstractMove>();
		
		playerOneToPlace = 9;
		playerTwoToPlace = 9;
		playerOneRemaining = 9;
		playerTwoRemaining = 9;
		
		phase = Phase.ONE;
		
		turn = 'R';
		nextAction = 'P';
		valid = false;
		millMade = false;
		
	}
	
	/**
	 * Constructs a new board model with passed in values, this is primarily used for test purposes and the MCTS. 
	 * 
	 * @param              state - The 24 character state string made up of the letters N | R | B.
	 * @param   playerOneToPlace - The number of tokens player one has left to place.
	 * @param   playerTwoToPlace - The number of tokens player two has left to place.
	 * @param playerOneRemianing - The number of tokens player one has left in the game.
	 * @param playerTwoRemaining - The number of tokens player two has left in the game.
	 * @param              phase - The phase of the game. 
	 * @param               turn - Whos turn it is. 
	 */
	public BoardModel(String state, int playerOneToPlace, int playerTwoToPlace, int playerOneRemianing, int playerTwoRemaining, Phase phase, int turn, char nextAction){
		
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
		
		this.nextAction = nextAction;
		
	}
	
	public BoardModel(String state, int playerOneToPlace, int playerTwoToPlace, int playerOneRemianing, int playerTwoRemaining, Phase phase, int turn, char nextAction, AbstractMove move){
		
		this.state = state;
		history = new Stack<AbstractMove>();
		history.push(move);
		
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
		
		this.nextAction = nextAction;
		
	}

	@Override
	/**
	 * @param move - executes the passed in move on the board, storing it in the stack. 
	 */
	public void executeMove(AbstractMove move) {
		
		history.push(move);
		state = move.getStatePostAction();
		char action = move.getAction();
		
		switch (action) {
		case 'P':
			if(turn == 'R'){			//if player one placed a token...
				playerOneToPlace--;		//...lower their placement count.
			}else{
				playerTwoToPlace--;		//otherwise lower player twos account. 
			}
			break;
		case 'R':
			if(turn == 'R'){			//if player one removed a token...
				playerTwoRemaining--;	//...lower the number of tokens player two has remaining. 
			}else{
				playerOneRemaining--;	//otherwise lower player one tokens remaining.
			}
			break;
		case 'M':
			break;
		default:
			break;
		}
		
		//Notify Observers. 
		setChanged();
		notifyObservers();
	}
	
	
	public boolean validMove(){
		return valid;
	}
	
	public boolean millMade(){
		return millMade;
	}
	
	@Override
	/**
	 * Undoes the last executed move. 
	 */
	public void undo() {
		AbstractMove move = history.pop();		//pop the last executed move from the history stack. 
		
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
		
		
		//notify observers
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
		
		//notify observers
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
		nextAction = 'P';
		valid = false;
		
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
	
		if(playerOneRemaining < 3){			//if Player One has lost...
			rewards[0] = 0.0;				
			rewards[1] = 1.0;				//...give player 2 a point.
		}else if(playerTwoRemaining < 3){	//if Player Two has lost...
			rewards[0] = 1.0;				//...give player 1 a point.
			rewards[1] = 0.0;
		}else if(playerOneRemaining == playerTwoRemaining){		//if the game is currently a draw...
			rewards[0] = 0.5;									//each player gets 0.5
			rewards[1] = 0.5;
		}else if((playerOneRemaining > playerTwoRemaining )){		//if player One has 3 more tokens than player 2. 
			rewards[0] = 0.5;											//give them 0.5.
			rewards[1] = 0.0;
		}else if((playerTwoRemaining > playerOneRemaining)){		//if player Two has 3 more tokens than player 1
			rewards[0] = 0.0;		
			rewards[1] = 0.5;											//give them 0.5
		}
		
		if(trappedPlayer == 'R'){			//if player 1 is trapped...
			rewards[0] = 0.0;
			rewards[1] = 1.0;				//...give player two 1 point.
		}else if(trappedPlayer == 'B'){		//if player 2 is trapped...
			rewards[0] = 1.0;				//...give player One 1 point.
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
	
	public char getTrappedPlayer(){
		return trappedPlayer;
	}

	@Override
	public void setNextAction(char action) {
		this.nextAction = action;
		setChanged();
		notifyObservers();
	}

	@Override
	public char getNextAction() {
		return nextAction;
	}


	@Override
	public Player getPlayerOne() {
		return null;
	}

	@Override
	public Player getPlayerTwo() {
		return null;
	}

	@Override
	public void setPlayers(Player playerOne, Player playerTwo) {
	}

	@Override
	public List<AbstractMove> getAllPossibleMoves() {
		return null;
	}

	@Override
	public int getGamesPlayed() {
		return 0;
	}

	@Override
	public int getGamesToPlay() {
		return 0;
	}

	@Override
	public int getPlayerOneWins() {
		return 0;
	}

	@Override
	public int getPlayerTwoWins() {
		return 0;
	}

	@Override
	public void setGamesToPlay(int gamesToPlay) {
	}

	@Override
	public int getNumberOfDraws() {
		return 0;
	}

	@Override
	public boolean playerOneWin() {
		return false;
	}

	@Override
	public boolean playerTwoWin() {
		return false;
	}

	@Override
	public Stack<AbstractMove> getHistory() {
		return null;
	}

	@Override
	public boolean gameOver() {
		return false;
	}
}
