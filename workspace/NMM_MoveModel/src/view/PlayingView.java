package view;

import interfaces.BoardDetailsInterface;
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
	
	private BoardDetailsInterface model;
	
	private Graphics g;
	
	
	public PlayingView(BoardDetailsInterface model){
		super();
		setOpaque(false);
		setSize(800,650);
		setBackground(Color.white);
		toolTipFont = new Font("Arial", Font.BOLD, 20);
		setFont(toolTipFont);
		this.model = model;
		addMouseMotionListener(new PlayerViewMouseListener());
		addMouseListener(new PlayerViewMouseListener());
		gs = model.getState();
		playerOneTokens = model.getPlayerOneToPlace();
		playerTwoTokens = model.getPlayerTwoToPlace();
		
		intializeCordinates();
		initializeBeingDragged();
		
		toolTip = "Player One (Orange) to place, click an empty node to play...";
		
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
		this.g = g;
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
		
		if(nodes[0] == 'R' && !bd0){
			g2.setColor(p1.darker());
			g2.fillOval(x0, y0, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x0 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[0] == 'B' && !bd0){
			g2.setColor(p2.darker());
			g2.fillOval(x0, y0, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x0 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		
		if(nodes[1] == 'R' && !bd1){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y0, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[1] == 'B' && !bd1){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y0, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[2] == 'R' && !bd2){
			g2.setColor(p1.darker());
			g2.fillOval(x2, y0, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x2 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[2] == 'B' && !bd2){
			g2.setColor(p2.darker());
			g2.fillOval(x2, y0, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x2 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[3] == 'R' && !bd3){
			g2.setColor(p1.darker());
			g2.fillOval(x3, y1, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x3 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[3] == 'B' && !bd3){
			g2.setColor(p2.darker());
			g2.fillOval(x3, y1, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x3 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[4] == 'R' && !bd4){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y1, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[4] == 'B' && !bd4){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y1, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[5] == 'R' && !bd5){
			g2.setColor(p1.darker());
			g2.fillOval(x4, y1, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x4 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[5] == 'B' && !bd5){
			g2.setColor(p2.darker());
			g2.fillOval(x4, y1, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x4 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[6] == 'R' && !bd6){
			g2.setColor(p1.darker());
			g2.fillOval(x5, y2, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x5 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[6] == 'B' && !bd6){
			g2.setColor(p2.darker());
			g2.fillOval(x5, y2, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x5 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[7] == 'R' && !bd7){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y2, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[7] == 'B' && !bd7){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y2, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[8] == 'R' && !bd8){
			g2.setColor(p1.darker());
			g2.fillOval(x6, y2, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x6 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[8] == 'B' && !bd8){
			g2.setColor(p2.darker());
			g2.fillOval(x6, y2, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x6 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[9] == 'R' && !bd9){
			g2.setColor(p1.darker());
			g2.fillOval(x0, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x0 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[9] == 'B' && !bd9){
			g2.setColor(p2.darker());
			g2.fillOval(x0, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x0 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[10] == 'R' && !bd10){
			g2.setColor(p1.darker());
			g2.fillOval(x3, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x3 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[10] == 'B' && !bd10){
			g2.setColor(p2.darker());
			g2.fillOval(x3, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x3 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[11] == 'R' && !bd11){
			g2.setColor(p1.darker());
			g2.fillOval(x5, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x5 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[11] == 'B' && !bd11){
			g2.setColor(p2.darker());
			g2.fillOval(x5, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x5 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[12] == 'R' && !bd12){
			g2.setColor(p1.darker());
			g2.fillOval(x6, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x6 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[12] == 'B' && !bd12){
			g2.setColor(p2.darker());
			g2.fillOval(x6, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x6 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[13] == 'R' && !bd13){
			g2.setColor(p1.darker());
			g2.fillOval(x4, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x4 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[13] == 'B' && !bd13){
			g2.setColor(p2.darker());
			g2.fillOval(x4, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x4 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[14] == 'R' && !bd14){
			g2.setColor(p1.darker());
			g2.fillOval(x2, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x2 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[14] == 'B' && !bd14){
			g2.setColor(p2.darker());
			g2.fillOval(x2, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x2 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[15] == 'R' && !bd15){
			g2.setColor(p1.darker());
			g2.fillOval(x5, y4, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x5 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[15] == 'B' && !bd15){
			g2.setColor(p2.darker());
			g2.fillOval(x5, y4, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x5 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[16] == 'R' && !bd16){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y4, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[16] == 'B' && !bd16){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y4, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[17] == 'R' && !bd17){
			g2.setColor(p1.darker());
			g2.fillOval(x6, y4, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x6 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[17] == 'B' && !bd17){
			g2.setColor(p2.darker());
			g2.fillOval(x6, y4, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x6 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[18] == 'R' && !bd18){
			g2.setColor(p1.darker());
			g2.fillOval(x3, y5, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x3 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[18] == 'B' && !bd18){
			g2.setColor(p2.darker());
			g2.fillOval(x3, y5, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x3 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[19] == 'R' && !bd19){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y5, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[19] == 'B' && !bd19){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y5, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[20] == 'R' && !bd20){
			g2.setColor(p1.darker());
			g2.fillOval(x4, y5, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x4 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[20] == 'B' && !bd20){
			g2.setColor(p2.darker());
			g2.fillOval(x4, y5, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x4 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[21] == 'R' && !bd21){
			g2.setColor(p1.darker());
			g2.fillOval(x0, y6, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x0 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[21] == 'B' && !bd21){
			g2.setColor(p2.darker());
			g2.fillOval(x0, y6, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x0 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[22] == 'R' && !bd22){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y6, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[22] == 'B' && !bd22){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y6, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[23] == 'R' && !bd23){
			g2.setColor(p1.darker());
			g2.fillOval(x2, y6, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x2 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[23] == 'B' && !bd23){
			g2.setColor(p2.darker());
			g2.fillOval(x2, y6, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x2 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
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
		
		if(dragging && model.getNextAction() == 'M'){
			if(token == 'R'){
				g2.setColor(p1.darker());
				g2.fillOval(x - (tokenWidth/2), y - (tokenWidth/2), tokenWidth, tokenWidth);
				g2.setColor(p1);
				g2.fillOval(x + 5 - (tokenWidth/2), y + 5 - (tokenWidth/2), tokenWidth - 10, tokenWidth - 10);
			}else if(token == 'B'){
				g2.setColor(p2.darker());
				g2.fillOval(x - (tokenWidth/2), y - (tokenWidth/2), tokenWidth, tokenWidth);
				g2.setColor(p2);
				g2.fillOval(x + 5 - (tokenWidth/2), y + 5 - (tokenWidth/2), tokenWidth - 10, tokenWidth - 10);
			}
			
		}
		
		super.paint(g2);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		gs = model.getState();
		System.out.println("Updated, new state: " + gs);
//		result = bd.getResult();
		playerOneTokens = model.getPlayerOneToPlace();
		playerTwoTokens = model.getPlayerTwoToPlace();
		
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
				toolTip = "Player One (Orange) to remove, please click on a Blue piece to remove it from the game...";
			}else{
				toolTip = "Player Two (Blue) to remove, please click on a Orange piece to remove it from the game...";
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
//		((BoardFacadeInterface)model).printDetails();
		repaint();
	}
	
	private void intializeCordinates() {
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
	
	private void initializeBeingDragged() {
		 bd0 = false;
		 bd1 = false;
		 bd2 = false;
		 bd3 = false;
		 bd4 = false;
		 bd5 = false;
		 bd6 = false;
		 bd7 = false;
		 bd8 = false; 
		 bd9 = false;
		bd10 = false; 
		bd11 = false;
		bd12 = false;
		bd13 = false;
		bd14 = false;
		bd15 = false;
		bd16 = false;
		bd17 = false;
		bd18 = false;
		bd19 = false;
		bd20 = false;
		bd21 = false;
		bd22 = false;
		bd23 = false;
	}

	private boolean dragging;
	private int x, y;
	private char token;
	
	private class PlayerViewMouseListener implements  MouseMotionListener, MouseListener {
		
		
		private NodeFinder finder;
		private int node;
		
		public PlayerViewMouseListener(){
			finder = new NodeFinder();
			dragging = false;
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			dragging = true;
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
			switch (node) {
			case 0:
				bd0 = false;
				break;
			case 1:
				bd1 = false;
				break;
			case 2:
				bd2 = false;
				break;
			case 3:
				bd3 = false;
				break;
			case 4:
				bd4 = false;
				break;
			case 5:
				bd5 = false;
				break;
			case 6:
				bd6 = false;
				break;
			case 7:
				bd7 = false;
				break;
			case 8:
				bd8 = false;
				break;
			case 9:
				bd9 = false;
				break;
			case 10:
				bd10 = false;
				break;
			case 11:
				bd11 = false;
				break;
			case 12:
				bd12 = false;
				break;
			case 13:
				bd13 = false;
				break;
			case 14:
				bd14 = false;
				break;
			case 15:
				bd15 = false;
				break;
			case 16:
				bd16 = false;
				break;
			case 17:
				bd17 = false;
				break;
			case 18:
				bd18 = false;
				break;
			case 19:
				bd19 = false;
				break;
			case 20:
				bd20 = false;
				break;
			case 21:
				bd21 = false;
				break;
			case 22:
				bd22 = false;
				break;
			case 23:
				bd23 = false;
				break;
			default:
				break;
			}
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			x = e.getX();
			y = e.getY();
			node = finder.getNode(x, y);
			if(node != -1){
				token = model.getState().charAt(node);
			}
			dragging = true;
			switch (node) {
			case 0:
				bd0 = true;
				break;
			case 1:
				bd1 = true;
				break;
			case 2:
				bd2 = true;
				break;
			case 3:
				bd3 = true;
				break;
			case 4:
				bd4 = true;
				break;
			case 5:
				bd5 = true;
				break;
			case 6:
				bd6 = true;
				break;
			case 7:
				bd7 = true;
				break;
			case 8:
				bd8 = true;
				break;
			case 9:
				bd9 = true;
				break;
			case 10:
				bd10 = true;
				break;
			case 11:
				bd11 = true;
				break;
			case 12:
				bd12 = true;
				break;
			case 13:
				bd13 = true;
				break;
			case 14:
				bd14 = true;
				break;
			case 15:
				bd15 = true;
				break;
			case 16:
				bd16 = true;
				break;
			case 17:
				bd17 = true;
				break;
			case 18:
				bd18 = true;
				break;
			case 19:
				bd19 = true;
				break;
			case 20:
				bd20 = true;
				break;
			case 21:
				bd21 = true;
				break;
			case 22:
				bd22 = true;
				break;
			case 23:
				bd23 = true;
				break;
			default:
				break;
			}
		}
	}

}
