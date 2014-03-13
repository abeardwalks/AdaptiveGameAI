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

import players.Human;
import players.IntPair;

import utility.NodeFinder;

public class PlayingView extends JPanel implements Observer {

	private static final long serialVersionUID = -68209173743986677L;
	
	private Image board;
	private Font toolTipFont;
	
	private final Color p1 = Color.ORANGE;
	private final Color p2 = Color.BLUE;
	
	private final int tokenWidth = 40;
	private final int p1StartX = 20;
	private final int p1StartY = 15;
	private final int p2StartX = 730;
	private final int p2StartY = 15;
	
	private String gs;
	private int playerOneTokens;
	private int playerTwoTokens;
	
	private int x0,x1,x2,x3,x4,x5,x6;
	private int y0,y1,y2,y3,y4,y5,y6;
	
	private boolean  bd0, bd1, bd2, bd3, bd4, 
					 bd5, bd6, bd7, bd8, bd9, 
					bd10, bd11, bd12, bd13, 
					bd14, bd15, bd16, bd17, 
					bd18, bd19, bd20, bd21, 
					bd22, bd23;
	
	private boolean showToolTip;
	private String toolTip;
	
	private BoardViewInterface model;
	
	private boolean playerOneHuman, playerTwoHuman;

	private int dragCandidate;
	
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
		gs = model.getState();
		playerOneTokens = model.getPlayerOneToPlace();
		playerTwoTokens = model.getPlayerTwoToPlace();
		
		if(model.getPlayerOne() instanceof Human){
			playerOneHuman = true;
		}
		
		if(model.getPlayerTwo() instanceof Human){
			playerTwoHuman = true;
		}
		
		toolTip = "Player One (Orange) to place, click an empty node to play...";
		
		finder = new NodeFinder();
		
		try {
			board = ImageIO.read(new File("src/NineMensMorris.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void setToolTip(boolean showToolTip){
		this.showToolTip = showToolTip;
	}


	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, 800, 650);
		g2.drawImage(board, 95, 0, null);
		
		int incrementY = 0;
		
		for (int i = 0; i < playerOneTokens; i++) {
			g2.setColor(p1.darker());
			g2.fillOval(p1StartX, p1StartY + incrementY, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(p1StartX + 5, p1StartY + 5 + incrementY, tokenWidth - 10, tokenWidth - 10);
		
			incrementY += tokenWidth + 10;
		}
		
		incrementY = 0;
		
		for (int i = 0; i < playerTwoTokens; i++) {
			g2.setColor(p2.darker());
			g2.fillOval(p2StartX, p2StartY + incrementY, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(p2StartX + 5, p2StartY + 5 + incrementY, tokenWidth - 10, tokenWidth - 10);
			
			incrementY += tokenWidth + 10;
		}
		
		char[] nodes = gs.toCharArray();
		
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
		
		if(dragging && model.getNextAction() == 'M'){
			System.out.println("DRAGGING: " + x + ", " + y);
			if(model.getTurn() == 1){
				g.setColor(p1.darker());
				g.fillOval(x - (tokenWidth/2), y - (tokenWidth/2), tokenWidth, tokenWidth);
				g.setColor(p1);
				g.fillOval(x + 5 - (tokenWidth/2), y + 5 - (tokenWidth/2), tokenWidth - 10, tokenWidth - 10);
			}else if(model.getTurn() == 2){
				g.setColor(p2.darker());
				g.fillOval(x - (tokenWidth/2), y - (tokenWidth/2), tokenWidth, tokenWidth);
				g.setColor(p2);
				g.fillOval(x + 5 - (tokenWidth/2), y + 5 - (tokenWidth/2), tokenWidth - 10, tokenWidth - 10);
			}
			
		}
		
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
		
		if(showToolTip){				//change to Show Tool Tip
			g2.drawString(toolTip, 25, 630);
		}
		
		super.paint(g2);
		
	}
	
	public Graphics2D paintTokens(Graphics2D g){
		
		return g;
	}

	@Override
	public void update(Observable o, Object arg) {
		gs = model.getState();
		System.out.println("Updated, new state: " + gs);
//		result = bd.getResult();
		playerOneTokens = model.getPlayerOneToPlace();
		playerTwoTokens = model.getPlayerTwoToPlace();
		
		if(model.getPlayerOne() instanceof Human){
			playerOneHuman = true;
		}
		
		if(model.getPlayerTwo() instanceof Human){
			playerTwoHuman = true;
		}
		
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
	
	

	private boolean dragging;
	private int x, y;
	private char token;
	private NodeFinder finder;
	
	private class PlayerViewMouseListener implements  MouseMotionListener, MouseListener {
				
		public PlayerViewMouseListener(){
			dragging = false;
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if(model.getNextAction() == 'M'){
				x = e.getX();
				y = e.getY();
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
			x = e.getX();
			y = e.getY();
			char dragTokens = 'R';
			if(model.getTurn() == 2){
				dragTokens = 'B';
			}
			
			dragCandidate = finder.getNode(x, y);
			if(dragCandidate != -1){
				token = model.getState().charAt(dragCandidate);
				if(token == dragTokens){
					dragging = true;
				}else{
					dragging = false;
					dragCandidate = -1;
				}
			}
		}
	}

}
