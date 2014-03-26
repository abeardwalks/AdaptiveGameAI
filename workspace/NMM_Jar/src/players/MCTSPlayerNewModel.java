package players;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import model.board.Model;
import move.AbstractMove;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;
import interfaces.BoardViewInterface;
import interfaces.BoardFacadeInterface;
import interfaces.IntPairInterface;


/**
 * MCTS Player (Monte-Carlo Treesearch Player). This AI makes use the Monte-Carlo search technique.
 * It carries out numerous rollouts to ascertain which move is the best. This AI is the hardest to have 
 * been produced and has been successful in beating me. 
 * 
 * TODO: Adjust the AI to improve its play style during the chase phase of the game. 
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 *
 */
public class MCTSPlayerNewModel extends AbstractPlayer {

	private BoardFacadeInterface workingGame;
	private TreeNode root;
	
	@Override
	public int placeToken(BoardViewInterface game) {
		workingGame = new Model(game.getState(), game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
														 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
														 game.getPhase(), game.getTurn(), 
														 game.millMade(), game.getNextAction());
		
		AbstractMove m = getMove('P');
		return ((PlacementMove)m).getPlacementIndex();
	}

	@Override
	public int removeToken(BoardViewInterface game) {
		workingGame = new Model(game.getState(), game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
				 										 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
				 										 game.getPhase(), game.getTurn(), 
				 										 game.millMade(), game.getNextAction());
		AbstractMove m = getMove('R');
		return ((RemovalMove)m).getRemovalIndex();
	}

	@Override
	public IntPairInterface moveToken(BoardViewInterface game) {
		workingGame = new Model(game.getState(), game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
				 										 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
				 										 game.getPhase(), game.getTurn(), 
				 										 game.millMade(), game.getNextAction());
		AbstractMove m = getMove('M');
		IntPairInterface pair = new IntPair(((MovementMove)m).getFrom(),((MovementMove)m).getTo());
		
		return pair;
	}
	
	/**
	 * This method setups up the tree and move checker to begin the MCTS. It also calculates
	 * out of the possible moves available which is the best to place.
	 * 
	 * @param action - the type of move to search for, P - Placement.
	 * 												   R - Removal.
	 * 												   M - Movement.
	 * @return - the best move found. 
	 */
	private AbstractMove getMove(char action){
		List<AbstractMove> moves = workingGame.getAllPossibleMoves();	//Gets all possible moves from the given action. 
		
		if(moves.size() == 1){		//if there is only possible one move...
			return moves.get(0);	//...return it. 
		}
		
		root = new TreeNode(null, 0);
		
		long stop = System.currentTimeMillis() + 5000;		//Search for a set period of time. 
		
		while(System.currentTimeMillis() < stop){		//while stop time not reached
			root.selectAction();						//begin searching.
		}
		
		//Determine the best move from the executed search. 
		double bestValue = Double.NEGATIVE_INFINITY;
		AbstractMove bestMove = null;
		for(TreeNode child : root.children){
//			System.out.println("Rewards: " + child.rewards[0] + " | " + child.rewards[1] + ", after " + child.nVisits + " visits.");
			
			double value = child.rewards[getPlayerID() - 1 ] / child.nVisits;
			if(value > bestValue){
				bestValue = value;
				bestMove = child.move;
			}
		}
		return bestMove;
	}

	@Override
	public String getName() {
		return "MCTS";
	}
	
	/**
	 * The Tree structure class that handles the MCTS rollout and move scoring. It was based on
	 * the MCTS tree structure developed by Phil Rodgers. 
	 * 
	 * @author Andrew White - BSc Software Engineering, 200939787
	 *
	 */
	private class TreeNode{
		
		private double epsilon = 1e-6;		//weight for calculating the UCT value.
		private Random r = new Random();	//Random generator for selecting the moves for the rollouts. 
		private int depth;					//The depth of this node.
		private AbstractMove move;			//The move this node executes on. 
		
		TreeNode[] children;				//The children of this node.
		double nVisits;						//The number of times this node has been visited.
		double[] rewards;					//The rewards that each player could expect from this move. 
		
		public TreeNode(AbstractMove move, int depth){
			this.move = move;
			rewards = new double[2];
			this.depth = depth;
		}
		
		/**
		 * Starts searching the tree for strong and correct moves. 
		 */
		public void selectAction(){
			
			List<TreeNode> visited = new LinkedList<TreeNode>();				//The list of nodes already visited.
			TreeNode current = this;
			
			while(!current.isLeaf()){					
				current = current.select();
				try{
					workingGame.executeMove(current.move);
				}catch(Exception e){
					e.printStackTrace();
				}
				visited.add(current);			//adds the processed node to the visited list. 
			}
			
			double[] rewards;
			
			if(workingGame.gameWon()){					//if the game is won...
				rewards = workingGame.getRewards();		//...get its rewards...
			}else{										//...otherwise...
				current.expand();						//expand this node...
				TreeNode newNode = current.select();	//...select a child node...
				workingGame.executeMove(newNode.move);
				visited.add(newNode);
				rewards = rollOut();				//...Rollout the game after this move.
			}
			
			for (TreeNode node : visited) {				//for each node visited...
				workingGame.undo();						//...undo the move...
				node.updateStats(rewards);				//...update the rewards of the node.
			}
			
			this.updateStats(rewards);					
		}

		/**
		 * Expands this node, to discover its children.  
		 */
		private void expand() {
			
			List<AbstractMove> moves = workingGame.getAllPossibleMoves();

			children = new TreeNode[moves.size()];
			
			if(moves.size() == 0){			//if there were no children the has been an error. 
				System.out.println("Expanding Error, Depth: " + depth);
				System.out.println("Next Action Was: " + workingGame.getNextAction());
				System.out.println("Turn was: " + workingGame.getTurn());
				System.out.println("Game Phase: " + workingGame.getPhase());
				workingGame.printDetails();
				
				System.out.println("Error expanding " + moves.size() + " children. Was the game won? " + workingGame.gameWon());
			}
			if(moves.get(0).getAction() != workingGame.getNextAction()){
				System.err.println("Error expanding, new moves do not match required action!");
			}
			
			int i = 0;
			for(AbstractMove m : moves){					//for each new move...
				children[i] = new TreeNode(m, depth + 1);	//create a Tree Node.
				i++;
			}
		}

		/**
		 * Selects the next tree node to select for expanding based on its Upper Confidence Bound
		 * 
		 * @return
		 */
		private TreeNode select() {
			TreeNode selected = children[0];
			double bestValue = Double.NEGATIVE_INFINITY;
			if (children.length == 0) {
				System.out.println("NO children to select daniel son");
			}
			
			for (TreeNode c : children) {
				double uctValue = c.rewards[getPlayerID()-1]
						/ (c.nVisits + epsilon)
						+ Math.sqrt(Math.log(nVisits + 1)
								/ (c.nVisits + epsilon)) + r.nextDouble()
						* epsilon;
				if (uctValue > bestValue) {
					selected = c;
					bestValue = uctValue;
				}
			}
			return selected;
		}

		/**
		 * @return - True if it is a leaf, false otherwise.
		 */
		private boolean isLeaf() {
			return (children == null);
		}
		
		/**
		 * Rollout a game of NMM until it is either won or reaches a stopping state.
		 * 
		 * @param node - The node to rollout.
		 * @return - The rewards from rolling out this game.
		 */
		private double[] rollOut() {
			
			int count = 0;
			List<AbstractMove> moves = workingGame.getAllPossibleMoves();		//get all possible moves.
			
			while(!workingGame.gameWon()){		//while the game has not been won.
				AbstractMove move = moves.get(r.nextInt(moves.size()));		//get a move at random...
				workingGame.executeMove(move);
				
				moves = workingGame.getAllPossibleMoves();	//get all possible next moves.
				count++;
				if(workingGame.getPlayerOneRemaining() == 3 || workingGame.getPlayerTwoRemaining() == 3){	//if the game is a chase state, break.
					break;
				}
			}
			
			double[] rewards = workingGame.getRewards();	//update the rewards after the rollout.

			for (int i = 0; i < count; i++) {		//for each randomly selected move...
				workingGame.undo();					//...undo it.
			}
			
			return rewards;
		}
		
		/**
		 * Updates this nodes reward values.
		 * 
		 * @param rewards - The reward values to add on to the current reward values. 
		 */
		private void updateStats(double[] rewards) {
			nVisits++;
			this.rewards[0] += rewards[0];
			this.rewards[1] += rewards[1];
		}
	}

	@Override
	public void reset() {
	}
	
}
