package model.board;

import java.util.List;
import java.util.Observable;
import java.util.Stack;

import utility.BoardManagement;
import model.Phase;
import move.AbstractMove;
import interfaces.BoardFacadeInterface;
import interfaces.Player;

public class Model extends Observable implements BoardFacadeInterface {
	
	private String state;
	private Stack<AbstractMove> history;				//the history of the game, this allows for moves to be undone. 
	private int playerOneToPlace, playerTwoToPlace;
	private int playerOneRemaining, playerTwoRemaining;
	private Phase phase;
	private char turn;
	private char nextAction;
	private char trappedPlayer;
	private boolean valid;
	private boolean millMade;
	private int chasePhaseMoves;
	
	private Player playerOne, playerTwo;
	
	private BoardManagement manager;
	
	private double p1millcount;
	private double p2millcount;
	
	private int gamesToPlay, gamesPlayed, playerOneWins, playerTwoWins, draws;
	private boolean playerOneWin, playerTwoWin;
	
	private boolean gameover;
	
	public Model(){
		state = "NNNNNNNNNNNNNNNNNNNNNNNN";
		history = new Stack<AbstractMove>();
		
		playerOneToPlace = 9;
		playerTwoToPlace = 9;
		playerOneRemaining = 9;
		playerTwoRemaining = 9;
		
		p1millcount = 0;
		p2millcount = 0;
		phase = Phase.ONE;
		
		turn = 'R';
		nextAction = 'P';
		valid = false;
		millMade = false;
		
		manager = new BoardManagement(this);
		
		gamesToPlay = 0;
		gamesPlayed = 0;
		playerOneWins = 0;
		playerTwoWins = 0;
		draws = 0;
		chasePhaseMoves = 0;
		
		playerOneWin = false;
		playerTwoWin = false;
		
		gameover = false;
	}
	
	public Model(String state, int playerOneToPlace, int playerTwoToPlace, int playerOneRemaining, int playerTwoRemaining, Phase phase, int turn, boolean millMade, char nextAction){
		this.state = state;
		history = new Stack<AbstractMove>();
		
		this.playerOneToPlace = playerOneToPlace;
		this.playerTwoToPlace = playerTwoToPlace;
		this.playerOneRemaining = playerOneRemaining;
		this.playerTwoRemaining = playerTwoRemaining;
		
		p1millcount = 0;
		p2millcount = 0;
		this.phase = phase;
		
		if(turn == 1){
			this.turn = 'R';
		}else{
			this.turn = 'B';
		}
		
		valid = false;
		this.millMade = millMade;
		this.nextAction = nextAction;
		manager = new BoardManagement(this);
		
		gameover = false;
	}

	@Override
	public void executeMove(AbstractMove move) {
		if(move.getAction() == nextAction){
			int result = manager.resultOfMove(move);
			if(result > -1){
				history.push(move);
				valid = true;
				state = move.getStatePostAction();
				switch (move.getAction()) {
				case 'P':
					if(move.getPlayerID() == 1){
						playerOneToPlace--;
					}else{
						playerTwoToPlace--;
					}
					break;
				case 'R':
					if(move.getPlayerID() == 1){
						playerTwoRemaining--;
						p1millcount++;
					}else{
						playerOneRemaining--;
						p2millcount++;
					}
					break;
				case 'M':
					if(phase == Phase.THREE){
						chasePhaseMoves++;
					}
					break;
				default:
					break;
				}
				phase = manager.calculatePhase();
				AbstractMove nextmove = manager.nextMove(move, result);
				if(nextmove.getAction() == 'R'){
					millMade = true;
				}else{
					millMade = false;
					setTurn();
				}
				nextAction = nextmove.getAction();
				setChanged();
				notifyObservers();
			}else{
				valid = false;
			}
		}else{
			valid = false;
		}
		phase = manager.calculatePhase();
	}
	
	@Override
	public boolean validMove() {
		return valid;
	}

	@Override
	public boolean millMade() {
		return millMade;
	}

	@Override
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
				p1millcount--;
				turn = move.getPlayerColour();
			}else{
				playerOneRemaining++;
				p2millcount--;
				turn = move.getPlayerColour();
			}
			millMade = true;
			break;
		case 'M':
			turn = move.getPlayerColour();
			if(phase == Phase.THREE){
				chasePhaseMoves--;
			}
			break;
		default:
			break;
		}
		nextAction = move.getAction();
		phase = manager.calculatePhase();
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
	}
	
	

	@Override
	public void setPhase(Phase phase) {
		this.phase = phase;
	}

	@Override
	public void setNextAction(char action) {
		this.nextAction = action;
	}

	@Override
	public void reset() {

		state = "NNNNNNNNNNNNNNNNNNNNNNNN";
		history = new Stack<AbstractMove>();
		
		playerOneToPlace = 9;
		playerTwoToPlace = 9;
		playerOneRemaining = 9;
		playerTwoRemaining = 9;
		
		chasePhaseMoves = 0;
		phase = Phase.ONE;
		
		turn = 'R';
		nextAction = 'P';
		valid = false;
		millMade = false;
		gameover = false;
		playerOneWin = false;
		playerTwoWin = false;
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void setTrappedPlayer(char trappedPlayer) {
		this.trappedPlayer = trappedPlayer;
		if(trappedPlayer != 'N'){
			System.out.println("Trapped Player Set: " + trappedPlayer + ", " +  gamesPlayed + ", " + phase);
		}
		
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
//		if(!manager.playersCanMove()){
//			phase = Phase.FOUR;
//		}
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
		phase = manager.calculatePhase();
		if(gameover){
			return true;
		}
		if(playerOneRemaining == 2 || playerTwoRemaining == 2){
			if(playerOneRemaining == 2){
				playerTwoWin = true;
				playerTwoWins++;
//				System.err.println("PLAYER TWO WIN");
			}
			
			if(playerTwoRemaining == 2){
				playerOneWin = true;
				playerOneWins++;
//				System.err.println("PLAYER ONE WIN");
			}
			gameover = true;
			gamesPlayed++;
			setChanged();
			notifyObservers(new String("write"));
			return true;
		}else if(phase == Phase.FOUR){
			if(trappedPlayer == 'B'){
				playerOneWin = true;
				playerOneWins++;
				System.err.println("PLAYER ONE WIN" + gamesPlayed);
			}else if(trappedPlayer == 'R'){
				playerTwoWin = true;
				playerTwoWins++;
				System.err.println("PLAYER TWO WIN" + gamesPlayed);
			}
			gameover = true;
			gamesPlayed++;
			setChanged();
			notifyObservers(new String("write"));
			return true;
		}else if(phase == Phase.THREE && chasePhaseMoves == 15){
		
			gameover = true;
			gamesPlayed++;
			draws++;
			setChanged();
			notifyObservers(new String("write"));
			System.err.println("GAME DRAW");
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public char getNextAction() {
		return nextAction;
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
		System.out.println("Turn: " + turn);
		System.out.println("Mill Made? " + millMade);
		System.out.println("Next Action: " + nextAction);
		System.out.println("Players can Move: " + manager.playersCanMove());
		System.out.println("--------------------------------------------------");
	}
	
	public List<AbstractMove> getAllPossibleMoves(){
		
		switch (nextAction) {
		case 'P':
			return manager.getAllPossiblePlacements(turn);
		case 'R':
			return manager.getAllPossibleRemovals(turn);
		case 'M':
			return manager.getAllPossibleMovements(turn);
		default:
			break;
		}
		
		return null;
	}
	
	@Override
	public void setPlayers(Player playerOne, Player playerTwo) {
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}
	
	public Player getPlayerOne(){
		return playerOne;
	}
	
	public Player getPlayerTwo(){
		return playerTwo;
	}

	@Override
	public int getGamesPlayed() {
		return gamesPlayed;
	}

	@Override
	public int getGamesToPlay() {
		return gamesToPlay;
	}

	@Override
	public int getPlayerOneWins() {
		return playerOneWins;
	}

	@Override
	public int getPlayerTwoWins() {
		return playerTwoWins;
	}

	@Override
	public void setGamesToPlay(int gamesToPlay) {
		this.gamesToPlay = gamesToPlay;
	}

	@Override
	public int getNumberOfDraws() {
		return draws;
	}

	@Override
	public boolean playerOneWin() {
		return playerOneWin;
	}

	@Override
	public boolean playerTwoWin() {
		return playerTwoWin;
	}

	@Override
	public Stack<AbstractMove> getHistory() {
		return history;
	}

	@Override
	public boolean gameOver() {
		return gameover;
	}
	
}
