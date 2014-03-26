package move;

/**
 * Abstract Move is the parent of the different move types. It provides 
 * the common functionality and defines abstract methods that will be 
 * defined by the subclasses.
 * 
 * It stores the state the move has been acted on and the player who owns
 * this move. 
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 *
 */
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

	/**
	 * Implemented by the subclasses, this calculates the new state
	 * after the action is carried out. 
	 * 
	 * @return - The state post the action.
	 */
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
