package interfaces;

public interface BoardFacadeInterface extends BoardDetailsInterface, BoardMutatorInterface {
	
	/**
	 * Evaluates the current board state and returns the relevant scores.
	 * 
	 * @return
	 */
	double[] getRewards();
	
}
