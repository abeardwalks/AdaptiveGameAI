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

import players.Human;

import model.Phase;
import model.board.BoardModel;
import model.board.NewBoardModel;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;
import utility.NodeFinder;
import view.ApplicationView;
import view.PauseView;
import view.PlayingView;
import view.SetupView;
import interfaces.BoardControllerInterface;
import interfaces.BoardDetailsInterface;
import interfaces.IntPairInterface;
import interfaces.Player;

public class NewController {
	
	private BoardControllerInterface model;
	
	private ApplicationView primaryView;
	private SetupView setupView;
	private PlayingView gameView;
	private PauseView pauseView;
	
	private Thread thread;
	
	private Player player1, player2;
	
	private boolean paused;
	private boolean started;
	
	public NewController(){
		model = new NewBoardModel();
		
		primaryView = new ApplicationView();
		primaryView.addKeyListener(new HumanKeyListener());
		primaryView.setFocusable(true);
		
		List<Player> players1 = getPlayers();
		List<Player> players2 = getPlayers();
		
		setupView = new SetupView(new SetupActionListener(), players1, players2);
		primaryView.add(setupView);
		
		gameView = new PlayingView((BoardDetailsInterface) model);
		gameView.addMouseListener(new HumanMouseListener());
		gameView.addKeyListener(new HumanKeyListener());
		gameView.setFocusable(true);
		
		pauseView = new PauseView(new PauseMenuListener());
		paused = false;
		started = false;
		
		((Observable) model).addObserver(gameView);
		
		primaryView.setVisible(true);
	}
	
	private void start() {
		primaryView.remove(setupView);
		//TODO - fix this so setupView uses new player interface
		player1 = (Player) setupView.getPlayerOne();
		player2 = (Player) setupView.getPlayerTwo();
		
		player1.setTokenColour('R');
		player2.setTokenColour('B');
		
		gameView.setEnabled(true);
		
		started = true;
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				play();
			}
		});

		primaryView.add(gameView);
		primaryView.repaint();
		thread.start();
	}
	
	private void play() {
		while(!model.gameWon()){
			if(!player1.getName().equals("Human")){
				if(model.getTurn() == 1){
					executeMove(player1);
				}
			}
			if(model.gameWon()){
				break;
			}
			if(!player2.getName().equals("Human")){
				if(model.getTurn() == 2){
					executeMove(player2);
				}
			}
		}
	}

	private void executeMove(Player player){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		char playerColour = player.getTokenColour();

		if(model.getPhase().equals(Phase.ONE) && !model.millMade()){
			int placement = player.placeToken((BoardDetailsInterface) model);
			model.executeMove(new PlacementMove(model.getState(), playerColour, placement));
		}
		if(model.getPhase().equals(Phase.TWO) || model.getPhase().equals(Phase.THREE) && !model.millMade()){
			IntPairInterface movement = player.moveToken((BoardDetailsInterface) model);
			model.executeMove(new MovementMove(model.getState(), playerColour, movement.getFirstInt(), movement.getSecondInt()));
		}
		if(model.millMade()){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int removal = player.removeToken((BoardDetailsInterface) model);
			model.executeMove(new RemovalMove(model.getState(), playerColour, removal));
		}

	}
	
	@SuppressWarnings("deprecation")
	private void showPauseMenu() {
		if(paused){
			if(thread.isAlive()){
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
			if(thread.isAlive()){
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
					player = loadPlyaer("players." + file.substring(0, file.length() - 6));
				} catch (Throwable e) {}
			}
			
			if (player != null){
				players.add(player);
			}
		}
		return players;
	}

	private Player loadPlyaer(String playerClass) {
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
		boolean mill = false;
		
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
			String state = ((BoardDetailsInterface) model).getState();
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
			String state = ((BoardDetailsInterface) model).getState();
			if(model.getPhase().equals(Phase.ONE) && !model.millMade()){
				model.executeMove(new PlacementMove(state, player.getTokenColour(), position));
			}else if(model.millMade()){
				model.executeMove(new RemovalMove(state, player.getTokenColour(), position));
			}
		}
	}
}
