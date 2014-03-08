package utility;

import java.util.List;

import move.AbstractMove;

public interface MoveUtilityInterface {

	/**
	 * Checkes a placement of a token to the board, returning the result of the placement.
	 * 
	 * @param tokenColour - The colour of the token to be placed.
	 * @param    position - The index (0-23) that the token is to be placed.
	 * @return - The success of the move: -1 - Illegal placement.
	 * 									   0 - Valid Placement.
	 * 									   1 - Valid Placement, Mill Created.
	 * 									   2 - Valid Placement, Game Won. 
	 */
	public abstract int placeToken(char tokenColour, int position);

	/**
	 * Checks the removal of a token from the board, returning the result of the removal.
	 * 
	 * @param tokenColour - The colour of the player removing the token.
	 * @param    position - The index (0-23) that the token is to be removed from.
	 * @return - The success of the move: -1 - Illegal Removal.
	 * 									   0 - Valid Removal.
	 * 									   2 - Valid Removal, Game Won. 
	 */
	public abstract int removeToken(char token, int position);

	/**
	 * Checks the move of a token on the board, returning the result of the movement.
	 * 
	 * @param token - The colour of the player moving the token.
	 * @param  from - The position the token is moving from.
	 * @param    to - The position the token is moving to.
	 * @return - The success of the move: -1 - Illegal movement.
	 * 									   0 - Valid movement.
	 * 									   1 - Valid movement, Mill Created.
	 * 									   2 - Valid movement, Game Won. 
	 */
	public abstract int moveToken(char token, int from, int to);

	public abstract char trappedPlayer();

	/**
	 * Checks if the position passed in against all other positions that could
	 * be a Mill.
	 * 
	 * @param position - The Position to check (Between 0-23). 
	 * @return - True if it is part of a mill, false otherwise. 
	 */
	public abstract boolean partOfMill(int position);

	/**
	 * Executes a move on the Move Checker, this (and the next methods) are all used
	 * by the MCTS Player(s). 
	 * 
	 * @param  move - the move to execute.
	 * @return      - the next type of move to be executed based on the result of this move. 
	 */
	public abstract AbstractMove executeMove(AbstractMove move);

	public abstract char getNewAction();

	public abstract char getNewPlayerTurn();

	/**
	 * Determines all legal moves available, based on an action and player colour. 
	 * 
	 * @param      action - The action to find all the moves from. 
	 * @param tokenColour - The colour of the token related to the action.
	 * @return 			  - The list of possible moves. 
	 */
	public abstract List<AbstractMove> getAllPossibleMoves(char action,
			char tokenColour);

}