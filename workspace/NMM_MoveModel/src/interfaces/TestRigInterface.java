package interfaces;

public interface TestRigInterface extends BoardControllerInterface {

	int getPlayerOneWins();
	int getPlayerTwoWins();
	int getNumberOfGamesToPlay();
	int getNumberOfGamesPlayed();
	
	void setNumberOfGamesToPlay(int numberOfGamesToPlay);
	
}
