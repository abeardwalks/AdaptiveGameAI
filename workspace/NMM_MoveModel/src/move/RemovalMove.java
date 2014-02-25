package move;

public class RemovalMove extends AbstractMove{

	private int removal;
	
	public RemovalMove(String state, char action, char playerColour, int removal) {
		super(state, action, playerColour);
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

}