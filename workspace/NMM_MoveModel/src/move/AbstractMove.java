package move;


public abstract class AbstractMove  {
	
	private String stateActedOn;
	private char playerColour;
	
	public AbstractMove(String state, char playerColour){
		stateActedOn = state;
		this.playerColour = playerColour;
	}
	
	public String getStateActedOn() {
		return stateActedOn;
	}

	public abstract String getStatePostAction();

	public abstract char getAction();

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
