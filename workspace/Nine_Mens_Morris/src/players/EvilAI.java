package players;

import java.util.Observable;

import interfaces.GameStateInterface;
import interfaces.PlayerInterface;

public class EvilAI implements PlayerInterface {

	private final String name = "EvilAI";
	private char tokenColour;
	private GameStateInterface gs;
	
	
	
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
	}
	@Override
	public void makeMove() {		
	}
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}
