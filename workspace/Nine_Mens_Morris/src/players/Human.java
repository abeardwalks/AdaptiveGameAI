package players;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import interfaces.GameStateInterface;
import interfaces.PlayerInterface;

public class Human implements PlayerInterface{
	
	private final String name = "Human";
	
	private GameStateInterface gs;
	
	private int x0,x1,x2,x3,x4,x5,x6;
	private int y0,y1,y2,y3,y4,y5,y6;
	private int result = -2;
	public boolean turn = true;
	public boolean change = true;
	private char playerChar;

	public void setGameState(GameStateInterface gs){
		result = -2;
		this.gs = gs;
	}
	
	public void setPlayerChar(char playerChar){
		this.playerChar = playerChar;
	}
	
	@Override
	public int getTokensRemaining() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getChar() {
		return playerChar;
	}

	@Override
	public void removeToken() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return name;
	}
	
	private void makeMove(int nodeClicked) {
		int previousResult = result;
		if(result == -2 || result == 0){
			result = gs.addToken(playerChar, nodeClicked);
		} else if(result == 1){
			result = gs.removeToken(playerChar, nodeClicked);
		} 
		if(result == -1){
			result = previousResult;
		}
	}
	
	private int findNodeClicked(int x, int y){

		if((x >= x0 && x <= (x0 + 40)) && (y >= y0 && y <= (y0 + 40))){
			return 0;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y0 && y <= y0 + 40)){
			return 1;
		} else if((x >= x2 && x <= x2 + 40) && (y >= y0 && y <= y0 + 40)){
			return 2;
		} else if((x >= x3 && x <= x3 + 40) && (y >= y1 && y <= y1 + 40)){
			return 3;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y1 && y <= y1 + 40)){
			return 4;
		} else if((x >= x4 && x <= x4 + 40) && (y >= y1 && y <= y1 + 40)){
			return 5;
		} else if((x >= x5 && x <= x5 + 40) && (y >= y2 && y <= y2 + 40)){
			return 6;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y2 && y <= y2 + 40)){
			return 7;
		} else if((x >= x6 && x <= x6 + 40) && (y >= y2 && y <= y2 + 40)){
			return 8;
		} else if((x >= x0 && x <= x0 + 40) && (y >= y3 && y <= y3 + 40)){
			return 9;
		} else if((x >= x3 && x <= x3 + 40) && (y >= y3 && y <= y3 + 40)){
			return 10;
		} else if((x >= x5 && x <= x5 + 40) && (y >= y3 && y <= y3 + 40)){
			return 11;
		} else if((x >= x6 && x <= x6 + 40) && (y >= y3 && y <= y3 + 40)){
			return 12;
		} else if((x >= x4 && x <= x4 + 40) && (y >= y3 && y <= y3 + 40)){
			return 13;
		} else if((x >= x2 && x <= x2 + 40) && (y >= y3 && y <= y3 + 40)){
			return 14;
		} else if((x >= x5 && x <= x5 + 40) && (y >= y4 && y <= y4 + 40)){
			return 15;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y4 && y <= y4 + 40)){
			return 16;
		} else if((x >= x6 && x <= x6 + 40) && (y >= y4 && y <= y4 + 40)){
			return 17;
		} else if((x >= x3 && x <= x3 + 40) && (y >= y5 && y <= y5 + 40)){
			return 18;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y5 && y <= y5 + 40)){
			return 19;
		} else if((x >= x4 && x <= x4 + 40) && (y >= y5 && y <= y5 + 40)){
			return 20;
		} else if((x >= x0 && x <= x0 + 40) && (y >= y6 && y <= y6 + 40)){
			return 21;
		} else if((x >= x1 && x <= x1 + 40) && (y >= y6 && y <= y6 + 40)){
			return 22;
		} else if((x >= x2 && x <= x2 + 40) && (y >= y6 && y <= y6 + 40)){
			return 23;
		}
		return -1;
	}

	

	
	public void makeHumanPlacement(int x, int y){
		int nodeClicked = findNodeClicked(x, y);

		if(nodeClicked >= 0 && turn){
			makeMove(nodeClicked);
		}
	}
	
	public void makeHumanMove(int x, int y, int x2, int y2){
		int nodeFrom = findNodeClicked(x, y);
		int nodeTo = findNodeClicked(x2, y2);

		if(nodeFrom >= 0 && nodeTo >= 0 && turn){
			int previousResult = result;
			if(result == -2 || result == 0){
				result = gs.moveToken(playerChar, nodeFrom, nodeTo);
			} else if(result == 1){
				result = gs.moveToken(playerChar, nodeFrom, nodeTo);
			} 
			if(result == -1){
				result = previousResult;
			}
		}
		//System.out.println("Move MAde");
	}
	
	public void intializeCordinates() {
		x0 = 102;
		x1 = 375;
		x2 = 647;
		x3 = 193;
		x4 = 557;
		x5 = 285;
		x6 = 465;
		
		y0 = 7;
		y1 = 97;
		y2 = 188;
		y3 = 281;
		y4 = 371;
		y5 = 462;
		y6 = 552;
	}

	@Override
	public void setChar(char playerChar) {
		this.playerChar = playerChar;
	}
	
	@Override
	public void setTurn() {
	}
	
	public boolean getTurn(){
		return turn;
	}

}
