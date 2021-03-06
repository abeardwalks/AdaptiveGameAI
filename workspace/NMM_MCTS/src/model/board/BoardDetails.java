package model.board;

import model.Phase;

public class BoardDetails{
	private String gs;
	private int result;
	private int playerOneRemaining;
	private int playerTwoRemaining;
	private int playerOneToPlace;
	private int playerTwoToPlace;
	private char turn;
	private Phase phase;

	public BoardDetails(String gs, int result, int playerOneRemaining, int playerTwoRemaining, int playerOneToPlace, int playerTwoToPlace, char turn, Phase phase){
		this.gs = gs;
		this.result = result;
		this.playerOneRemaining = playerOneRemaining;
		this.playerTwoRemaining = playerTwoRemaining;
		this.playerOneToPlace = playerOneToPlace;
		this.playerTwoToPlace = playerTwoToPlace;
		this.turn = turn;
		this.phase = phase;
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
	
	public int getPlayerOneToPlace(){
		return playerOneToPlace;
	}
	
	public int getPlayerTwoToPlace(){
		return playerTwoToPlace;
	}
	
	public char getTurn(){
		return turn;
	}
	
	public Phase getPhase(){
		return phase;
	}
	
	public void printDetails(){
		System.out.println("-----------Board Details----------");
		System.out.println("State: " + gs);
		System.out.println("P1 TP: " + playerOneToPlace);
		System.out.println("P2 TP: " + playerTwoToPlace);
		System.out.println("P1 TR: " + playerOneRemaining);
		System.out.println("P2 TR: " + playerTwoRemaining);
		System.out.println("Current Turn: " + turn);
		System.out.println("Phase: " + phase);
		System.out.println("--------------------------------------");
	}
	
}
