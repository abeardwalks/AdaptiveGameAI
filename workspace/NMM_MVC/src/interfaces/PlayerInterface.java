package interfaces;

public interface PlayerInterface {
	
	int placeToken(String state);
	int removeToken(String state);
	IntPairInterface moveToken(String state);
	String getName();
	void setTokenColour(char c);
	char getTokenColour();

}
