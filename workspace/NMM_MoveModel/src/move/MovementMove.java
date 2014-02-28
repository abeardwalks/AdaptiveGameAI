package move;

public class MovementMove extends AbstractMove {
	
	private int from, to;

	public MovementMove(String state, char playerColour, int from, int to) {
		super(state, playerColour);
		this.from = from;
		this.to = to;
	}

	public int getFrom(){
		return from;
	}
	
	public int getTo(){
		return to;
	}
	
	@Override
	public String getStatePostAction() {
		String actingOn = super.getStateActedOn();
		char[] actingOnArray = actingOn.toCharArray(); 
		char token = actingOnArray[from];
		actingOnArray[from] = 'N';
		actingOnArray[to] = token;
		return new String(actingOnArray);
	}

	@Override
	public char getAction() {
		return 'M';
	}

}
