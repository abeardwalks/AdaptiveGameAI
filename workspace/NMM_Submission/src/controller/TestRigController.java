package controller;

import interfaces.BoardControllerInterface;
import interfaces.BoardFacadeInterface;
import interfaces.BoardTestRigInterface;
import interfaces.BoardViewInterface;
import interfaces.IntPairInterface;
import interfaces.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Phase;
import model.board.Model;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;

import view.test.ApplicationTestView;
import view.test.GameProgressBar;
import view.test.ProgressView;
import view.test.SetupTestView;

import data.recorder.Writer;

/**
 * Similar to the controller, this Test Rig controller handles the creation of the test rig view and relevant models.
 * It also creates 5 threads, on which 100 games are played on. After each game is completed it is passed to the writer for
 * output.
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 *
 */
public class TestRigController {
		
	private ApplicationTestView primaryView;
	private SetupTestView setupView;
	private ProgressView progressView;
	private GameProgressBar g1, g2, g3, g4, g5;
	
	private Player player1, player2;
	
	private String path;
	
	private HashMap<String, String> playerList;
	
	
	public TestRigController(){
		
		primaryView = new ApplicationTestView();
		primaryView.setFocusable(true);
		
		playerList = new HashMap<String, String>();
		
		g1 = new GameProgressBar();
		g2 = new GameProgressBar();
		g3 = new GameProgressBar();
		g4 = new GameProgressBar();
		g5 = new GameProgressBar();
		
		
		
		List<Player> players1 = getPlayers();
		List<Player> players2 = getPlayers();
		
		setupView = new SetupTestView(new SetupActionListener(), players1, players2);
		primaryView.add(setupView);
		
		primaryView.setVisible(true);
		
	}
	
	private void start() {
		primaryView.remove(setupView);
		player1 = setupView.getPlayerOne();
		player2 = setupView.getPlayerTwo();
		
		progressView = new ProgressView(g1, g2, g3, g4, g5, player1.getName(), player2.getName());
		
		player1.setTokenColour('R');
		player2.setTokenColour('B');
		
		path = setupView.getPath();

		//setup the threads with.
		Thread thread1 = new TestThread(0, path, player1.getName(), player2.getName(), g1);
		Thread thread2 = new TestThread(1, path, player1.getName(), player2.getName(), g2);
		Thread thread3 = new TestThread(2, path, player1.getName(), player2.getName(), g3);
		Thread thread4 = new TestThread(3, path, player1.getName(), player2.getName(), g4);
		Thread thread5 = new TestThread(4, path, player1.getName(), player2.getName(), g5);
		
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		thread5.start();

		progressView.setVisible(true);
		primaryView.add(progressView);
		primaryView.repaint();
		
	}
	
	/**
	 * Private inner class that represents a TestThread, it encapsulates everything a thread needs
	 * to execute its 100 games. 
	 * 
	 * @author Andrew White - BSc Software Engineering, 200939787
	 *
	 */
	private class TestThread extends Thread{
		
		private int number;
		private String path, playerOne, playerTwo;
		private Player player1, player2;
		
		private BoardControllerInterface model;
		
		private GameProgressBar bar;
		
		public TestThread(int number, String path, String playerOne, String playerTwo, GameProgressBar bar){
			this.number = number;
			model = new Model(); 
			this.path = path;
			this.playerOne = playerOne;
			this.playerTwo = playerTwo;
			player1 = loadPlayer(playerList.get(playerOne));
			player2 = loadPlayer(playerList.get(playerTwo));
			
			player1.setTokenColour('R');
			player2.setTokenColour('B');
			
			this.bar = bar;
		}
		
		public void run() {
		    model = new Model(); 
			((BoardTestRigInterface) model).setGamesToPlay(100);
			
			Writer writer = new Writer((BoardViewInterface) model, path, number, playerOne, playerTwo);
			writer.addObserver(bar);
			writer.addObserver(progressView);
			
			while(((BoardTestRigInterface) model).getGamesPlayed() < ((BoardTestRigInterface) model).getGamesToPlay()){
				while(!model.gameWon() && model.getPhase() != Phase.FOUR){
					if(model.getTurn() == 1 && !model.gameWon() && model.getPhase() != Phase.FOUR){
						if(!player1.getName().equals("Human")){
							executeMove(player1, (BoardFacadeInterface) model);
						}
						
					}
					if(model.getTurn() == 2 && !model.gameWon() && model.getPhase() != Phase.FOUR){
						if(!player2.getName().equals("Human")){
							executeMove(player2, (BoardFacadeInterface) model);
						}
					}
				}
				
				boolean writeComplete = writer.writeline();
				player1.reset();
				player2.reset();
				if(writeComplete){
					model.reset();	//resets the game once the game has completed. 
				}
			}
			writer.closeBuffer();
		}
		
		private void executeMove(Player player, BoardFacadeInterface model){
			
			char playerColour = player.getTokenColour();
			boolean played = false;
			while(!played && !model.gameWon() && model.getPhase() != Phase.FOUR){
				if(model.getPhase().equals(Phase.ONE) && !model.millMade() && !played){
					int placement = player.placeToken((BoardViewInterface) model);
					model.executeMove(new PlacementMove(model.getState(), playerColour, placement));
					if(model.validMove()){
						played = true;
					}
				}
				if((model.getPhase().equals(Phase.TWO) || model.getPhase().equals(Phase.THREE)) && !model.millMade() && !played){
					IntPairInterface movement = player.moveToken((BoardViewInterface) model);
					model.executeMove(new MovementMove(model.getState(), playerColour, movement.getFirstInt(), movement.getSecondInt()));
					if(model.validMove()){
						played = true;
					}
				}
				if(model.millMade() && !played){
					int removal = player.removeToken((BoardViewInterface) model);
					model.executeMove(new RemovalMove(model.getState(), playerColour, removal));
					if(model.validMove()){
						played = true;
					}
				}
			}
		}
	}	
	
	private List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		File dir = new File("bin" + System.getProperty("file.separator") + "players" );
		for(String file : dir.list()){
			Player player = null;
			if(!file.contains("$")){
				try {
					player = loadPlayer("players." + file.substring(0, file.length() - 6));
				} catch (Throwable e) {}
			}
			
			if (player != null){
				players.add(player);
				playerList.put(player.getName(), "players." + file.substring(0, file.length() - 6));
			}
		}
		return players;
	}

	private Player loadPlayer(String playerClass) {
		Player player = null;
		try {
			Class<?> theClass = Class.forName(playerClass);
			Constructor<?>[] cons = theClass.getConstructors();
			player = (Player) cons[0].newInstance();
		} catch (Throwable e) {}
		return player;
	}

	private class SetupActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			start();
		}
	}
	
}
