package move;


public abstract class AbstractMove  {
	
	private String stateActedOn;
	private char action;
	private char playerColour;
	
	public AbstractMove(String state, char action, char playerColour){
		stateActedOn = state;
		this.action = action;
		this.playerColour = playerColour;
	}
	
	public String getStateActedOn() {
		return stateActedOn;
	}

	public abstract String getStatePostAction();

	public char getAction() {
		return action;
	}

	public char getPlayerColour() {
		return playerColour;
	}

	public int getPlayerID() {
		if(playerColour == 'R'){
			return 1;
		}else{
			return 2;
		}
	}

}