package interfaces;

import model.Phase;
import move.AbstractMove;

/**
 * Interface to allow the controller to modify the game state. It decouples it 
 * from the rest of the program and hides methods not required by the controller.
 * 
 * @author Andrew White - 200939787, BSc software Engineering
 *
 */
public interface BoardMutatorInterface {
	
	/**
	 * Executes the passed in move.
	 * 
	 * @param move - The Move to be excuted on the game.
	 */
	void executeMove(AbstractMove move);
	
	/**
	 * Undoes the last move excuted on the game.
	 */
	void undo();
	
	/**
	 * Changes whos turn it is.
	 */
	void setTurn();
	
	/**
	 * @param phase - the new phase of the game.
	 */
	void setPhase(Phase phase);
	
	/**
	 * Resets the entire state of the game. 
	 */
	void reset();

}