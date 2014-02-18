package players;

import games.Game;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import data.Move;

public class MCTSPlayer implements Player {

	int nPlayers;
	TreeNode root;
	private int playerID;
//	private Random rng;
	
	public MCTSPlayer () {
		this.nPlayers = 2;
//		rng = new Random();
	}
	
	@Override
	public Move getMove(Game game) {
		List<Move> moves = game.getAvailableMoves();
		if(moves.size() == 1) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return moves.get(0);
		}
		root = new TreeNode(null, game);
		long stop = System.currentTimeMillis() + 1000;
		
		while(System.currentTimeMillis() < stop) {
			root.selectAction();
		}
		
		
//		for (int i = 0; i < 100000; i++) {
//			root.selectAction();
//		}
		
		double bestValue = Double.NEGATIVE_INFINITY;
		Move bestMove = null;
		System.out.println("Player " + game.playerID());
		for (TreeNode child : root.children) {
					double value = child.rewards[game.playerID()-1] / child.nVisits;
					System.out.println(child.move + " : " + value + " (" + child.nVisits + ")");
					if (value > bestValue) {
						bestValue = value;
						bestMove = child.move;
					}
			}
		System.out.println("----------------------------------------------");
		return bestMove;
	}

	private class TreeNode {
		private Game game;
		private Random r = new Random();
		private Move move;
		private double epsilon = 1e-6;

		TreeNode[] children;
		double nVisits;
		double[] rewards;

		private TreeNode(Move move, Game game) {
			this.move = move;
			this.game = game;
			rewards = new double[nPlayers];
		}

		public void selectAction() {
			List<TreeNode> visited = new LinkedList<TreeNode>();
			TreeNode cur = this;
			// visited.add(this);
			while (!cur.isLeaf() && !game.isWon()) {
				cur = cur.select();
				try {
					game.makeMove(cur.move);
				} catch (Exception e) {
					System.out.println(">>" + game);
				}
				visited.add(cur);
			}
			double[] rewards;
			if (game.isWon()) {
				rewards = game.rewards();
			} else {
				cur.expand();
				TreeNode newNode = cur.select();
				game.makeMove(newNode.move);
				visited.add(newNode);
				rewards = rollOut(newNode);
			}
			// System.out.println(visited.size());

			for (TreeNode node : visited) {
				game.undo();
				node.updateStats(rewards);
			}
			this.updateStats(rewards);
		}

		public void expand() {
			List<Move> moves = game.getAvailableMoves();
			children = new TreeNode[moves.size()];
			if (moves.size() == 0) {
				System.out.println("Error expanding " + moves.size() + " children.");
			}
			int i = 0;
			for (Move m : moves) {
				children[i] = new TreeNode(m, game);
				i++;
			}
		}

		private TreeNode select() {
			TreeNode selected = children[0];
			double bestValue = Double.NEGATIVE_INFINITY;
			if (children.length == 0) {
				System.out.println("NO children to select.");
			}
			
			for (TreeNode c : children) {
				double uctValue = c.rewards[game.playerID()-1]
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

		public boolean isLeaf() {
			return children == null;
		}

		public double[] rollOut(TreeNode tn) {
			
			//Do the rollout in the game state
			int count = 0;
			List<Move> moves = game.getAvailableMoves();
			
			while(!game.isWon()) {
				game.makeMove(moves.get(r.nextInt(moves.size())));
				count++;
				moves = game.getAvailableMoves();
			}
			
			double[] rewards = game.rewards();

			for (int i = 0; i < count; i++) {
				game.undo();
			}

			return rewards;
		}

		public void updateStats(double[] rewards) {
			nVisits++;
			for(int i = 0 ; i < rewards.length ; i++) {
				this.rewards[i] += rewards[i];
			}
		}
		//
		// public int arity() {
		// return children == null ? 0 : children.length;
		// }
	}

	@Override
	public String getName() {
		return "MCTS Player";
	}
	
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	@Override
	public int getPlayerID() {
		return playerID;
	}
}