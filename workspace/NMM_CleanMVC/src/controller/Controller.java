package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import data.recorder.Writer;

import players.Human;

import model.Phase;
import model.board.Model;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;
import utility.NodeFinder;
import view.ApplicationView;
import view.PauseView;
import view.PlayingView;
import view.SetupView;
import view.TestView;
import interfaces.BoardControllerInterface;
import interfaces.BoardViewInterface;
import interfaces.BoardFacadeInterface;
import interfaces.IntPairInterface;
import interfaces.Player;

public class Controller {
	
	private BoardControllerInterface model;
	
	private ApplicationView primaryView;
	private SetupView setupView;
	private PlayingView gameView;
	private PauseView pauseView;
	private TestView testView;
	
	private Thread thread;
	
	private Player player1, player2;
	
	private boolean paused;
	private boolean started;
	
	private Writer writer;
	
	public Controller(){
		model = new Model();
		
		primaryView = new ApplicationView();
		primaryView.addKeyListener(new HumanKeyListener());
		primaryView.setFocusable(true);
		
		List<Player> players1 = getPlayers();
		List<Player> players2 = getPlayers();
		
		setupView = new SetupView(new SetupActionListener(), players1, players2);
		primaryView.add(setupView);
		
		gameView = new PlayingView((BoardViewInterface) model);
		gameView.addMouseListener(new HumanMouseListener());
		gameView.addKeyListener(new HumanKeyListener());
		gameView.setFocusable(true);
		
		pauseView = new PauseView(new PauseMenuListener());
		paused = false;
		started = false;
		
		testView = new TestView((BoardViewInterface) model);
		
		((Observable) model).addObserver(gameView);
		((Observable) model).addObserver(testView);
		
		primaryView.setVisible(true);
	}
	
	private void start() {
		primaryView.remove(setupView);
		player1 = setupView.getPlayerOne();
		player2 = setupView.getPlayerTwo();
		
		player1.setTokenColour('R');
		player2.setTokenColour('B');
		
		gameView.setEnabled(true);
		
		started = true;
		if(!setupView.getTestRig()){
			thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					play();
				}
			});
	
			primaryView.add(gameView);
			primaryView.repaint();
		}else{
			thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					testPlay();
				}
			});

			primaryView.add(testView);
			primaryView.repaint();
		}
		thread.start();
	}
	
	private void play() {
		while(!model.gameWon()){
			try {
				Thread.sleep(2000);
				if(model.getTurn() == 1 && !model.gameWon()){
					if(!player1.getName().equals("Human")){
						executeMove(player1, (BoardFacadeInterface) model);
					}
				}else if(model.getTurn() == 2 && !model.gameWon()){
					if(!player2.getName().equals("Human")){
						executeMove(player2, (BoardFacadeInterface) model);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void testPlay() {

		model.setGamesToPlay(100);
		writer = new Writer((BoardViewInterface) model);

		while(model.getGamesPlayed() < model.getGamesToPlay()){
			while(!model.gameWon()){
				if(model.getTurn() == 1 && !model.gameWon()){
					if(!player1.getName().equals("Human")){
						executeMove(player1, (BoardFacadeInterface) model);
					}
				}else if(model.getTurn() == 2 && !model.gameWon()){
					if(!player2.getName().equals("Human")){
						executeMove(player2, (BoardFacadeInterface) model);
					}
				}
			}
			writer.writeline();
			player1.reset();
			player2.reset();
			model.reset();
		}
		writer.closeBuffer();
	}

	private void executeMove(Player player, BoardFacadeInterface model){
		
		char playerColour = player.getTokenColour();
		boolean played = false;
		while(!played){
			if(model.getNextAction() == 'P' && !played){
				int placement = player.placeToken((BoardViewInterface) model);
				model.executeMove(new PlacementMove(model.getState(), playerColour, placement));
				if(model.validMove()){
					played = true;
				}
			}
			if(model.getNextAction() == 'M' && !played){
				IntPairInterface movement = player.moveToken((BoardViewInterface) model);
				model.executeMove(new MovementMove(model.getState(), playerColour, movement.getFirstInt(), movement.getSecondInt()));
				if(model.validMove()){
					played = true;
				}
			}
			if(model.getNextAction() == 'R' && !played){
				int removal = player.removeToken((BoardViewInterface) model);
				model.executeMove(new RemovalMove(model.getState(), playerColour, removal));
				if(model.validMove()){
					played = true;
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void showPauseMenu() {
		if(paused){
			if(thread.isAlive() || model.gameWon()){
				thread.suspend();
				gameView.setEnabled(false);
				pauseView.setEnabled(true);
				pauseView.setVisible(true);
				primaryView.remove(gameView);
				primaryView.add(pauseView);
				primaryView.addPane(pauseView);
				primaryView.add(gameView);
				primaryView.repaint();
			}
			pauseView.repaint();
		}else{
			if(thread.isAlive() || model.gameWon()){
				thread.resume();
				pauseView.setEnabled(false);
				pauseView.setVisible(false);
				primaryView.remove(pauseView);
				primaryView.repaint();
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
	
	private class HumanKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println("Key Released " + e.getKeyCode());
			int code = e.getKeyCode();
			if(started){
				if(code == 27 || code == 32 || code == 80 || code == 10){
					if(paused){
						paused = false;
						gameView.setEnabled(true);
					}else{
						paused = true;
						gameView.setEnabled(false);
					}
					showPauseMenu();
				}
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
		
	}
	
	private class SetupActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			gameView.setToolTip(setupView.getShowToolTip());
			start();
		}
	}
	
	private class PauseMenuListener implements ActionListener {
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e){
			String action = e.getActionCommand();
			if(action.equals("resume")){
				paused = false;
				gameView.setEnabled(true);
				showPauseMenu();
			}else if(action.equals("reset")){
				paused = false;
				model.reset();
				gameView.setEnabled(true);
				pauseView.setEnabled(false);
				pauseView.setVisible(false);
				primaryView.remove(pauseView);
				primaryView.remove(gameView);
				primaryView.add(setupView);
				primaryView.repaint();
				start();
			}else if(action.equals("setup")){
				paused = false;
				started = false;
				model.reset();
				thread.suspend();
				gameView.setEnabled(false);
				pauseView.setEnabled(false);
				pauseView.setVisible(false);
				primaryView.remove(pauseView);
				primaryView.remove(gameView);
				primaryView.add(setupView);
				primaryView.repaint();
			}else if(action.equals("quit")){
				System.exit(0);
			}
		}
	}
	
	private class HumanMouseListener implements MouseListener{
		
		private int x = 0;
		private int y = 0;
		
		
		private NodeFinder finder;
		
		public HumanMouseListener(){
			finder = new NodeFinder();
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == 3){
				if(started){
					if(paused){
						paused = false;
						gameView.setEnabled(true);
					}else{
						paused = true;
						gameView.setEnabled(false);
					}
					showPauseMenu();
				}
			}
			if(e.getButton() == 1 && !paused){
				if(!model.gameWon()){
					int x = e.getX();
					int y = e.getY();
					int position = finder.getNode(x, y);
					if(player1 instanceof Human && player1.getPlayerID() == model.getTurn() && position != -1){	
						humanClick(position, player1);
					} else if(player2 instanceof Human && player2.getPlayerID() == model.getTurn() && position != -1){	
						humanClick(position, player2);
					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(model.getPhase().equals(Phase.TWO) || model.getPhase().equals(Phase.THREE)){
				x = e.getX();
				y = e.getY();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			String state = ((BoardViewInterface) model).getState();
			if(!paused){
					if((model.getPhase().equals(Phase.TWO) || model.getPhase().equals(Phase.THREE)) && !model.millMade()){
						int x2 = e.getX();
						int y2 = e.getY();
						int firstNode = finder.getNode(x, y);
						int secondNode = finder.getNode(x2, y2);
						if((player1 instanceof Human && player1.getPlayerID() == model.getTurn()) && (firstNode != -1 && secondNode != -1)){
							if(!model.millMade()){
								
								model.executeMove(new MovementMove(state, player1.getTokenColour(), firstNode, secondNode));
							}
						} else if((player2 instanceof Human && player2.getPlayerID() == model.getTurn()) && (firstNode != -1 && secondNode != -1)){	
							if(!model.millMade()){
								
								model.executeMove(new MovementMove(state, player2.getTokenColour(), firstNode, secondNode));
							}
						} 
					
				}
			}
		}

		private void humanClick(int position, Player player){
			String state = ((BoardViewInterface) model).getState();
			if(model.getPhase().equals(Phase.ONE) && !model.millMade()){
				model.executeMove(new PlacementMove(state, player.getTokenColour(), position));
			}else if(model.millMade()){
				model.executeMove(new RemovalMove(state, player.getTokenColour(), position));
			}
		}
	}
}
