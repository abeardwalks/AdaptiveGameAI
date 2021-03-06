package players;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import board.BoardDetails;
import board.Phase;

import interfaces.GameStateInterface;
import interfaces.PlayerInterface;

public class RandomAI implements PlayerInterface, Observer {
	
	private char tokenColour;
	private final String name = "RandomAI";
	private GameStateInterface gs;
	private String gsString = "NNNNNNNNNNNNNNNNNNNNNNNN";
	private char[] gsArray;
	private int result;
	private char turn;
	private Phase phase;
	private Random rdm;
	

	@Override
	public String getName() {
		return name;
	}

	@Override
	public char getTokenColour() {
		return tokenColour;
	}

	@Override
	public void setTokenColour(char tokenColour) {
		this.tokenColour = tokenColour;
	}

	@Override
	public void intialize(GameStateInterface gs) {
		this.gs = gs;
		phase = gs.getPhase();
		gsArray = gsString.toCharArray();
		turn = 'R';
		result = -2;
		rdm = new Random();
	}

	@Override
	public void makeMove() {

		if(turn == tokenColour){
			System.out.println("In If: " + tokenColour);
			int node;
			boolean nodeFound = true;
			if(phase.equals(Phase.ONE)){
				if(result == -2 || result == 0 || result == -1){
					while(nodeFound){
						node = rdm.nextInt(24);
						if(gsArray[node] == 'N'){
							
							nodeFound = false;
							gs.addToken(tokenColour, node);
							break;
						}
					}
				}
			}
			if((phase.equals(Phase.TWO) || phase.equals(Phase.THREE)) && (result == 0 || result == -1)){
				
			}
			phase = gs.getPhase();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		BoardDetails bd = (BoardDetails)arg1;
		result = bd.getResult();
		turn = bd.getTurn();
		gsString = gs.getState();
		gsArray = gsString.toCharArray();
		
	}

}
