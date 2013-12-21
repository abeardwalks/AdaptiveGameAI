package board;

public class BoardDetails{
	private String gs;
	private int result;
	private int playerOneRemaining;
	private int playerTwoRemaining;
	


	public BoardDetails(String gs, int result, int playerOneRemaining, int playerTwoRemaining){
		this.gs = gs;
		this.result = result;
		this.playerOneRemaining = playerOneRemaining;
		this.playerTwoRemaining = playerTwoRemaining;
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
	
}
