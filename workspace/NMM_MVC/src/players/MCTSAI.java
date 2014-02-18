package players;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import model.Phase;
import model.board.BoardDetails;

import players.MCTS.Move;
import controller.MoveChecker;
import interfaces.GameStateInterface;
import interfaces.IntPairInterface;
import interfaces.MCTSInterface;
import interfaces.PlayerInterface;

public class MCTSAI implements MCTSInterface {
	
	private int nPlayers;
	private int playerID;
	private char color;
	private MoveChecker moveChecker;
	private GameStateInterface game;
	private TreeNode root;

	@Override
	public int placeToken(String state) {
		setUpMoveChecker();
		Move m = getMove('P');
		return m.getPlacementIndex();
	}

	@Override
	public int removeToken(String state) {
		setUpMoveChecker();
		Move m = getMove('R');
		return m.getRemovalIndex();
	}

	@Override
	public IntPairInterface moveToken(String state) {
		setUpMoveChecker();
		Move m = getMove('M');
		return m.getMovementIndexs();
	}
	
	private Move getMove(char action){
		setUpMoveChecker();
		List<Move> moves = moveChecker.getAvailableMoves(action, color);
		if(moves.size() == 1){
			try{
				Thread.sleep(500);
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			return moves.get(0);
		}
		root = new TreeNode(null, moveChecker);
		long stop = System.currentTimeMillis() + 1000;
		
		while(System.currentTimeMillis() < stop){
			root.selectAction();
		}
		
		double bestValue = Double.NEGATIVE_INFINITY;
		Move bestMove = null;
		for(TreeNode child : root.children){
			double value = child.rewards[moveChecker.getPlayerID()-1] / child.nVisits;
			if(value > bestValue){
				bestValue = value;
				bestMove = child.move;
			}
		}
		return bestMove;
	}

	@Override
	public String getName() {
		return "MCTS AI";
	}

	@Override
	public void setTokenColour(char c) {
		color = c;
	}

	@Override
	public char getTokenColour() {
		return color;
	}

	@Override
	public int getPlayerID() {
		return playerID;
	}

	@Override
	public void setPlayerID(int id) {
		playerID = id;
	}

	@Override
	public void initialise(GameStateInterface game) {
		System.out.println("initialised MCTS");
		this.game = game;
		moveChecker = new MoveChecker();
	}
	
	public void setUpMoveChecker(){
		BoardDetails bd = new BoardDetails(game.getState(), game.getResult(), game.getPlayerOneTokensRemaining(), 
												game.getPlayerTwoTokensRemaining(), game.getPlayerOneTokensToPlace(), game.getPlayerTwoTokensToPlace(), game.getTurn(), game.getPhase());
		moveChecker.setDetails(bd);
		nPlayers = 2;
	}
	
	private class TreeNode {
		private Random r = new Random();
		private Move move;
		private double epsilon = 1e-6;
		private MoveChecker moveCheck;
		
		TreeNode[] children;
		double nVisits;
		double[] rewards;
		
		private TreeNode(Move move, MoveChecker mc){
			this.move = move;
			rewards = new double[nPlayers];
			moveCheck = mc;
		}
		
		public void selectAction(){
			List<TreeNode> visited = new LinkedList<TreeNode>();
			TreeNode current = this;
			moveCheck.preserveCurrentState();
			while(!current.isLeaf() && !moveCheck.gameWon()){
				current = current.select();
				try{
					Move m = current.move;
					int result = -2;
					switch (m.getAction()) {
					case 'P':
						result = moveCheck.addToken(m.getPlayer(), m.getPlacementIndex());
						m.setResult(result);
						break;
					case 'M':
						IntPairInterface movement = m.getMovementIndexs();
						result = moveCheck.moveToken(m.getPlayer(), movement.getFirstInt(), movement.getSecondInt());
						m.setResult(result);
						break;
					case 'R':
						result = moveCheck.removeToken(m.getPlayer(), m.getRemovalIndex());
						m.setResult(result);
						break;
					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				visited.add(current);
			}
			double[] rewards;
			if(moveCheck.gameWon()){
				rewards = moveCheck.getRewards();
			}else{
				current.expand();
				TreeNode newNode = current.select();
//				Move m = newNode.move;
//				int result = -2;
//				switch (m.getAction()) {
//				case 'P':
//					result = moveCheck.addToken(m.getPlayer(), m.getPlacementIndex());
//					m.setResult(result);
//					break;
//				case 'M':
//					IntPairInterface movement = m.getMovementIndexs();
//					result = moveCheck.moveToken(m.getPlayer(), movement.getFirstInt(), movement.getSecondInt());
//					m.setResult(result);
//					break;
//				case 'R':
//					result = moveCheck.removeToken(m.getPlayer(), m.getRemovalIndex());
//					m.setResult(result);
//					break;
//				default:
//					break;
//				}
				visited.add(newNode);
				rewards = rollOut(newNode);
			}
//			moveCheck.returnToPreservedState();
			for(TreeNode node : visited){
				node.updateStats(rewards);
			}
			this.updateStats(rewards);
		}
		
		public void expand() {
			char action = 'P';
			char turn = getTokenColour();
			if(this.move != null){
				int result = this.move.getResult();
				turn = this.move.getPlayer();
				action = 'P';
				if(result == 0){
					if(this.move.getPlayer() == 'R'){
						turn = 'B';
					}else{
						turn = 'R';
					}
					if(moveCheck.getPhase() == Phase.TWO || moveCheck.getPhase() == Phase.THREE){
						action = 'M';
					}
				}else if(result == 1){
					action = 'R';
				}
			}
			List<Move> moves = moveCheck.getAvailableMoves(action, turn);
			children = new TreeNode[moves.size()];
			if(moves.size() == 0){
				System.out.println("Error expanding! ---- moves is empty");
			}
			int i = 0;
			for (Move m : moves){
				children[i] = new TreeNode(m, moveCheck);
				i++;
			}
		}
		
		private TreeNode select(){
			TreeNode selected = children[0];
			double bestValue = Double.NEGATIVE_INFINITY;
			if(children.length == 0){
				System.out.println("No chilren to select");
			}
			
			for(TreeNode c : children){
				double uctValue = c.rewards[moveCheck.getPlayerID()-1] 
						/ (c.nVisits + epsilon) 
						+ Math.sqrt(Math.log(nVisits + 1)
								/ (c.nVisits + epsilon)) + r.nextDouble()
				        * epsilon;
				if(uctValue > bestValue){
					selected = c;
					bestValue = uctValue;
				}
			}
			
			return selected;
		}
		
		public boolean isLeaf(){
			return (children == null);
		}
		
		public double[] rollOut(TreeNode tn){
			System.out.println("NEW ROLLOUT - " + moveCheck.getState());
			int count = 0;
			Move m = tn.move;
			
			moveCheck.preserveCurrentState();
			
			char action = 'P';
			char turn = getTokenColour();
			int result = -2;
			if(this.move != null){
				 result = this.move.getResult();
				 turn = this.move.getPlayer();
				 action = 'P';
				if(result == 0){
					if(this.move.getPlayer() == 'R'){
						turn = 'B';
					}else{
						turn = 'R';
					}
					if(moveCheck.getPhase() == Phase.TWO || moveCheck.getPhase() == Phase.THREE){
						action = 'M';
					}
				}else if(result == 1){
					action = 'R';
				}
			}
			
			
			List<Move> moves = moveCheck.getAvailableMoves(action, turn);
			boolean gameWon = false;
			while(!gameWon){
//				System.out.println("Playing Game..." + moveCheck.getState());
//				System.out.println(moveCheck.getPhase());
//				System.out.println("Available moves..." + moves.size());
				Move move = moves.get(r.nextInt(moves.size()));
				int resultOfMove = -2;
				turn = moveCheck.getTurn();
//				System.out.println("Players turn: " + turn);
//				System.out.println("Players action: " + move.getAction());
				switch (move.getAction()) {
				case 'P':
//					System.out.println("placing on... " + move.getPlacementIndex());
					resultOfMove = moveCheck.addToken(turn, move.getPlacementIndex());
					move.setResult(resultOfMove);
					break;
				case 'M':
//					System.out.println("moving...");
					IntPairInterface movement = move.getMovementIndexs();
					resultOfMove = moveCheck.moveToken(turn, movement.getFirstInt(), movement.getSecondInt());
					move.setResult(resultOfMove);
					break;
				case 'R':
//					System.out.println("removing...");
					resultOfMove = moveCheck.removeToken(turn, move.getRemovalIndex());
					move.setResult(resultOfMove);
					break;
				default:
					break;
				}
//				System.out.println("Game Won - " + moveCheck.gameWon());
				if(moveCheck.gameWon()){
					gameWon = true;
				}
				
				action = 'P';
				
				
//				System.out.println("Result of turn: " + resultOfMove);
				if(resultOfMove == 0){
					action = 'P';
					if(moveCheck.getPhase() == Phase.TWO || moveCheck.getPhase() == Phase.THREE){
						action = 'M';
					}
				}else if(resultOfMove == 1){
					action = 'R';
				}
				turn = moveCheck.getTurn();
				moves = moveCheck.getAvailableMoves(action, turn);
//				System.out.println("Moves size: " + moves.size());
//				System.out.println("Turn: " + turn);
//				System.out.println("Action: " + action);
				if(result == -1) {
					System.exit(0);
				}
				System.out.println("---------------------------------");
				moveCheck.printDetails();
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				count++;
			}
			
			double[] rewards = moveCheck.getRewards();
			
			moveCheck.returnToPreservedState();
//			moveCheck.printDetails();
			
			
			return rewards;
		}
		
		public void updateStats(double[] rewards){
			nVisits++;
			for(int i = 0; i < rewards.length; i++){
				this.rewards[i] += rewards[i];
			}
		}
	}
}