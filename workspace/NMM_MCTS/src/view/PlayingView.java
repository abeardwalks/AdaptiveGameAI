package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.board.BoardDetails;

public class PlayingView extends JPanel implements Observer {

	private static final long serialVersionUID = -68209173743986677L;
	
	private Image board;
	
	private final Color p1 = Color.ORANGE;
	private final Color p2 = Color.BLUE;
	
	private final int tokenWidth = 40;
	private final int p1StartX = 20;
	private final int p1StartY = 15;
	private final int p2StartX = 730;
	private final int p2StartY = 15;
	
	private String gs;
	private int result;
	private int playerOneTokens;
	private int playerTwoTokens;
	private char turn;
	
	private int x0,x1,x2,x3,x4,x5,x6;
	private int y0,y1,y2,y3,y4,y5,y6;
	
	private boolean showToolTip;
	private String toolTip;
	
	
	public PlayingView(){
		super();
		setOpaque(false);
		setSize(800,650);
		setBackground(Color.white);
		
		gs = "NNNNNNNNNNNNNNNNNNNNNNNN";
		result = -2;
		playerOneTokens = 9;
		playerTwoTokens = 9;
		turn = 'R';
		
		intializeCordinates();
		
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
		
		if(nodes[0] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x0, y0, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x0 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[0] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x0, y0, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x0 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		
		if(nodes[1] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y0, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[1] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y0, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[2] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x2, y0, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x2 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[2] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x2, y0, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x2 + 5, y0 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[3] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x3, y1, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x3 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[3] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x3, y1, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x3 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[4] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y1, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[4] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y1, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[5] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x4, y1, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x4 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[5] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x4, y1, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x4 + 5, y1 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[6] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x5, y2, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x5 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[6] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x5, y2, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x5 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[7] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y2, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[7] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y2, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[8] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x6, y2, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x6 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[8] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x6, y2, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x6 + 5, y2 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[9] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x0, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x0 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[9] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x0, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x0 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[10] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x3, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x3 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[10] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x3, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x3 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[11] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x5, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x5 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[11] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x5, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x5 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[12] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x6, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x6 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[12] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x6, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x6 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[13] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x4, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x4 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[13] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x4, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x4 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[14] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x2, y3, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x2 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[14] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x2, y3, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x2 + 5, y3 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[15] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x5, y4, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x5 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[15] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x5, y4, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x5 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[16] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y4, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[16] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y4, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[17] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x6, y4, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x6 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[17] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x6, y4, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x6 + 5, y4 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[18] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x3, y5, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x3 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[18] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x3, y5, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x3 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[19] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y5, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[19] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y5, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[20] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x4, y5, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x4 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[20] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x4, y5, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x4 + 5, y5 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[21] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x0, y6, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x0 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[21] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x0, y6, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x0 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[22] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x1, y6, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x1 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[22] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x1, y6, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x1 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		if(nodes[23] == 'R'){
			g2.setColor(p1.darker());
			g2.fillOval(x2, y6, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(x2 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		} else if(nodes[23] == 'B'){
			g2.setColor(p2.darker());
			g2.fillOval(x2, y6, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(x2 + 5, y6 + 5, tokenWidth - 10, tokenWidth - 10);
		}
		
			g2.setColor(Color.black);
		if(turn == 'R'){
			g2.fillOval(35, 540, tokenWidth, tokenWidth);
			g2.setColor(Color.gray);
			g2.fillOval(40, 545, tokenWidth - 10, tokenWidth - 10);
		}else{
			g2.fillOval(710, 540, tokenWidth, tokenWidth);
			g2.setColor(Color.gray);
			g2.fillOval(715, 545, tokenWidth - 10, tokenWidth - 10);
		}
		
		if(showToolTip){				//change to Show Tool Tip
			g2.drawString(toolTip, 25, 635);
		}
		
		super.paint(g2);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		BoardDetails bd = (BoardDetails)arg;
		gs = bd.getGS();
		result = bd.getResult();
		playerOneTokens = bd.getPlayerOneToPlace();
		playerTwoTokens = bd.getPlayerTwoToPlace();
		turn = bd.getTurn();
		
		if(result == 0 && turn == 'B'){
			if(playerTwoTokens == 0){
				toolTip = "Player Two (Blue) to move, please click and drag a blue token to an empty node...";
			}else{
				toolTip = "Player Two (Blue) to place, click an empty node to play...";
			}
		}else if(result == 0 || result == -2){
			if(playerOneTokens == 0){
				toolTip = "Player One (Orange) to move, please click and drag a orange token to an empty node...";
			}else{
				toolTip = "Player One (Orange) to place, click an empty node to play...";
			}
		}else if(result == 1 && turn == 'R'){
			toolTip = "Player One (Orange) to remove, please click on a Blue piece to remove it from the game...";
		}else if(result == 1){
			toolTip = "Player Two (Blue) to remove, please click on a Orange piece to remove it from the game...";
		}else if(result == 2){
			toolTip = "The Game has been Won, hazzah!";
		}
		
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

}
