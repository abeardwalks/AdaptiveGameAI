package view;

import interfaces.BoardViewInterface;
import interfaces.BoardFacadeInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import players.IntPair;

import utility.NodeFinder;

public class PlayingView extends JPanel implements Observer {
	
	private static final long serialVersionUID = -5435569886930663623L;
	private Image board;
	private Font toolTipFont;
	
	private final Color p1 = Color.ORANGE;
	private final Color p2 = Color.BLUE;
	
	private final int tokenWidth = 40;
	private final int p1StartX = 20;
	private final int p1StartY = 15;
	private final int p2StartX = 730;
	private final int p2StartY = 15;
	
	private boolean showToolTip;
	private String toolTip;
	
	private BoardViewInterface model;
	
	private int dragCandidate;
	private boolean dragging;
	private int dragX, dragY;
	private char dragToken;
	
	private NodeFinder finder;
	
	public PlayingView(BoardViewInterface model){
		super();
		setOpaque(false);
		setSize(800,650);
		setBackground(Color.white);
		toolTipFont = new Font("Arial", Font.BOLD, 17);
		
		setFont(toolTipFont);
		this.model = model;
		addMouseMotionListener(new PlayerViewMouseListener());
		addMouseListener(new PlayerViewMouseListener());
		
		toolTip = "Player One (Orange) to place, click an empty node to play...";
		
		finder = new NodeFinder();
		
		try {
			board = ImageIO.read(new File("src/NineMensMorris.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setToolTip(boolean showToolTip){
		this.showToolTip = showToolTip;
	}


	public void paint(Graphics g){
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, 800, 650);
		
		//draw the board.
		g2.drawImage(board, 95, 0, null);
		
		int incrementY = 0;
		
		//Draw player Ones tokens to place.
		for (int i = 0; i < model.getPlayerOneToPlace(); i++) {
			g2.setColor(p1.darker());
			g2.fillOval(p1StartX, p1StartY + incrementY, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(p1StartX + 5, p1StartY + 5 + incrementY, tokenWidth - 10, tokenWidth - 10);
		
			incrementY += tokenWidth + 10;
		}
		
		incrementY = 0;
		
		//Draw player Twos tokens to place.
		for (int i = 0; i < model.getPlayerTwoToPlace(); i++) {
			g2.setColor(p2.darker());
			g2.fillOval(p2StartX, p2StartY + incrementY, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(p2StartX + 5, p2StartY + 5 + incrementY, tokenWidth - 10, tokenWidth - 10);
			
			incrementY += tokenWidth + 10;
		}
		
		
		//Draw the state string onto the board.
		int node = 0;
		for (char n : model.getState().toCharArray()) {
			IntPair cords = finder.getCordinates(node);
			int x = cords.getFirstInt();
			int y = cords.getSecondInt();
			if(n == 'R' && (dragCandidate != node)){
				g.setColor(p1.darker());
				g.fillOval(x, y, tokenWidth, tokenWidth);
				g.setColor(p1);
				g.fillOval(x + 5, y + 5, tokenWidth - 10, tokenWidth - 10);
			} else if(n == 'B' && dragCandidate != node){
				g.setColor(p2.darker());
				g.fillOval(x, y, tokenWidth, tokenWidth);
				g.setColor(p2);
				g.fillOval(x + 5, y + 5, tokenWidth - 10, tokenWidth - 10);
			}
			node++;
		}
		
		//If players are dragging a token, draw it to follow the mouse.
		if(dragging && model.getNextAction() == 'M'){
			if(model.getTurn() == 1){
				g.setColor(p1.darker());
				g.fillOval(dragX - (tokenWidth/2), dragY - (tokenWidth/2), tokenWidth, tokenWidth);
				g.setColor(p1);
				g.fillOval(dragX + 5 - (tokenWidth/2), dragY + 5 - (tokenWidth/2), tokenWidth - 10, tokenWidth - 10);
			}else if(model.getTurn() == 2){
				g.setColor(p2.darker());
				g.fillOval(dragX - (tokenWidth/2), dragY - (tokenWidth/2), tokenWidth, tokenWidth);
				g.setColor(p2);
				g.fillOval(dragX + 5 - (tokenWidth/2), dragY + 5 - (tokenWidth/2), tokenWidth - 10, tokenWidth - 10);
			}
			
		}
		
		//draw the Turn indication token
		g2.setColor(Color.black);
		if(model.getTurn() == 1){
			g2.fillOval(35, 540, tokenWidth, tokenWidth);
			g2.setColor(Color.gray);
			g2.fillOval(40, 545, tokenWidth - 10, tokenWidth - 10);
		}else{
			g2.fillOval(710, 540, tokenWidth, tokenWidth);
			g2.setColor(Color.gray);
			g2.fillOval(715, 545, tokenWidth - 10, tokenWidth - 10);
		}
		
		//draw the tool tip.
		if(showToolTip){				
			g2.drawString(toolTip, 25, 630);
		}
		
		super.paint(g2);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		
		switch (model.getNextAction()) {
		case 'P':
			if(model.getTurn() == 1){
				toolTip = "Player One (Orange) to place, click an empty node to play...";
			}else{
				toolTip = "Player Two (Blue) to place, click an empty node to play...";
			}
			break;
		case 'R':
			if(model.getTurn() == 1){
				toolTip = "Player One (Orange) to remove, please click on a \n Blue piece to remove it from the game...";
			}else{
				toolTip = "Player Two (Blue) to remove, please click on an Orange piece to remove it from the game...";
			}
			break;
		case 'M':
			if(model.getTurn() == 1){
				toolTip = "Player One (Orange) to move, please click and drag a orange token to an empty node...";
			}else{
				toolTip = "Player Two (Blue) to move, please click and drag a blue token to an empty node...";
			}
			break;
		default:
			break;
		}
		
		if(model.gameWon()){
			toolTip = "The Game has been won, hazzah!";
		}
		((BoardFacadeInterface)model).printDetails();
		repaint();
	}
	
	private class PlayerViewMouseListener implements  MouseMotionListener, MouseListener {
				
		public PlayerViewMouseListener(){
			dragging = false;
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(model.getNextAction() == 'M'){
				dragX = e.getX();
				dragY = e.getY();
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {	
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			dragging = false;
			dragCandidate = -1;
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			dragX = e.getX();
			dragY = e.getY();
			char dragTokens = 'R';
			if(model.getTurn() == 2){
				dragTokens = 'B';
			}
			
			dragCandidate = finder.getNode(dragX, dragY);
			if(dragCandidate != -1){
				dragToken = model.getState().charAt(dragCandidate);
				if(dragToken == dragTokens){
					dragging = true;
				}else{
					dragging = false;
					dragCandidate = -1;
				}
			}
		}
	}

}
