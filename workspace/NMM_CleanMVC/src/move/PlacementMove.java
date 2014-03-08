package move;

/**
 * Class representative of a placement by a player.
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 *
 */
public class PlacementMove extends AbstractMove{

	private int placement;
	
	public PlacementMove(String state,char playerColour, int placement) {
		super(state, playerColour);
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

	@Override
	public char getAction() {
		return 'P';
	}

}
