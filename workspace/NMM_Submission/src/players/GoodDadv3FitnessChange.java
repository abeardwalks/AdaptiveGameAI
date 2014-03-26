package players;

import interfaces.BoardFacadeInterface;
import interfaces.BoardViewInterface;
import interfaces.IntPairInterface;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import model.Phase;
import model.board.BoardModel;
import move.AbstractMove;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;
import utility.MoveChecker;

public class GoodDadv3FitnessChange extends AbstractPlayer {

	private BoardFacadeInterface workingGame;
	private MoveChecker mc;
	private TreeNode root;
	private TreeNode[] playedChildred;
	private double opponentFitnessPlayerTwo = 15.0;
	private double opponentFitnessPlayerOne = 18.0;
	private double fitness = 10.0;
	private TreeNode opponentsPlay;
	private double[] convertedScores;
	
	@Override
	public int placeToken(BoardViewInterface game) {
		workingGame = new BoardModel(game.getState(), 
									 game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
									 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
									 game.getPhase(), game.getTurn(), game.getNextAction());
		setPlayedChilded();
		recalculatePlayerFitness();
		AbstractMove m = getMove('P');
		return ((PlacementMove)m).getPlacementIndex();
	}

	@Override
	public int removeToken(BoardViewInterface game) {
		workingGame = new BoardModel(game.getState(), 
				 game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
				 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
				 game.getPhase(), game.getTurn(), game.getNextAction());
		setPlayedChilded();
		recalculatePlayerFitness();
		AbstractMove m = getMove('R');
		return ((RemovalMove)m).getRemovalIndex();
	}

	@Override
	public IntPairInterface moveToken(BoardViewInterface game) {
		workingGame = new BoardModel(game.getState(), 
				 game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
				 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
				 game.getPhase(), game.getTurn(), game.getNextAction());
	
		setPlayedChilded();
		recalculatePlayerFitness();
		AbstractMove m = getMove('M');
		IntPairInterface pair = new IntPair(((MovementMove)m).getFrom(),((MovementMove)m).getTo());
		return pair;
	}
	
	private void setPlayedChilded() {
		if(playedChildred != null){
			boolean found = false;
			int i = 0;
			while(!found && i < playedChildred.length){
				if(playedChildred[i].move.getStatePostAction().equals(workingGame.getState())){
					found = true;
					opponentsPlay = playedChildred[i];
				}
				i++;
			}
			if(opponentsPlay != null && found && opponentsPlay.nVisits != 0){
//				System.out.println( "Opponent Played: " + "Rewards: " + opponentsPlay.rewards[0] + " | " + opponentsPlay.rewards[1] + ", after " + opponentsPlay.nVisits + " visits.");
				
				long stop = System.currentTimeMillis() + 1000;
				
				while(System.currentTimeMillis() < stop){		//while stop time not reached
					mc = new MoveChecker(workingGame);
					opponentsPlay.selectAction();						//begin searching.
				}

				
//				System.out.println( "AFTER SEARCH: Opponent Played: " + "Rewards: " + opponentsPlay.rewards[0] + " | " + opponentsPlay.rewards[1] + ", after " + opponentsPlay.nVisits + " visits.");
			}
		}
	}
	
	private void recalculatePlayerFitness(){
		int opponentID = 1;
		if(getPlayerID() == 1){
			opponentID = 2;
		}
		
		if(getPlayerID() == 2){
			if(opponentsPlay != null){
	//			if(Math.max(opponentScore, ourScore) - Math.min(opponentScore, ourScore) > 15.0){
					if(opponentsPlay.rewards[opponentID - 1] > opponentsPlay.rewards[getPlayerID() -1] && opponentFitnessPlayerOne < 20.0){
						opponentFitnessPlayerOne += 1.0;
					}else if(opponentsPlay.rewards[opponentID - 1] < opponentsPlay.rewards[getPlayerID() -1] && opponentFitnessPlayerOne > 5.0){
						opponentFitnessPlayerOne -= 1.0;
					}
	//			}
			}else if(opponentFitnessPlayerOne < 15.0){
				opponentFitnessPlayerOne += 1.0;
			}
//			System.out.println("Opponent Fitness: " + opponentFitnessPlayerOne);
		}else{
			if(opponentsPlay != null){
				
	//			if(Math.max(opponentScore, ourScore) - Math.min(opponentScore, ourScore) > 15.0){
					if(opponentsPlay.rewards[opponentID - 1] > opponentsPlay.rewards[getPlayerID() -1] && opponentFitnessPlayerTwo < 20.0){
						opponentFitnessPlayerTwo += 1.0;
					}else if(opponentsPlay.rewards[opponentID - 1] < opponentsPlay.rewards[getPlayerID() -1] && opponentFitnessPlayerTwo > 5.0){
						opponentFitnessPlayerTwo -= 1.0;
					}
	//			}
			}else if(opponentFitnessPlayerTwo < 15.0){
				opponentFitnessPlayerTwo += 1.0;
			}
//			System.out.println("Opponent Fitness: " + opponentFitnessPlayerTwo);
		}
		
//		System.out.println("     Our Fitness: " + fitness);
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
		mc = new MoveChecker(workingGame);			//initialises the MoveChecker, a utility class for checking move validity. 
		
		List<AbstractMove> moves = mc.getAllPossibleMoves(action, getTokenColour());	//Gets all possible moves from the given action. 
		
		if(moves.size() == 1){		//if there is only possible one move...
			return moves.get(0);	//...return it. 
		}
		
		//Construct the tree's root move. 
		AbstractMove m = null;
		if(action == 'P'){										
			m = new PlacementMove(workingGame.getState(), getTokenColour(), -1);
		}else if(action == 'R'){
			m = new RemovalMove(workingGame.getState(), getTokenColour(), -1);
		}else if(action == 'M'){
			m = new MovementMove(workingGame.getState(), getTokenColour(), -1, -1);
		}
		root = new TreeNode(m, 0);
		
		long stop = System.currentTimeMillis() + 5000;		//Search for a set period of time. 
		
		while(System.currentTimeMillis() < stop){		//while stop time not reached
			mc = new MoveChecker(workingGame);
			root.selectAction();						//begin searching.
		}
		
		
		convertToRange();
		
		int index = selectAppropriatePlay();
		if(getPlayerID() == 2){
			if(index == -1 || (opponentFitnessPlayerOne < 16.0 && opponentFitnessPlayerOne > 9.0)){
				index = secondBest();
			}
		}else{
			if(index == -1 || (opponentFitnessPlayerTwo < 16.0 && opponentFitnessPlayerTwo > 9.0)){
				index = secondBest();
			}
		}
		if(root.children[index].children != null){
			playedChildred = root.children[index].children.clone();
		}
		return root.children[index].move;
		
	}
	
	private int selectAppropriatePlay(){
		if(getPlayerID() == 2){
			if(playedChildred != null){
				if(fitness <= opponentFitnessPlayerOne){
					return selectNearestHighMatch();
				}else{
					return selectNearestLowMatch();
				}
			}
		}else{
			if(playedChildred != null){
				if(fitness <= opponentFitnessPlayerTwo){
					return selectNearestHighMatch();
				}else{
					return selectNearestLowMatch();
				}
			}
		}
		return -1;
	}
	
	private int selectNearestHighMatch(){
		double bestValue = Double.NEGATIVE_INFINITY;
		int count = 0;
		int index = 0;
		for(TreeNode child : root.children){
			double value = child.rewards[getPlayerID() - 1 ] / child.nVisits;
			if(value > bestValue){
				bestValue = value;
				index = count;
			}
			count++;
		}
		return index;
	}
	
	private int selectNearestLowMatch(){
		double goal = 0.6;
		double closest = Math.abs(convertedScores[0] - goal);
		int secondBest = 0;
		for(int i = 1; i < convertedScores.length; i++){
		    double distance = Math.abs(convertedScores[i] - goal);
		    if(closest > distance){
		        secondBest = i;
		        closest = distance;
		    }
		}
		return secondBest;
	}
	
	private int secondBest(){
		double goal = 0.75;
		double closest = Math.abs(convertedScores[0] - goal);
		int secondBest = 0;
		for(int i = 1; i < convertedScores.length; i++){
		    double distance = Math.abs(convertedScores[i] - goal);
		    if(closest > distance){
		        secondBest = i;
		        closest = distance;
		    }
		}
		return secondBest;
	}
	

	private void convertToRange() {
		convertedScores = new double[root.children.length];
		
		double minValue = findMinScore();
		double maxValue = findMaxScore();
		
		for (int i = 0; i < root.children.length; i++) {
			convertedScores[i] = ((root.children[i].rewards[getPlayerID() - 1 ] / root.children[i].nVisits) - minValue)/(maxValue - minValue) * (1.0 - 0.0) + 0.0;
		}
	}
	
	private double findMaxScore(){
		double maxScore = root.children[0].rewards[getPlayerID() - 1] / root.children[0].nVisits;
		for (int i = 1; i < root.children.length; i++) {
			if((root.children[i].rewards[getPlayerID() - 1] / root.children[i].nVisits)> maxScore){
				maxScore = root.children[i].rewards[getPlayerID() - 1] / root.children[i].nVisits;
			}
		}
		return maxScore;
	}
	
	private double findMinScore(){
		double minScore = root.children[0].rewards[getPlayerID() - 1] / root.children[0].nVisits;
		for (int i = 1; i < root.children.length; i++) {
			if((root.children[i].rewards[getPlayerID() - 1] / root.children[i].nVisits) < minScore){
				minScore = root.children[i].rewards[getPlayerID() - 1] / root.children[i].nVisits;
			}
		}
		return minScore;
	}


	@Override
	public String getName() {
		return "Dad v3";
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
			
			while(!current.isLeaf() && !workingGame.gameWon()){					
				current = current.select();
				try{
					AbstractMove result = mc.executeMove(current.move);			//Executes the move
					workingGame.executeMove(current.move);
					if(result != null){							
						if(result.getPlayerID() != current.move.getPlayerID()){	
							workingGame.setTurn();								//sets the turn of the game depending of executing the move.
						}
					}
					workingGame.setPhase(mc.getPhase());
					
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
				rewards = rollOut(newNode);				//...Rollout the game after this move.
			}
			
			for (TreeNode node : visited) {				//for each node visited...
				workingGame.undo();						//...undo the move...
				
				node.updateStats(rewards);				//...update the rewards of the node.
			}
			
			mc = new MoveChecker(workingGame);			//re-initialise the Move Checker to ensure it reflects the game.
			workingGame.setPhase(mc.getPhase());
			this.updateStats(rewards);					
		}

		/**
		 * Expands this node, to discover its children.  
		 */
		private void expand() {
			
			List<AbstractMove> moves;
			
			if(depth == 0){
				moves = mc.getAllPossibleMoves(this.move.getAction(), this.move.getPlayerColour());		//if this is the root node, get all possible moves based on its move...
			}else{
				moves = mc.getAllPossibleMoves(mc.getNewAction(), mc.getNewPlayerTurn());		//...otherwise execute what the next action should be.
			}

			children = new TreeNode[moves.size()];
			
			if(moves.size() == 0){			//if there were no children the has been an error. 
				System.out.println("Expanding Error, Depth: " + depth);
				if(depth == 0){
					System.out.println("Tried to expand with the action: " + this.move.getAction() + ", turn was: " + this.move.getPlayerColour());
				}else{
					System.out.println("Tried to expand with the action: " + mc.getNewAction() + ", turn was: " + mc.getNewPlayerTurn());
				}
				workingGame.printDetails();
				mc.printDetails();
				System.err.println("Error expanding " + moves.size() + " children. Was the game won? " + mc.gameWon());
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
			if(children.length == 0){
				System.err.println("NO Children daniel son");
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
		private double[] rollOut(TreeNode node) {
			
			int count = 0;
			
			mc = new MoveChecker(workingGame);			//ensure the move checker is up to date.
			List<AbstractMove> moves = mc.getAllPossibleMoves(node.move.getAction(), node.move.getPlayerColour());		//get all possible moves.
			
			while(!workingGame.gameWon()){		//while the game has not been won.
				if(moves.size() == 0){
					break;
				}
				AbstractMove move = moves.get(r.nextInt(moves.size()));		//get a move at random...
				AbstractMove nextAction = mc.executeMove(move);				//...execute it on the Move Checker...
				workingGame.executeMove(move);								//...and the game.
				mc = new MoveChecker(workingGame);							//update the move checker. 
				if(workingGame.gameWon()){		//check if won...
					count++;					
					break;						//...break if won.
				}
				if(nextAction != null){			//if the next move is not null...
					if(nextAction.getPlayerID() != move.getPlayerID()){		
						workingGame.setTurn();								//set the turn if necessary.
					}
					workingGame.setPhase(mc.getPhase());					//set the phase.
					if(mc.getPhase() == Phase.FOUR){
						workingGame.setTrappedPlayer(mc.trappedPlayer());	//determine which player is trapped.
					}
					moves = mc.getAllPossibleMoves(nextAction.getAction(), nextAction.getPlayerColour());	//get all possible next moves.
				}
				count++;
				if(workingGame.getPlayerOneRemaining() == 3 || workingGame.getPlayerTwoRemaining() == 3){	//if the game is a chase state, break.
					break;
				}
			}
			
			double[] rewards = workingGame.getRewards();	//update the rewards after the rollout.

			for (int i = 0; i < count; i++) {		//for each randomly selected move...
				workingGame.undo();					//...undo it.
			}
			mc = new MoveChecker(workingGame);		//update the move checker.
			workingGame.setPhase(mc.getPhase());
			
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
		root = null;
		playedChildred = null;
		opponentFitnessPlayerTwo = 9.0;
		fitness = 10.0;
		opponentsPlay = null;
	}

}