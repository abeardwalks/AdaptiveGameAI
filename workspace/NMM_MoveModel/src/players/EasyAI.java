package players;

import java.util.Random;

import move.AbstractMove;
import move.MovementMove;
import move.PlacementMove;
import utility.MoveChecker;
import interfaces.BoardDetailsInterface;
import interfaces.IntPairInterface;

public class EasyAI extends AbstractPlayer {
	
	private MoveChecker movecheck;
	private BoardDetailsInterface game;
	private Random rdm = new Random();
	private int lastPlacement;
	
	
	@Override
	public int placeToken(BoardDetailsInterface game) {
		movecheck = new MoveChecker(game);
		this.game = game;
		
		int placement = -1;
		
		int decision = rdm.nextInt(2);
		
		if(decision == 0){
			placement = findMillMakingPlacement();
		}
		
		if(decision == 1 || placement == -1 ){
			placement = findMillBlockingPlacement();
		}
		if(placement != -1){
			lastPlacement = placement;
			return placement;
		}else{
			placement = getRandomPlacement();
		}
		lastPlacement = placement;
		return placement;
	}

	@Override
	public int removeToken(BoardDetailsInterface game) {
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
	public IntPairInterface moveToken(BoardDetailsInterface state) {
		
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
			AbstractMove m = movecheck.executeMove(new MovementMove(game.getState(), getTokenColour(), positionFrom, positionToo));
			movecheck.undo();
			if (m != null ) {
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
				AbstractMove m = movecheck.executeMove(new MovementMove(game.getState(), getTokenColour(), i, j));
				movecheck.undo();
				if(m != null){
					m = movecheck.executeMove(new PlacementMove(game.getState(), opponent, j));
					movecheck.undo();
					if(m.getAction() == 'R'){
						System.out.println("Returning Mill Blocking Move");
						return new IntPair(i, j);
					}
				}
			}
		}
		return null;
	}

	private IntPairInterface findMillMakingMovement() {
		for (int i = 0; i < 24; i++) {
			for(int j = 0; j < 24; j++){
				AbstractMove m = movecheck.executeMove(new MovementMove(game.getState(), getTokenColour(), i, j));
				movecheck.undo();
				if(m != null){
					if(m.getAction() == 'R'){
						System.out.println("Returning Mill Making Move");
						return new IntPair(i, j);
					}
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
		
		for (int i = 0; i < 24; i++) {
			AbstractMove m = movecheck.executeMove(new PlacementMove(game.getState(), opponent, i));
			if(m != null){
				if(m.getAction() == 'R'){
					movecheck.undo();
					return i;
				}
			}
			movecheck.undo();
		}
		return -1;
	}
	
	private int findMillMakingPlacement(){
	
		for (int i = 0; i < 24; i++) {
			AbstractMove m = movecheck.executeMove(new PlacementMove(game.getState(), getTokenColour(), i));
			if(m != null){
				if(m.getAction() == 'R'){
					movecheck.undo();
					return i;
				}
			}
			movecheck.undo();
		}
		return -1;
	}
	
	private int getRandomPlacement() {
		char[] stateArray = game.getState().toCharArray();
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
	
	

}
