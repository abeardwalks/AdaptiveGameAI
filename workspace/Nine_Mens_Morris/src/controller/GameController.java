package controller;

import interfaces.GameStateInterface;
import interfaces.PlayerInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import board.MorrisBoard;

import view.ApplicationView;
import view.PlayingView;
import view.SetupView;

public class GameController {
	
	private GameStateInterface gs;
	private PlayerInterface[] players;
	private ApplicationView primaryView;
	private Thread thread;
	private PlayingView gameView;
	private SetupView setupView;
	
	public GameController(){
		
		primaryView = new ApplicationView();
		
		List<PlayerInterface> players = getPlayers();
		gs = new MorrisBoard();
		
		setupView = new SetupView(new SetupActionListener(), players);
		primaryView.addPane(setupView);
		
		gameView = new PlayingView(new HumanPlayerListener());
		
		primaryView.setVisible(true);
	
	}
	
	private void start() {
		primaryView.remove(setupView);
		primaryView.addPane(gameView);
		primaryView.repaint();
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
	
	private class HumanPlayerListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println(e.getX());
			System.out.println(e.getY());
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
