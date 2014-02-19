package players;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Stack;

import controller.MoveChecker;

import model.Phase;
import model.board.BoardDetails;

import players.MCTS.Move;

import interfaces.IntPairInterface;
import interfaces.PlayerInterface;

public class MCTSAI implements PlayerInterface, Observer {
	
	private BoardDetails bd;
	private TreeNode root;
	private char color;

	@Override
	public int placeToken(String state) {
		Move m = getMove('P');
		return m.getPlacementIndex();
	}

	@Override
	public int removeToken(String state) {
		Move m = getMove('R');
		return m.getRemovalIndex();
	}

	@Override
	public IntPairInterface moveToken(String state) {
		Move m = getMove('M');
		return m.getMovementIndexs();
	}
	
	private Move getMove(char action){
		MoveChecker mc = new MoveChecker();
		
		List<Move> moves = mc.getAllPossibleMoves(action, getTokenColour(), bd);
		
		if(moves.size() == 1){
			return moves.get(0);
		}
		
		root = new TreeNode(null, new MCTSGame(bd));
		
		long stop = System.currentTimeMillis() + 1000;
		
		while(System.currentTimeMillis() < stop){
			root.selectAction();
		}
		
		double bestValue = Double.NEGATIVE_INFINITY;
		Move bestMove = null;
		for(TreeNode child : root.children){
			double value = child.rewards[getPlayerID() - 1] / child.nVisits;
			if(value > bestValue){
				bestValue = value;
				bestMove = child.move;
			}
		}
		
		return bestMove;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTokenColour(char c) {
		color = c;
	}

	@Override
	public char getTokenColour() {
		return color;
	}
	
	public int getPlayerID(){
		if(color == 'R'){
			return 1;
		}else{
			return 2;
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof BoardDetails){
			bd = (BoardDetails) arg1;
		}
	}
	
	private class TreeNode{
		
		private MCTSGame game;
		private Random r;
		private Move move;
		private double epsilon;
		private MoveChecker moveCheck;
		
		TreeNode[] children;
		double nVisits;
		double[] rewards;
		
		public TreeNode(Move move, MCTSGame game){
			this.move = move;
			this.game = game;
			
			epsilon = 1e-6;
			
			rewards = new double[2];
			
			moveCheck = new MoveChecker();
		}
		
		public void selectAction(){
			List<TreeNode> visited = new LinkedList<TreeNode>();
			TreeNode current = this;
			
			while(!current.isLeaf() && !game.isWon()){
				current = current.select();
				try{
					game.makeMove(current.move);
				} catch (Exception e){
					e.printStackTrace();
				}
				visited.add(current);
			}
			
			double[] rewards;
			if(game.isWon()){
				rewards = game.getRewards();
			} else {
				current.expand();
				TreeNode newNode = current.select();
				game.makeMove(newNode.move);
				visited.add(newNode);
				rewards = rollOut(newNode);
			}
			
			for(TreeNode node : visited){
				game.undo();
				node.updateStats(rewards);
			}
			this.updateStats(rewards);
			
		}


		private double[] rollOut(TreeNode newNode) {
			// TODO Auto-generated method stub
			return null;
		}

		private void expand() {
			char action = this.move.getAction();
			char turn = this.move.getTurn();
			List<Move> moves = moveCheck.getAllPossibleMoves(action, turn, game.getDetails());
			children = new TreeNode[moves.size()];
			if(moves.size() == 0){
				System.out.println("Error expanding " + moves.size() + " children");
			}
			int i = 0;
			for (Move move : moves) {
				children[i] = new TreeNode(move, game);
				i++;
			}
		}

		private TreeNode select() {
			TreeNode selected = children[0];
			double bestValue = Double.NEGATIVE_INFINITY;
			if(children.length == 0){
				System.out.println("No Children to select");
			}
			
			for (TreeNode c : children) {
				double uctValue = c.rewards[game.getPlayerID()-1]
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
		
		private void updateStats(double[] rewards) {
			nVisits++;
			for(int i = 0 ; i < rewards.length ; i++) {
				this.rewards[i] += rewards[i];
			}
		}
	}
	
	private class MCTSGame{
		
		private String state;
		private Stack<Move> history;
		private char turn;
		private int playerOneTokensToPlace, playerTwoTokensToPlace;
		private int playerOneTokensRemaining, playerTwoTokensRemaining;
		private Phase phase;
		
		public MCTSGame(String state, char turn, int playerOneTTP, int playerTwoTTP, int playerOneTR, int playerTwoTR, Phase phase){
			
			this.state = state;
			this.turn = turn;
			
			playerOneTokensToPlace = playerOneTTP;
			playerTwoTokensToPlace = playerTwoTTP;
			playerOneTokensRemaining = playerOneTR;
			playerTwoTokensRemaining = playerTwoTR;
			
			this.phase = phase;
			
			history = new Stack<Move>();
		}
		
		public void undo() {
			// TODO Auto-generated method stub
			
		}

		public double[] getRewards() {
			// TODO Auto-generated method stub
			return null;
		}

		public void makeMove(Move move) {
			// TODO Auto-generated method stub
			
		}

		public boolean isWon() {
			// TODO Auto-generated method stub
			return false;
		}
		
		public int getPlayerID(){
			if(turn == 'R'){
				return 1;
			}else{
				return 2;
			}
		}

		public MCTSGame(BoardDetails bd){
			state = bd.getGS();
			turn = bd.getTurn();
			playerOneTokensToPlace = bd.getPlayerOneToPlace();
			playerTwoTokensToPlace = bd.getPlayerTwoToPlace();
			playerOneTokensRemaining = bd.getPlayerOneRemaining();
			playerTwoTokensRemaining = bd.getPlayerTwoRemaining();
			phase = bd.getPhase();
		}
		
		public BoardDetails getDetails(){
			return new BoardDetails(state, -2, playerOneTokensRemaining, playerTwoTokensRemaining, playerOneTokensToPlace, playerTwoTokensToPlace, turn, phase);
		}
		
	}

}
