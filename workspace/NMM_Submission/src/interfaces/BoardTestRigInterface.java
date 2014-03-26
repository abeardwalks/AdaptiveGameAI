package interfaces;

/**
 * Group of methods used by the test rig and writer class for outputting the stats from automated game play.
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 *
 */
public interface BoardTestRigInterface {
	
	/**
	 * @return - The number of games played (used for testing).
	 */
	int getGamesPlayed();
	
	/**
	 * @return - The number of games to play (used for testing).
	 */
	int getGamesToPlay();
	
	/**
	 * @return - The number of player one wins.
	 */
	int getPlayerOneWins();
	
	/**
	 * @return
	 */
	int getPlayerTwoWins();
	
	/**
	 * @param gamesToPlay - the number of games to play in total.
	 */
	void setGamesToPlay(int gamesToPlay);
	
	/**
	 * @return - the number of draws, if any.
	 */
	int getNumberOfDraws();

}
