package board;

public class BoardDetails{
	private String gs;
	private int result;
	private int playerOneRemaining;
	private int playerTwoRemaining;
	private char turn;
	


	public BoardDetails(String gs, int result, int playerOneRemaining, int playerTwoRemaining, char turn){
		this.gs = gs;
		this.result = result;
		this.playerOneRemaining = playerOneRemaining;
		this.playerTwoRemaining = playerTwoRemaining;
		this.turn = turn;
	}
	
	public String getGS(){
		return gs;
	}
	
	public int getResult(){
		return result;
	}
	
	public int getPlayerOneRemaining() {
		return playerOneRemaining;
	}

	public int getPlayerTwoRemaining() {
		return playerTwoRemaining;
	}
	
	public char getTurn(){
		return turn;
	}
	
}
