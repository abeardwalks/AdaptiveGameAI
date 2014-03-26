package move;

/**
 * Class representative of a removal by a player.
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 *
 */
public class RemovalMove extends AbstractMove{

	private int removal;
	
	public RemovalMove(String state, char playerColour, int removal) {
		super(state, playerColour);
		this.removal = removal;
	}

	public int getRemovalIndex(){
		return removal;
	}
	
	@Override
	public String getStatePostAction() {
		String actingOn = super.getStateActedOn();
		char[] actingOnArray = actingOn.toCharArray(); 
		actingOnArray[removal] = 'N';
		return new String(actingOnArray);
	}

	@Override
	public char getAction() {
		return 'R';
	}

}
