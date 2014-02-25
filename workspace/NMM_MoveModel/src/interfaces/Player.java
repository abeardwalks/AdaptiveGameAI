package interfaces;

public interface Player {
	
	public int placeToken(BoardDetailsInterface game);

	public int removeToken(BoardDetailsInterface game);

	public IntPairInterface moveToken(BoardDetailsInterface state);

	public String getName();

	public void setTokenColour(char tokenColour);

	public char getTokenColour();
	
	public int getPlayerID();

}
