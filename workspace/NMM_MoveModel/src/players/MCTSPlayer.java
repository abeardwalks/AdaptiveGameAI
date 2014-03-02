package players;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import controller.MoveChecker;
import model.Phase;
import model.board.BoardModel;
import move.AbstractMove;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;
import interfaces.BoardDetailsInterface;
import interfaces.BoardFacadeInterface;
import interfaces.IntPairInterface;

public class MCTSPlayer extends AbstractPlayer {

	private BoardFacadeInterface workingGame;
	private MoveChecker mc;
	private TreeNode root;
	private int numberOfRollouts = 0;
	
	@Override
	public int placeToken(BoardDetailsInterface game) {
		workingGame = new BoardModel(game.getState(), 
									 game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
									 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
									 game.getPhase(), game.getTurn());
		
		AbstractMove m = getMove('P');
		return ((PlacementMove)m).getPlacementIndex();
	}

	@Override
	public int removeToken(BoardDetailsInterface game) {
		workingGame = new BoardModel(game.getState(), 
				 game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
				 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
				 game.getPhase(), game.getTurn());
		AbstractMove m = getMove('R');
		return ((RemovalMove)m).getRemovalIndex();
	}

	@Override
	public IntPairInterface moveToken(BoardDetailsInterface game) {
		workingGame = new BoardModel(game.getState(), 
				 game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
				 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
				 game.getPhase(), game.getTurn());
		AbstractMove m = getMove('M');
		IntPairInterface pair = new IntPair(((MovementMove)m).getFrom(),((MovementMove)m).getTo());
		return pair;
	}
	
	private void sleep(){
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private AbstractMove getMove(char action){
		mc = new MoveChecker(workingGame);
		
		List<AbstractMove> moves = mc.getAllPossibleMoves(action, getTokenColour());
		
		if(moves.size() == 1){
			return moves.get(0);
		}
		AbstractMove m = null;
		if(action == 'P'){
			m = new PlacementMove(workingGame.getState(), getTokenColour(), -1);
		}else if(action == 'R'){
			m = new RemovalMove(workingGame.getState(), getTokenColour(), -1);
		}else if(action == 'M'){
			m = new MovementMove(workingGame.getState(), getTokenColour(), -1, -1);
		}
		root = new TreeNode(m, 0);
		
		long stop = System.currentTimeMillis() + 5000;
		
		while(System.currentTimeMillis() < stop){
			mc = new MoveChecker(workingGame);
			root.selectAction();
		}
		
		double bestValue = Double.NEGATIVE_INFINITY;
		AbstractMove bestMove = null;
		for(TreeNode child : root.children){
			System.out.println("Rewards: " + child.rewards[getPlayerID() - 1] + ", after " + child.nVisits + " visits.");
			
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
		return "MCTS Player";
	}
	
	private class TreeNode{
		
		private double epsilon = 1e-6;
		private Random r = new Random();
		private int depth;
		private AbstractMove expandMove;
		private AbstractMove move;
		
		TreeNode[] children;
		double nVisits;
		double[] rewards;
		
		public TreeNode(AbstractMove move, int depth){
			this.move = move;
			rewards = new double[2];
			this.depth = depth;
		}
		
		public void selectAction(){
			List<TreeNode> visited = new LinkedList<TreeNode>();
			TreeNode current = this;
			
			while(!current.isLeaf() && !workingGame.gameWon()){
				current = current.select();
				try{
					AbstractMove result = mc.executeMove(current.move);
					workingGame.executeMove(current.move);
					if(result.getPlayerID() != current.move.getPlayerID()){
						workingGame.setTurn();
					}
					workingGame.setPhase(mc.getPhase());
					
				}catch(Exception e){
					e.printStackTrace();
				}
				visited.add(current);
			}
			
			double[] rewards;
			
			if(workingGame.gameWon()){
				rewards = workingGame.getRewards();
			}else{
//				System.out.println("BEFORE ROLLOUT-------------------");
//				workingGame.printDetails();
//				mc.printDetails();
//				mc = new MoveChecker(workingGame);
			
				current.expand();
				
				TreeNode newNode = current.select();
//				workingGame.executeMove(newNode.move);
				
				rewards = rollOut(newNode);
			}
			
			for (TreeNode node : visited) {
//				System.out.println("Number of visits: " + node.nVisits + ", rewards: " + node.rewards);
				workingGame.undo();
				node.updateStats(rewards);
			}
//			System.out.println("AFTER UNDO-------------------------");
//			workingGame.printDetails();
//			mc.printDetails();
			mc = new MoveChecker(workingGame);
//			System.out.println("-----------------------------------");

			
			this.updateStats(rewards);
			
		}

		private void expand() {
			List<AbstractMove> moves;
			if(depth == 0){
				moves = mc.getAllPossibleMoves(this.move.getAction(), this.move.getPlayerColour());
			}else{
//				System.out.println(depth + " excuting move: " + move.getAction() + ", " + move.getPlayerColour());

//				mc.printDetails();
//				AbstractMove expand = mc.executeMove(move);
//				mc = new MoveChecker(workingGame);
//				if(expand == null){
//					System.err.println("It's null");
//				}
//				
//				System.err.println("Depth: " + depth + ", move is: " + move.getAction() + ", turn: " + move.getPlayerColour() + " position: " + ((PlacementMove)move).getPlacementIndex());
//				System.err.println("Expand move: " + expand);
//				System.err.println("New action was: " + mc.getNewAction() + ", new turn was: " + mc.getNewPlayerTurn());
				moves = mc.getAllPossibleMoves(mc.getNewAction(), mc.getNewPlayerTurn());
				
			}
			children = new TreeNode[moves.size()];
			if(moves.size() == 0){
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
//			if(depth == 2){
//				System.out.println(expandMove.getAction() + "    " + expandMove.getPlayerColour());
//				System.out.println(moves.get(0).getAction() + "  " + moves.get(0).getPlayerColour());
//				System.exit(0);
//			}
			
			for(AbstractMove m : moves){
				children[i] = new TreeNode(m, depth + 1);
				i++;
			}
		}

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

		private boolean isLeaf() {
			return (children == null);
		}
		
		private double[] rollOut(TreeNode node) {
			int count = 0;
			
			mc = new MoveChecker(workingGame);
			List<AbstractMove> moves = mc.getAllPossibleMoves(node.move.getAction(), node.move.getPlayerColour());
			
//			System.out.println("Before Rollout....");
//			workingGame.printDetails();
//			mc.printDetails();
//			System.out.println("Starting Rollout " + numberOfRollouts);
			numberOfRollouts++;
			while(!workingGame.gameWon()){
//				sleep();
//				System.out.println("looping in roolout");
				if(moves.size() == 0){
					break;
				}
				AbstractMove move = moves.get(r.nextInt(moves.size()));
//				System.out.println();
//				System.out.println("Executing action: " + move.getAction() + " by player " + move.getPlayerColour());
				AbstractMove nextAction = mc.executeMove(move);
				workingGame.executeMove(move);
				mc = new MoveChecker(workingGame);
				if(workingGame.gameWon()){
					count++;
					break;
				}
//				workingGame.printDetails();
//				mc.printDetails();
				if(nextAction != null){
//					System.out.println("Next action: " + nextAction.getAction() + " by player " + nextAction.getPlayerColour());
					if(nextAction.getPlayerID() != move.getPlayerID()){
						workingGame.setTurn();
					}
					
					workingGame.setPhase(mc.getPhase());
					if(mc.getPhase() == Phase.FOUR){
						workingGame.setTrappedPlayer(mc.trappedPlayer());
					}
					moves = mc.getAllPossibleMoves(nextAction.getAction(), nextAction.getPlayerColour());
//					System.out.println("--------------------------------");
					
				}
				count++;
				if(workingGame.getPlayerOneRemaining() == 3 || workingGame.getPlayerTwoRemaining() == 3){
					break;
				}
//				workingGame.printDetails();

			}
			
//			System.out.println("Rollout Ended " + (numberOfRollouts - 1));
//			workingGame.printDetails();
//			mc.printDetails();
//			System.exit(0);
			double[] rewards = workingGame.getRewards();

			for (int i = 0; i < count; i++) {
				workingGame.undo();
			}
			mc = new MoveChecker(workingGame);
			workingGame.setPhase(mc.getPhase());
			
			
			return rewards;
		}
		
		private void updateStats(double[] rewards) {
			nVisits++;
//			System.out.println("Visits: " + nVisits);
			this.rewards[0] += rewards[0];
			this.rewards[1] += rewards[1];
		}
	}

}
