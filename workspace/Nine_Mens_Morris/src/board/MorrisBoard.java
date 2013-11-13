package board;

import java.awt.Color;
import java.util.Observable;

import javax.xml.crypto.KeySelector.Purpose;

import Player.Token;
import interfaces.GameStateInterface;

public class MorrisBoard extends Observable implements GameStateInterface{
	
	private Token[][] board;
	private static final Color PLAYER1_COLOR = Color.RED;
	private static final Color PLAYER2_COLOR = Color.YELLOW;
	
	public MorrisBoard(){
		initialiseBoard();
	}

	private void initialiseBoard() {
		int x = 0;
		int y = 0;
		while(x < 7){
			
			if(y == 7){
				y = 0;
			}
			
			if(((x == 0 || x == 6) && (y == 1 || y == 2 || y == 4 || y == 5)) 
			|| ((x == 1 || x == 5) && (y == 0 || y == 2 || y == 4 || y == 6))
			|| ((x == 2 || x == 4) && (y == 0 || y == 1 || y == 5 || y == 6))
			||  (x == 3 && (y == 3))){
				board[x][y] = new Token(x, y, Color.ORANGE);
			}else{
				board[x][y] = null;
			}
		}
	}

	@Override
	public int addToken(Token token) {
		
		int tokenX = token.getX();
		int tokenY = token.getY();
		Token present = board[tokenX][tokenY];
		
		if(present != null){
			return -1;
		}else{
			board[tokenX][tokenY] = token;
		}
		
		if(canMove()){
			
		}
		
		if(millCreated(tokenX, tokenY)){
			return 1;
		}else if(gameWon()){
			return 2;
		}
		return 0;
	}

	private boolean canMove() {
		return false;
	}

	private boolean gameWon() {
		boolean playerOne = false;
		boolean playerTwo = false;
		return false;
	}

	private boolean millCreated(int x, int y) {
		return false;
	}

	@Override
	public int removeToken(Token token) {
		return 0;
	}
	
	@Override
	public int moveToken(Token token, int x, int y) {
		return 0;
	}

	@Override
	public GameStateInterface getState() {
		return null;
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void undo() {
		
	}

	@Override
	public void redo() {
		
	}
	
}
