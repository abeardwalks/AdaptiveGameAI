package controller;

import interfaces.GameStateInterface;
import interfaces.IntPairInterface;
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

import players.Human;


import model.Phase;
import model.board.MorrisBoard;
import view.ApplicationView;
import view.PlayingView;
import view.SetupView2;

public class GameController {
	
	private GameStateInterface gs;
	private ApplicationView primaryView;
	private Thread thread;
	private PlayingView gameView;
	private SetupView2 setupView;
	private PlayerInterface p1;
	private PlayerInterface p2;
	private char turn;
	private MoveChecker mc;
	private Phase phase;
	private int result;
	private boolean millMade;
	
	public GameController(){
		
		primaryView = new ApplicationView();
		List<PlayerInterface> players1 = getPlayers();
		List<PlayerInterface> players2 = getPlayers();
		gs = new MorrisBoard();
		turn = 'R';
		result = -2;
		mc = new MoveChecker();
		phase = Phase.ONE;
		millMade = false;
		setupView = new SetupView2(new SetupActionListener(), players1, players2);
		primaryView.addPane(setupView);
		
		gameView = new PlayingView();
		gameView.addMouseListener(new HumanMouseListener());
		((Observable) gs).addObserver(gameView);
		
		primaryView.setVisible(true);
		
	}
	
	private void start() {
		primaryView.remove(setupView);
		p1 = setupView.getPlayerOne();
		p2 = setupView.getPlayerTwo();
		
		p1.setTokenColour('R');
		p2.setTokenColour('B');

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
		while(result != 2){
			if(!p1.getName().equals("Human")){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(turn == 'R'){
					executeMove('R', p1);
				}
			}
				
			if(!p2.getName().equals("Human")){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(turn == 'B'){
					executeMove('B', p2);
				}
			}
		}
	}
	
	private void executeMove(char tokenColour, PlayerInterface player){
		boolean played = false;
		while(!played){
			if(phase.equals(Phase.ONE) && (result == -2 || result == 0 || result == -1)){
				int position = player.placeToken(gs.getState());
				result = mc.addToken(tokenColour, position);
				if(result == 0 || result == 1){
					if(tokenColour == 'R'){
						gs.lowerPlayerOneCount();
					}else{
						gs.lowerPlayerTwoCount();
					}
					gs.addToken(tokenColour, position);
				}
				if(result == 1){
					millMade = true;
				}
			}
			if(phase.equals(Phase.TWO) || phase.equals(Phase.THREE) && (result == 0 || result == -1)){
				IntPairInterface ip = player.moveToken(gs.getState());
				result = mc.moveToken(tokenColour, ip.getFirstInt(), ip.getSecondInt());
				if(result == 0 || result == 1){
					gs.moveToken(ip.getFirstInt(), ip.getSecondInt());
				}
				if(result == 1){
					millMade = true;
				}
			}
			if(millMade && (result == -1 || result == 1)){
				
				int position = player.removeToken(gs.getState());
				result = mc.removeToken(tokenColour, position);
				if(result == 0){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					millMade = false;
					gs.removeToken(position);
				}
			}
			
			if(result == 0 ){
				played = true;
				phase = mc.getPhase();
				System.out.println(phase);
			}
		}
		if(!millMade){
			if(turn == 'R'){
				turn = 'B';
			}else{
				turn = 'R';
			}
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
		
		private int x0,x1,x2,x3,x4,x5,x6;
		private int y0,y1,y2,y3,y4,y5,y6;
		boolean mill = false;
		
		public HumanMouseListener(){
			initializeCordinates();
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if(result != 2){
				int x = e.getX();
				int y = e.getY();
				int position = findNodeClicked(x, y);
				if(p1 instanceof Human && p1.getTokenColour() == turn && position != -1){	
					humanClick(position, p1);
				} else if(p2 instanceof Human && p2.getTokenColour() == turn && position != -1){	
					humanClick(position, p2);
				}
			}
			if(!mill && result != -1){
				if(turn == 'R'){
					turn = 'B';
				}else{
					turn = 'R';
				}
			}
			phase = mc.getPhase();
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
				System.out.println("Moving phase");
				int x2 = e.getX();
				int y2 = e.getY();
				int firstNode = findNodeClicked(x, y);
				int secondNode = findNodeClicked(x2, y2);
				System.out.println("Node One: " + firstNode + ", Second Node: " + secondNode); 
				System.err.println(turn);
				if((p1 instanceof Human && p1.getTokenColour() == turn) && (firstNode != -1 && secondNode != -1)){
					System.out.println("Above to move");
					if(result == -1 || result == 0){
						result = mc.moveToken(p1.getTokenColour(), firstNode, secondNode);
						System.err.println("Result of move: " + result);
						if(result != -1){
							System.out.println("Made move...");
							gs.moveToken(firstNode, secondNode);
						}
						if(result == 1){
							mill = true;
						}
					}
				} else if((p2 instanceof Human && p2.getTokenColour() == turn) && (firstNode != -1 && secondNode != -1)){	
					System.out.println("Above to move");
					if(result == -1 || result == 0){
						result = mc.moveToken(p2.getTokenColour(), firstNode, secondNode);
						if(result != -1){
							System.out.println("Made move...");
							gs.moveToken(firstNode, secondNode);
						}
						if(result == 1){
							mill = true;
						}
					}
				}
			}
			if((phase.equals(Phase.TWO) || phase.equals(Phase.THREE)) && !mill && result != -1){
				if(turn == 'R'){
					turn = 'B';
				}else{
					turn = 'R';
				}
			}
			
			phase = mc.getPhase();
			System.out.println("Result: " + result + ", " + phase);
		}

		private void humanClick(int position, PlayerInterface player){
			if(phase.equals(Phase.ONE) && (result == -1 || result == -2 || result == 0) && !mill){
				result = mc.addToken(player.getTokenColour(), position);
				if(result != -1){
					if(turn == 'R'){
						gs.lowerPlayerOneCount();
					}else{
						gs.lowerPlayerTwoCount();
					}
					gs.addToken(player.getTokenColour(), position);
				}
				if(result == 1){
					mill = true;
				}
			}else if(mill && (result == -1 || result == 1)){
				result = mc.removeToken(player.getTokenColour(), position);
				if(result != -1){
					gs.removeToken(position);
					mill = false;
				}
			}
		}
		
		private void initializeCordinates() {
			x0 = 102;
			x1 = 375;
			x2 = 647;
			x3 = 193;
			x4 = 557;
			x5 = 285;
			x6 = 465;
			
			y0 = 7;
			y1 = 97;
			y2 = 188;
			y3 = 281;
			y4 = 371;
			y5 = 462;
			y6 = 552;
		}
		
		private int findNodeClicked(int x, int y){
			if((x >= x0 && x <= (x0 + 40)) && (y >= y0 && y <= (y0 + 40))){
				return 0;
			} else if((x >= x1 && x <= x1 + 40) && (y >= y0 && y <= y0 + 40)){
				return 1;
			} else if((x >= x2 && x <= x2 + 40) && (y >= y0 && y <= y0 + 40)){
				return 2;
			} else if((x >= x3 && x <= x3 + 40) && (y >= y1 && y <= y1 + 40)){
				return 3;
			} else if((x >= x1 && x <= x1 + 40) && (y >= y1 && y <= y1 + 40)){
				return 4;
			} else if((x >= x4 && x <= x4 + 40) && (y >= y1 && y <= y1 + 40)){
				return 5;
			} else if((x >= x5 && x <= x5 + 40) && (y >= y2 && y <= y2 + 40)){
				return 6;
			} else if((x >= x1 && x <= x1 + 40) && (y >= y2 && y <= y2 + 40)){
				return 7;
			} else if((x >= x6 && x <= x6 + 40) && (y >= y2 && y <= y2 + 40)){
				return 8;
			} else if((x >= x0 && x <= x0 + 40) && (y >= y3 && y <= y3 + 40)){
				return 9;
			} else if((x >= x3 && x <= x3 + 40) && (y >= y3 && y <= y3 + 40)){
				return 10;
			} else if((x >= x5 && x <= x5 + 40) && (y >= y3 && y <= y3 + 40)){
				return 11;
			} else if((x >= x6 && x <= x6 + 40) && (y >= y3 && y <= y3 + 40)){
				return 12;
			} else if((x >= x4 && x <= x4 + 40) && (y >= y3 && y <= y3 + 40)){
				return 13;
			} else if((x >= x2 && x <= x2 + 40) && (y >= y3 && y <= y3 + 40)){
				return 14;
			} else if((x >= x5 && x <= x5 + 40) && (y >= y4 && y <= y4 + 40)){
				return 15;
			} else if((x >= x1 && x <= x1 + 40) && (y >= y4 && y <= y4 + 40)){
				return 16;
			} else if((x >= x6 && x <= x6 + 40) && (y >= y4 && y <= y4 + 40)){
				return 17;
			} else if((x >= x3 && x <= x3 + 40) && (y >= y5 && y <= y5 + 40)){
				return 18;
			} else if((x >= x1 && x <= x1 + 40) && (y >= y5 && y <= y5 + 40)){
				return 19;
			} else if((x >= x4 && x <= x4 + 40) && (y >= y5 && y <= y5 + 40)){
				return 20;
			} else if((x >= x0 && x <= x0 + 40) && (y >= y6 && y <= y6 + 40)){
				return 21;
			} else if((x >= x1 && x <= x1 + 40) && (y >= y6 && y <= y6 + 40)){
				return 22;
			} else if((x >= x2 && x <= x2 + 40) && (y >= y6 && y <= y6 + 40)){
				return 23;
			}
			return -1;
		}
	}


}