package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

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
	
	public PlayingView(MouseListener m){
		super();
		setOpaque(false);
		setSize(800,600);
		setBackground(Color.white);
		
		addMouseListener(m);
		
		try {
			board = ImageIO.read(new File("src/NineMensMorris.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, 800, 600);
		g2.drawImage(board, 95, 0, null);
		
		int incrementY = 0;
		
		for (int i = 0; i < 9; i++) {
			
			g2.setColor(p1.darker());
			g2.fillOval(p1StartX, p1StartY + incrementY, tokenWidth, tokenWidth);
			g2.setColor(p1);
			g2.fillOval(p1StartX + 5, p1StartY + 5 + incrementY, tokenWidth - 10, tokenWidth - 10);
			
			g2.setColor(p2.darker());
			g2.fillOval(p2StartX, p2StartY + incrementY, tokenWidth, tokenWidth);
			g2.setColor(p2);
			g2.fillOval(p2StartX + 5, p2StartY + 5 + incrementY, tokenWidth - 10, tokenWidth - 10);
			
			incrementY += tokenWidth + 10;
		
		}
		
		super.paint(g2);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		//TODO - Add code in here that reads the input 
	}

}
