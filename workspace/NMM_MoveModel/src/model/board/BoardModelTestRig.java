package model.board;

import model.Phase;
import interfaces.TestRigInterface;

public class BoardModelTestRig extends BoardModel implements TestRigInterface {
	
	private int numberOfGamesToPlay, numberOfGamesPlayed;
	private int playerOneWins, playerTwoWins;
	
	public BoardModelTestRig(){
		super();
		numberOfGamesToPlay = 0;
		numberOfGamesPlayed = 0;
		playerOneWins = 0;
		playerTwoWins = 0;
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
	public int getNumberOfGamesToPlay() {
		return numberOfGamesToPlay;
	}

	@Override
	public int getNumberOfGamesPlayed() {
		return numberOfGamesPlayed;
	}
	
	public boolean gameWon(){
		if(getPlayerOneRemaining() == 2){
			playerTwoWins++;
			numberOfGamesToPlay--;
			numberOfGamesPlayed++;
			return true;
		}else if(getPlayerTwoRemaining() == 2){
			playerOneWins++;
			numberOfGamesToPlay--;
			numberOfGamesPlayed++;
			return true;
		}else if(getPhase() == Phase.FOUR){
			if(getTrappedPlayer() == 'R'){
				playerTwoWins++;
				numberOfGamesToPlay--;
				numberOfGamesPlayed++;
				return true;
			}else{
				playerOneWins++;
				numberOfGamesToPlay--;
				numberOfGamesPlayed++;
				return true;
			}
		}		
		return false;
	}

	@Override
	public void setNumberOfGamesToPlay(int numberOfGamesToPlay) {
		this.numberOfGamesToPlay = numberOfGamesToPlay;
	}
	
	

}
