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
		moveChecker.reset();
		setUpMoveChecker();
		moveChecker.setTurn(color);
		Move m = getMove('P');
		return m.getPlacementIndex();
	}

	@Override
	public int removeToken(String state) {
		moveChecker.reset();
		setUpMoveChecker();
		moveChecker.setTurn(color);
		Move m = getMove('R');
		System.out.println("REEEMOOOOVING: " + m.getRemovalIndex());
		return m.getRemovalIndex();
	}

	@Override
	public IntPairInterface moveToken(String state) {
		moveChecker.reset();
		setUpMoveChecker();
		moveChecker.setTurn(color);
		Move m = getMove('M');
		return m.getMovementIndexs();
	}
	
	private Move getMove(char action){
		System.out.println("My Action: " + action);
		System.out.println("the current state: " + moveChecker.getState());
		
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
		long stop = System.currentTimeMillis() + 10000;
		
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
//				System.out.println("Applying move to: " + m.getGameStateBeforeAction());
//				System.out.println("Action was: " + m.getAction());
//				System.out.println("Turn was: " + m.getPlayer());
				rewards = rollOut(newNode);
			}
			moveCheck.returnToPreservedState();
			for(TreeNode node : visited){
				node.updateStats(rewards);
			}
			this.updateStats(rewards);
		}
		
		public void expand() {
			char action = 'P';
			char turn = moveCheck.getTurn();
			if(this.move != null){
				int result = this.move.getResult();
				turn = this.move.getPlayer();
				if(result == 0){
					action = 'P';
					if(turn == 'R'){
						turn = 'B';
					}else{
						turn = 'R';
					}
					if(turn == 'R' && moveCheck.getPlayerOneTokensToPlace() <= 0){
						action = 'M';
					}else if(turn == 'B' && moveCheck.getPlayerTwoTokensToPlace() <= 0){
						action = 'M';
					}
				}
				if(result == 1){
					action = 'R';
				}
			}
			List<Move> moves = moveCheck.getAvailableMoves(action, turn);
			children = new TreeNode[moves.size()];
			if(moves.size() == 0){
				System.out.println("-------------------------------------");
				System.out.println("Error expanding! ---- moves is empty");
				System.out.println("My action was: " + action);
				System.out.println("The turn was: " + turn);
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
			int count = 0;
			System.out.println("Using a move that was acted on..: " + tn.move.getGameStateBeforeAction());
			System.out.println("The Action was..." + tn.move.getAction());
			System.out.println("The turn was..." + tn.move.getPlayer());
			
			Move m = tn.move;
			int result = -2;
			char action = m.getAction();
			char turn = m.getPlayer();
			switch (action) {
			case 'P':
				result = moveCheck.addToken(turn, m.getPlacementIndex());
				m.setResult(result);
				break;
			case 'M':
				IntPairInterface movement = m.getMovementIndexs();
				result = moveCheck.moveToken(turn, movement.getFirstInt(), movement.getSecondInt());
				m.setResult(result);
				break;
			case 'R':
				result = moveCheck.removeToken(turn, m.getRemovalIndex());
				m.setResult(result);
				break;
			default:
				break;
			}
			if(result == 0){
				action = 'P';
				if(turn == 'R'){
					turn = 'B';
				}else{
					turn = 'R';
				}
				if(turn == 'R' && moveCheck.getPlayerOneTokensToPlace() <= 0){
					action = 'M';
				}else if(turn == 'B' && moveCheck.getPlayerTwoTokensToPlace() <= 0){
					action = 'M';
				}
			}
			if(result == 1){
				action = 'R';
			}
			moveCheck.preserveCurrentState();
			
			List<Move> moves = moveCheck.getAvailableMoves(action, turn);
			boolean gameWon = moveCheck.gameWon();
			
			while(!gameWon){
				if(moves.isEmpty()){
					break;
				}
				Move randomMove = moves.get(r.nextInt(moves.size()));
				
				action = randomMove.getAction();
				turn = randomMove.getPlayer();
				System.out.println("-------------------------------");
				System.out.println("Action: " + action);
				System.out.println("Turn: " + turn);
				System.out.println("State: " + moveCheck.getState());
				System.out.println("Game won - " + moveCheck.gameWon());
				moveCheck.printDetails();
				switch (action) {
				case 'P':
					
					result = moveCheck.addToken(turn, randomMove.getPlacementIndex());
					randomMove.setResult(result);
					break;
				case 'M':
					IntPairInterface movement = randomMove.getMovementIndexs();
					result = moveCheck.moveToken(turn, movement.getFirstInt(), movement.getSecondInt());
					randomMove.setResult(result);
					break;
				case 'R':
					result = moveCheck.removeToken(turn, randomMove.getRemovalIndex());
					randomMove.setResult(result);
					break;
				default:
					break;
				}
				if(result == 0){
					action = 'P';
					if(turn == 'R'){
						turn = 'B';
					}else{
						turn = 'R';
					}
					if(turn == 'R' && moveCheck.getPlayerOneTokensToPlace() <= 0){
						action = 'M';
					}else if(turn == 'B' && moveCheck.getPlayerTwoTokensToPlace() <= 0){
						action = 'M';
					}
				}
				if(result == 1){
					action = 'R';
				}
				gameWon = moveCheck.gameWon();
				moves = moveCheck.getAvailableMoves(action, turn);
				
			}
			
			
			
			double[] rewards = moveCheck.getRewards();
			
			moveCheck.returnToPreservedState();
			
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
