package players;

import java.util.Random;

import model.board.Model;
import move.MovementMove;
import move.PlacementMove;
import interfaces.BoardViewInterface;
import interfaces.BoardFacadeInterface;
import interfaces.IntPairInterface;

public class EasyAI extends AbstractPlayer {
	
	private BoardFacadeInterface workingGame;
	private Random rdm = new Random();	
	
	@Override
	public int placeToken(BoardViewInterface game) {
		workingGame = new Model(game.getState(), game.getPlayerOneToPlace() + 1, game.getPlayerTwoToPlace() + 1, 
				 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
				 game.getPhase(), game.getTurn(), 
				 game.millMade(), game.getNextAction());
		
		int placement = -1;
		
		int decision = rdm.nextInt(2);
		if(decision == 0){
			placement = findMillMakingPlacement();
		}
		
		if(decision == 1){
			placement = findMillBlockingPlacement();
		}
		if(placement != -1){
			return placement;
		}else{
			placement = getRandomPlacement();
		}
		return placement;
	}

	@Override
	public int removeToken(BoardViewInterface game) {
		char[] stateArray = game.getState().toCharArray();
		boolean found = false;
		int index = 0;
		while(!found){
			index = rdm.nextInt(24);
			if(stateArray[index] != this.getTokenColour() && stateArray[index] != 'N'){
				found = true; 
			}
		}
		return index;
	}

	@Override
	public IntPairInterface moveToken(BoardViewInterface game) {
		workingGame = new Model(game.getState(), game.getPlayerOneToPlace() + 1, game.getPlayerTwoToPlace() + 1, 
				 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
				 game.getPhase(), game.getTurn(), 
				 game.millMade(), game.getNextAction());
		
		int decision = rdm.nextInt(4);
		IntPairInterface movement = null;
		
		if(decision == 0 || decision == 1){
			movement = findMillMakingMovement();
		}
		
		if(decision == 2){
			movement = findMillBlockingMovement();
		}
		
		if(decision == 3 || movement == null){
			movement = findRandomMovement();
		}
		return movement;
	}

	private IntPairInterface findRandomMovement() {
		boolean found = false;
		int positionFrom = 0;
		int positionToo = 0;
		while (!found) {
			positionFrom = rdm.nextInt(24);
			positionToo = rdm.nextInt(24);
			workingGame.executeMove(new MovementMove(workingGame.getState(), getTokenColour(), positionFrom, positionToo));
			if(workingGame.validMove()){
				found = true;
			}
		}
		return new IntPair(positionFrom, positionToo);
	}

	private IntPairInterface findMillBlockingMovement() {
		char opponent = 'R';
		
		if(getTokenColour() == 'R'){
			opponent = 'B';
		}
		
		for (int i = 0; i < 24; i++) {
			for(int j = 0; j < 24; j++){
				workingGame.executeMove(new MovementMove(workingGame.getState(), getTokenColour(), i, j));
				if(workingGame.validMove()){
					workingGame.undo();
					workingGame.setTurn();
					workingGame.executeMove(new PlacementMove(workingGame.getState(), opponent, j));
					if(workingGame.validMove()){
						if(workingGame.millMade()){
							return new IntPair(i, j);
						}
						workingGame.undo();
					}
					workingGame.setTurn();
				}
			}
		}
		return null;
	}

	private IntPairInterface findMillMakingMovement() {
		for (int i = 0; i < 24; i++) {
			for(int j = 0; j < 24; j++){
				workingGame.executeMove(new MovementMove(workingGame.getState(), getTokenColour(), i, j));
				if(workingGame.validMove()){
					if(workingGame.millMade()){
						return new IntPair(i, j);
					}
					workingGame.undo();
				}
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return "Easy AI";
	}
	
	private int findMillBlockingPlacement() {
		char opponent = 'R';
		
		if(getTokenColour() == 'R'){
			opponent = 'B';
		}
		workingGame.setTurn();
		for (int i = 0; i < 24; i++) {
			workingGame.executeMove(new PlacementMove(workingGame.getState(), opponent, i));
			if(workingGame.validMove()){
				if(workingGame.millMade()){
					return i;
				}
				workingGame.undo();
			}
		}
		return -1;
	}
	
	private int findMillMakingPlacement(){
		for (int i = 0; i < 24; i++) {
			workingGame.executeMove(new PlacementMove(workingGame.getState(), getTokenColour(), i));
			if(workingGame.validMove()){
				if(workingGame.millMade()){
					return i;
				}
				workingGame.undo();
			}
		}
		return -1;
	}
	
	private int getRandomPlacement() {
		char[] stateArray = workingGame.getState().toCharArray();
		boolean found = false;
		int index = 0;
		while(!found){
			index = rdm.nextInt(24);
			if(stateArray[index] == 'N'){
				found = true; 
			}
		}
		return index;
	}
	
	public void reset(){
	}
	
}
