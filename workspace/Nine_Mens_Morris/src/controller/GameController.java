package controller;

import interfaces.GameStateInterface;
import interfaces.PlayerInterface;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


import players.Human;

import board.BoardDetails;
import board.MorrisBoard;
import board.Phase;

import view.ApplicationView;
import view.PlayingView;
import view.SetupView;

public class GameController implements Observer{
	
	private GameStateInterface gs;
	private Phase phase;
	private ApplicationView primaryView;
	private Thread thread;
	private boolean interrupted; 
	private PlayingView gameView;
	private SetupView setupView;
	private char turn;
	private int result;
	private PlayerInterface p1;
	private PlayerInterface p2;
	
	
	public GameController(){
		
		primaryView = new ApplicationView();
		
		List<PlayerInterface> players1 = getPlayers();
		List<PlayerInterface> players2 = getPlayers();
		gs = new MorrisBoard();
		phase = gs.getPhase();
		turn = 'R';
		result = -2;
		setupView = new SetupView(new SetupActionListener(), players1, players2);
		primaryView.addPane(setupView);
		
		gameView = new PlayingView();
		gameView.addMouseListener(new HumanMouseListener());
	    ((Observable) gs).addObserver(gameView);
	    ((Observable) gs).addObserver(this);
		
		primaryView.setVisible(true);
	
	}
	
	private void start() {
		primaryView.remove(setupView);
		p1 = setupView.getPlayerOne();
		p2 = setupView.getPlayerTwo();
		((Observable) gs).addObserver(p1);
		((Observable) gs).addObserver(p2);
		
		p1.setTokenColour('R');
		p2.setTokenColour('B');
		p1.intialize(gs);
		p2.intialize(gs);
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				play();
			}
		});


		primaryView.addPane(gameView);
		primaryView.repaint();
		thread.start();
	}
	
	private void play() {
		interrupted = false; 
		while(result != 2 || interrupted){
			p1.makeMove();
			p2.makeMove();
		}
	}
	
	
	private List<PlayerInterface> getPlayers() {
		List<PlayerInterface> players = new ArrayList<PlayerInterface>();
		File dir = new File("bin" + System.getProperty("file.separator") + "players" );
		for(String file : dir.list()){
			PlayerInterface player = null;
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

	private PlayerInterface loadPlyaer(String playerClass) {
		PlayerInterface player = null;
		try {
			Class<?> theClass = Class.forName(playerClass);
			Constructor<?>[] cons = theClass.getConstructors();
			player = (PlayerInterface) cons[0].newInstance();
		} catch (Throwable e) {}
		return player;
	}
	
	private class SetupActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			start();
		}
	}
	
	private class HumanMouseListener implements MouseListener {
		
		int x = 0;
		int y = 0;

		@Override
		public void mouseClicked(MouseEvent e) {
			if(phase.equals(Phase.ONE) || result == 1 || phase.equals(Phase.THREE)){
				int x = e.getX();
				int y = e.getY();
				if(p1 instanceof Human && p1.getTokenColour() == turn){	
					((Human) p1).makeHumanPlacement(x, y);
				} else if(p2 instanceof Human) {
					((Human) p2).makeHumanPlacement(x, y);
				}
				phase = gs.getPhase();
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
			if(phase.equals(Phase.TWO) || phase.equals(Phase.THREE)){
				x = e.getX();
				y = e.getY();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if((phase.equals(Phase.TWO) || phase.equals(Phase.THREE)) && (result == 0 || result == -1)){
				int x2 = e.getX();
				int y2 = e.getY();
				if(p1 instanceof Human && p1.getTokenColour() == turn){	
					((Human) p1).makeHumanMove(x, y, x2, y2);
				}else if(p2 instanceof Human) {
					((Human) p2).makeHumanMove(x, y, x2, y2);
				}
				phase = gs.getPhase();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		BoardDetails bd = (BoardDetails) arg;
		result = bd.getResult();
		turn = bd.getTurn();
		System.out.println("Result: " + result );
		System.out.println("  Turn:"  + turn);
	}

}
