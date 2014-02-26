package players;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import players.MCTSAI.MCTSGame;
import players.MCTSAI.TreeNode;

import controller.MoveChecker;
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
	
	@Override
	public int placeToken(BoardDetailsInterface game) {
		workingGame = new BoardModel(game.getState(), 
									 game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
									 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
									 game.getPhase(), game.getTurn());
		
		AbstractMove m = getMove('P');
		return 0;
	}

	@Override
	public int removeToken(BoardDetailsInterface game) {
		workingGame = new BoardModel(game.getState(), 
				 game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
				 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
				 game.getPhase(), game.getTurn());
		AbstractMove m = getMove('R');
		return 0;
	}

	@Override
	public IntPairInterface moveToken(BoardDetailsInterface game) {
		workingGame = new BoardModel(game.getState(), 
				 game.getPlayerOneToPlace(), game.getPlayerTwoToPlace(), 
				 game.getPlayerOneRemaining(), game.getPlayerTwoRemaining(), 
				 game.getPhase(), game.getTurn());
		AbstractMove m = getMove('M');
		return null;
	}
	
	private AbstractMove getMove(char action){
		mc = new MoveChecker(workingGame);
		
		List<AbstractMove> moves = mc.getAllPossibleMoves(action, getTokenColour());
		
		if(moves.size() == 1){
			return moves.get(0);
		}
		AbstractMove m = null;
		if(action == 'P'){
			m = new PlacementMove(workingGame.getState(), action, getTokenColour(), -1);
		}else if(action == 'R'){
			m = new RemovalMove(workingGame.getState(), action, getTokenColour(), -1);
		}else if(action == 'M'){
			m = new MovementMove(workingGame.getState(), action, getTokenColour(), -1, -1);
		}
		root = new TreeNode(m);
		
		long stop = System.currentTimeMillis() + 1000;
		
		while(System.currentTimeMillis() < stop){
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
		
		private AbstractMove move;
		
		TreeNode[] children;
		double nVisits;
		double[] rewards;
		
		public TreeNode(AbstractMove move){
			this.move = move;
			rewards = new double[2];
		}
		
		public void selectAction(){
			
		}

		private double[] rollOut(TreeNode node) {

		}

		private void expand() {

		}

		private TreeNode select() {
		
		}

		private boolean isLeaf() {
			
		}
		
		private void updateStats(double[] rewards) {
			
		}
	}

}
