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
	private String state;
	
	private void sleep(){
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int placeToken(String state) {
		this.state = state;
		Move m = getMove('P');
		return m.getPlacementIndex();
	}

	@Override
	public int removeToken(String state) {
		this.state = state;
		Move m = getMove('R');
		return m.getRemovalIndex();
	}

	@Override
	public IntPairInterface moveToken(String state) {
		this.state = state;
		Move m = getMove('M');
		return m.getMovementIndexs();
	}
	
	private Move getMove(char action){
		MoveChecker mc = new MoveChecker();
		mc.setDetails(bd);
		
		Move m = new Move(action, getTokenColour(), state);
		List<Move> moves = mc.getAllPossibleMoves(action, getTokenColour(), bd);

		if(moves.size() == 1){
			return moves.get(0);
		}
		
		root = new TreeNode(m, new MCTSGame(bd));
		
//		double stop = System.currentTimeMillis() + 1.8e+6;
//		
//		while(System.currentTimeMillis() < stop){
//			root.selectAction();
//		}
		
//		long stop = System.currentTimeMillis() + 1800000;
//		
//		while(System.currentTimeMillis() < stop){
//			root.selectAction();
//		}
		
		long stop = System.currentTimeMillis() + 600000;
		
		while(System.currentTimeMillis() < stop){
			root.selectAction();
		}
		
//		int count = 0;
//		int limit = 100000;
//		while(count < limit){
//			root.selectAction();
//			count++;
//		}
		
		double bestValue = Double.NEGATIVE_INFINITY;
		Move bestMove = null;
		for(TreeNode child : root.children){
			System.out.println("Rewards..." + child.rewards[getPlayerID() - 1] + ", visits: " + child.nVisits);
			
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
			r = new Random();
			rewards = new double[2];
			nVisits = 0.0;
			moveCheck = new MoveChecker();
		}
		
		public void selectAction(){
			List<TreeNode> visited = new LinkedList<TreeNode>();
			TreeNode current = this;
			
			while(!current.isLeaf() && !game.isWon()){
				System.out.println("spinning in select action loop");
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
				
				visited.add(newNode);
				rewards = rollOut(newNode);
			}
			
			for(TreeNode node : visited){
				game.undo();
				node.updateStats(rewards);
			}
			this.updateStats(rewards);
			
		}


		private double[] rollOut(TreeNode node) {
			
			int count = 0;
			char action = node.move.getAction();
			char turn = node.move.getTurn();
			
			Move m = game.makeMove(node.move);

			List<Move> moves = moveCheck.getAllPossibleMoves(m.getAction(), m.getTurn(), game.getDetails());
			
			System.out.println("BEFORE ROLLOUT");
			game.printDetails();
			System.out.println("Starting rollout...");
			
			while(!game.isWon() && !moves.isEmpty() && !game.gameDraw()){
				System.out.println("spinning in rollout loop");
				Move result = game.makeMove(moves.get(r.nextInt(moves.size())));
				System.out.println("The result...");
				result.printDetails();
				count++;
				action = result.getAction();
				turn = result.getTurn();
				moves = moveCheck.getAllPossibleMoves(action, turn, game.getDetails());
				System.out.println("The new moves...");
				for (Move move : moves) {
					move.printDetails();
				}
//				sleep();
			}
			
			double[] rewards = game.getRewards();
			
			for (int i = 0; i < count; i++) {
				game.undo();
			}
			return rewards;
		}

		private void expand() {
			char action = this.move.getAction();
			char turn = this.move.getTurn();
			if(game.playerOneTokensToPlace == 0 && game.playerTwoTokensToPlace == 1 && action == 'P'){
				turn = 'B';
			}else if(game.playerOneTokensToPlace == 1 && game.playerTwoTokensToPlace == 0 && action == 'P'){
				turn = 'R';
			}
			if(game.playerOneTokensToPlace == 0 && game.playerTwoTokensToPlace == 0 && action == 'P'){
				action = 'M';
			}
			List<Move> moves = moveCheck.getAllPossibleMoves(action, turn, game.getDetails());
			children = new TreeNode[moves.size()];
			if(moves.size() == 0){
				
				System.out.println("Error expanding " + moves.size() + " children");
				game.printDetails();
				System.out.println("MoveCheck state: " + moveCheck.getState());
				System.out.println("The action was: " + action);
				System.out.println("The turn was: " + turn);
				moveCheck.printDetails();
				return;
			}
			int i = 0;
			for (Move move : moves) {
				System.out.println("Move..." + move.getAction() + ", turn: " + move.getTurn()); 
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
				double uctValue = Double.NEGATIVE_INFINITY;
					uctValue = c.rewards[game.getPlayerID()-1]
						/ ((c.nVisits) + epsilon)
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
		private MoveChecker moveChecker;
		
		public MCTSGame(String state, char turn, int playerOneTTP, int playerTwoTTP, int playerOneTR, int playerTwoTR, Phase phase){
			
			this.state = state;
			this.turn = turn;
			
			playerOneTokensToPlace = playerOneTTP;
			playerTwoTokensToPlace = playerTwoTTP;
			playerOneTokensRemaining = playerOneTR;
			playerTwoTokensRemaining = playerTwoTR;
			
			this.phase = phase;
			
			moveChecker = new MoveChecker();
			
			history = new Stack<Move>();
		}
		
		public MCTSGame(BoardDetails bd){
			state = bd.getGS();
			turn = bd.getTurn();
			playerOneTokensToPlace = bd.getPlayerOneToPlace();
			playerTwoTokensToPlace = bd.getPlayerTwoToPlace();
			playerOneTokensRemaining = bd.getPlayerOneRemaining();
			playerTwoTokensRemaining = bd.getPlayerTwoRemaining();
			phase = bd.getPhase();
			
			moveChecker = new MoveChecker();
			
			history = new Stack<Move>();
		}
		
		public void undo() {
//			System.out.println("popped... " + history.size());
			Move moveUndo = history.pop();
			
			state = moveUndo.getState();
			char action = moveUndo.getAction();
			char moveTurn = moveUndo.getTurn();
			
			switch (action) {
			case 'P':
				if(moveTurn == 'R'){
					this.turn = 'B';
					playerOneTokensToPlace++;
				}else{
					this.turn = 'R';
					playerTwoTokensToPlace++;
				}
				break;
			case 'R':
				if(moveTurn == 'R'){
					this.turn = 'R';
					playerTwoTokensRemaining++;
				}else{
					this.turn = 'B';
					playerOneTokensRemaining++;
				}
				break;
			case 'M':
				if(moveTurn == 'R'){
					this.turn = 'B';
				}else{
					this.turn = 'R';
				}
				break;
			default:
				break;
			}
			moveChecker.setDetails(getDetails());
			
			this.phase = moveChecker.getPhase();
			
		}

		public double[] getRewards() {
			double[] rewards = new double[2];
			rewards[0] = 0.0;
			rewards[1] = 0.0;
			if(isWon()){
				if(playerOneTokensRemaining <= 3){
					rewards[1] = 1.0;
				}else{
					rewards[0] = 1.0;
				}
			}
			return rewards;
		}
		
		public boolean gameDraw(){
			if(playerOneTokensRemaining == 3 && playerTwoTokensRemaining == 3){
				return true;
			}else{
				return false;
			}
		}

		public Move makeMove(Move move) {
			history.push(move);
//			System.out.println("pushed... " + history.size());
			moveChecker.setDetails(getDetails());
			
			char[] stateArray = state.toCharArray();
			char action = move.getAction();
			this.turn = move.getTurn();
			int result = -2;
			
			switch (action) {
			case 'P':
				int placement = move.getPlacementIndex();
				result = moveChecker.addToken(turn, placement);
				stateArray[placement] = turn;
				if(turn == 'R'){
					playerOneTokensToPlace--;
				}else{
					playerTwoTokensToPlace--;
				}
				break;
			case 'R':
				int removal = move.getRemovalIndex();
				result = moveChecker.removeToken(turn, removal);
				stateArray[removal] = 'N';
				if(turn == 'R'){
					playerTwoTokensRemaining--;
				}else{
					playerOneTokensRemaining--;
				}
				break;
			case 'M':
				IntPairInterface movement = move.getMovementIndexs();
				result = moveChecker.moveToken(turn, movement.getFirstInt(), movement.getSecondInt());
				stateArray[movement.getFirstInt()] = 'N';
				stateArray[movement.getSecondInt()] = turn;
				break;
			default:
				break;
			}
			if(result == 0){
				action = 'P';
				if(this.turn == 'R'){
					this.turn = 'B';
				}else{
					this.turn = 'R';
				}
				if(this.turn == 'R' && playerOneTokensToPlace <= 0){
					action = 'M';
				}else if(this.turn == 'B' && playerTwoTokensToPlace <= 0){
					action = 'M';
				}
			}else if(result == 1){
				action = 'R';
			}
			
			state = new String(stateArray);
			phase = moveChecker.getPhase();
			return new Move(action, this.turn, state);
		}

		public boolean isWon() {
			moveChecker.setDetails(getDetails());
			if(playerOneTokensRemaining == 3 || playerTwoTokensRemaining == 3){
				return true;
			}
			return moveChecker.gameWon();
		}
		
		public int getPlayerID(){
			if(turn == 'R'){
				return 1;
			}else{
				return 2;
			}
		}
		
		public BoardDetails getDetails(){
//			printDetails();
			return new BoardDetails(state, -2, playerOneTokensRemaining, playerTwoTokensRemaining, playerOneTokensToPlace, playerTwoTokensToPlace, turn, phase);
		}
		
		public void printDetails(){
			System.out.println("-----------MCTS Game Details----------");
			System.out.println("State: " + state);
			System.out.println("P1 TP: " + playerOneTokensToPlace);
			System.out.println("P2 TP: " + playerTwoTokensToPlace);
			System.out.println("P1 TR: " + playerOneTokensRemaining);
			System.out.println("P2 TR: " + playerTwoTokensRemaining);
			System.out.println("Current Turn: " + turn);
			System.out.println("Phase: " + phase);
			System.out.println("Game Won - " + isWon());
			System.out.println("--------------------------------------");
		}
		
	}

}
