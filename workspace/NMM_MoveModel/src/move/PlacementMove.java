package move;

public class PlacementMove extends AbstractMove{

	private int placement;
	
	public PlacementMove(String state, char action, char playerColour, int placement) {
		super(state, action, playerColour);
		this.placement = placement;
	}
	
	public int getPlacementIndex(){
		return placement;
	}

	@Override
	public String getStatePostAction() {
		String actingOn = super.getStateActedOn();
		char[] actingOnArray = actingOn.toCharArray(); 
		actingOnArray[placement] = super.getPlayerColour();
		return new String(actingOnArray);
	}

}
