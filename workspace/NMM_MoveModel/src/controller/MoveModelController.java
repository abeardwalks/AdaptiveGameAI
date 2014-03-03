package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import players.Human;
import view.ApplicationView;
import view.MoveModelPlayingView;
import view.MoveModelSetupView;
import view.PauseView;
import model.Phase;
import model.board.BoardModel;
import move.MovementMove;
import move.PlacementMove;
import move.RemovalMove;
import interfaces.BoardDetailsInterface;
import interfaces.BoardMutatorInterface;
import interfaces.IntPairInterface;
import interfaces.Player;

public class MoveModelController {
	
	private BoardMutatorInterface model;
	
	private ApplicationView primaryView;
	private MoveModelSetupView setupView;
	private MoveModelPlayingView gameView;
	private PauseView pauseView;
	
	private Thread thread;
	
	private Player player1, player2;
	private char turn;
	
	private MoveChecker mc;
	private Phase phase;
	private int result;
	private boolean millMade;
	
	private boolean paused;
	private boolean started;
	
	public MoveModelController() {
		model = new BoardModel();
		
		primaryView = new ApplicationView();
		primaryView.addKeyListener(new HumanKeyListener());
		primaryView.setFocusable(true);
		
		List<Player> players1 = getPlayers();
		List<Player> players2 = getPlayers();
		
		setupView = new MoveModelSetupView(new SetupActionListener(), players1, players2);
		primaryView.add(setupView);
		
		gameView = new MoveModelPlayingView((BoardDetailsInterface) model);
		gameView.addMouseListener(new HumanMouseListener());
		gameView.addKeyListener(new HumanKeyListener());
		gameView.setFocusable(true);
		
		pauseView = new PauseView(new PauseMenuListener());
		paused = false;
		started = false;
		
		mc = new MoveChecker();
		turn = 'R';
		result = -2;
		phase = Phase.ONE;
		millMade = false;
		
		((Observable) model).addObserver(gameView);
		
		primaryView.setVisible(true);
		
	}
	
	public MoveModelController(BoardMutatorInterface model){
		
		this.model = model;
		
		primaryView = new ApplicationView();
		primaryView.addKeyListener(new HumanKeyListener());
		primaryView.setFocusable(true);
		
		List<Player> players1 = getPlayers();
		List<Player> players2 = getPlayers();
		
		setupView = new MoveModelSetupView(new SetupActionListener(), players1, players2);
		primaryView.add(setupView);
		
		gameView = new MoveModelPlayingView((BoardDetailsInterface) this.model);
		gameView.addMouseListener(new HumanMouseListener());
		gameView.addKeyListener(new HumanKeyListener());
		gameView.setFocusable(true);
		
		pauseView = new PauseView(new PauseMenuListener());
		paused = false;
		started = false;
		
		mc = new MoveChecker((BoardDetailsInterface) this.model);
		if(((BoardDetailsInterface) this.model).getTurn() == 1){
			turn = 'R';
		}else{
			turn = 'B';
		}
		result = 0;
		phase = ((BoardDetailsInterface) this.model).getPhase();
		millMade = false;
		
		((Observable) this.model).addObserver(gameView);
		
		primaryView.setVisible(true);
	}
	
	private void start() {
		primaryView.remove(setupView);
		//TODO - fix this so setupView uses new player interface
		player1 = (Player) setupView.getPlayerOne();
		player2 = (Player) setupView.getPlayerTwo();
		
		player1.setTokenColour('R');
		player2.setTokenColour('B');
		
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
		while(result != 2){
			if(!player1.getName().equals("Human")){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(turn == 'R'){
					executeMove(player1);
				}
			}
				
			if(!player2.getName().equals("Human")){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(turn == 'B'){
					executeMove(player2);
				}
			}
		}
	}
	
	private void executeMove(Player player){
		boolean notPlayed = true;
		String state = ((BoardDetailsInterface) model).getState();
		char playerColour = player.getTokenColour();
		while(notPlayed){
			if(phase.equals(Phase.ONE) && (result == -2 || result == 0 || result == -1)){
				int placement = player.placeToken((BoardDetailsInterface) model);
				result = mc.addToken(playerColour, placement);
				if(result != -1){
					model.executeMove(new PlacementMove(state, playerColour, placement));
				}
				if(result == 1){
					millMade = true;
				}
			}
			if(phase.equals(Phase.TWO) || phase.equals(Phase.THREE) && (result == 0 || result == -1)){
				IntPairInterface movement = player.moveToken((BoardDetailsInterface) model);
				result = mc.moveToken(playerColour, movement.getFirstInt(), movement.getSecondInt());
				if(result != -1){
					model.executeMove(new MovementMove(state, playerColour, movement.getFirstInt(), movement.getSecondInt()));
				}
				if(result == 1){
					millMade = true;
				}
			}
			if(millMade && (result == -1 || result == 1)){
				state = ((BoardDetailsInterface) model).getState();
				int removal = player.removeToken((BoardDetailsInterface) model);
				result = mc.removeToken(playerColour, removal);
				if(result != -1){
					model.executeMove(new RemovalMove(state, playerColour, removal));
				}
			}
			
			if(result == 0){
				notPlayed = false;
				phase = mc.getPhase();
				model.setPhase(phase);
				if(turn == 'R'){
					turn = 'B';
					playerColour = 'B';
				}else{
					turn = 'R';
					playerColour = 'R';
				}
				millMade = false;
				model.setTurn();
			}
		}
	}
	
	private void reset(){
		mc = new MoveChecker();
		turn = 'R';
		result = -2;
		phase = Phase.ONE;
		millMade = false;
		
		model.reset();
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
//			pauseView.repaint();
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
				reset();
				gameView.setEnabled(true);
				thread.suspend();
				start();
			}else if(action.equals("setup")){
				paused = false;
				started = false;
				reset();
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
	
	private class HumanMouseListener implements MouseListener, MouseMotionListener{
		
		private int x = 0;
		private int y = 0;
		
		
		private int x0,x1,x2,x3,x4,x5,x6;
		private int y0,y1,y2,y3,y4,y5,y6;
		boolean mill = false;
		
		public HumanMouseListener(){
			initializeCordinates();
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
				if(result != 2){
					int x = e.getX();
					int y = e.getY();
					int position = findNodeClicked(x, y);
					if(player1 instanceof Human && player1.getTokenColour() == turn && position != -1){	
						humanClick(position, player1);
					} else if(player2 instanceof Human && player2.getTokenColour() == turn && position != -1){	
						humanClick(position, player2);
					}
				}
				phase = mc.getPhase();
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
			String state = ((BoardDetailsInterface) model).getState();
			if(!paused){
				if(!mill){
					if((phase.equals(Phase.TWO) || phase.equals(Phase.THREE)) && (result == 0 || result == -1)){
						int x2 = e.getX();
						int y2 = e.getY();
						int firstNode = findNodeClicked(x, y);
						int secondNode = findNodeClicked(x2, y2);
						if((player1 instanceof Human && player1.getTokenColour() == turn) && (firstNode != -1 && secondNode != -1)){
							if(result == -1 || result == 0){
								result = mc.moveToken(player1.getTokenColour(), firstNode, secondNode);
								if(result != -1){
									model.executeMove(new MovementMove(state, player1.getTokenColour(), firstNode, secondNode));
								}
								if(result == 1){
									mill = true;
								}
							}
						} else if((player2 instanceof Human && player2.getTokenColour() == turn) && (firstNode != -1 && secondNode != -1)){	
							if(result == -1 || result == 0){
								result = mc.moveToken(player2.getTokenColour(), firstNode, secondNode);
								if(result != -1){
									model.executeMove(new MovementMove(state, player2.getTokenColour(), firstNode, secondNode));
								}
								if(result == 1){
									mill = true;
								}
							}
						} else if(firstNode == -1 || secondNode == -1){
							result = -1;
						}
					}
				}
				if((phase.equals(Phase.TWO) || phase.equals(Phase.THREE)) && !mill && result != -1){
					if(turn == 'R'){
						turn = 'B';
					}else{
						turn = 'R';
					}
					model.setPhase(mc.getPhase());
					model.setTurn();
				}
				
				phase = mc.getPhase();
			}
			
		}

		private void humanClick(int position, Player player){
			String state = ((BoardDetailsInterface) model).getState();
			if(phase.equals(Phase.ONE) && (result == -1 || result == -2 || result == 0) && !mill){
				result = mc.addToken(player.getTokenColour(), position);
				if(result != -1){
					model.executeMove(new PlacementMove(state, player.getTokenColour(), position));
				}
				if(result == 1){
					mill = true;
					System.out.println("mill created");
				}
			}else if(mill && (result == -1 || result == 1)){
				result = mc.removeToken(player.getTokenColour(), position);
				if(result != -1){
					model.executeMove(new RemovalMove(state, player.getTokenColour(), position));
					mill = false;
				}
			}
			if(!mill && result != -1 && result != -2){
				if(turn == 'R'){
					turn = 'B';
				}else{
					turn = 'R';
				}
				model.setPhase(mc.getPhase());
				model.setTurn();
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

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
//			System.out.println("mmmeeeehhh");
//			int x = e.getX();
//			int y = e.getY();
//			int position = findNodeClicked(x, y);
//			if(position != -1){
//				if(phase.equals(Phase.ONE) && !mill){
//					System.out.println("Paiting..");
//					g = gameView.getGraphics();
//					g.setColor(Color.red);
//					g.fillOval(100, 100, 50, 50);
//					gameView.repaint();
//					
//				}
//			}
//			
		}

	}

}
