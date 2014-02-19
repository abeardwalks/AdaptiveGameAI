package players;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Stack;

import players.MCTS.Move;

import interfaces.IntPairInterface;
import interfaces.PlayerInterface;

public class MCTSAI implements PlayerInterface, Observer {

	@Override
	public int placeToken(String state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeToken(String state) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IntPairInterface moveToken(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTokenColour(char c) {
		// TODO Auto-generated method stub

	}

	@Override
	public char getTokenColour() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	
	private class TreeNode{
		
		private MCTSGame game;
		private Random r;
		private Move move;
		private double epsilon;
		
		TreeNode[] children;
		double nVisits;
		double[] rewards;
		
		public TreeNode(Move move, MCTSGame game){
			this.move = move;
			this.game = game;
			
			epsilon = 1e-6;
			
			rewards = new double[2];
		}
	}
	
	private class MCTSGame{
		
		private String state;
		private Stack<Move> history;
		private char turn;
		private int playerOneTokensToPlace, playerTwoTokensToPlace;
		private int playerOneTokensRemaining, playerTwoTokensRemaining;
		
		public MCTSGame(String state, char turn, int playerOneTTP, int playerTwoTTP, int playerOneTR, int playerTwoTR){
			
			this.state = state;
			this.turn = turn;
			
			playerOneTokensToPlace = playerOneTTP;
			playerTwoTokensToPlace = playerTwoTTP;
			playerOneTokensRemaining = playerOneTR;
			playerTwoTokensRemaining = playerTwoTR;
			
			history = new Stack<Move>();
		}
		
	}

}
