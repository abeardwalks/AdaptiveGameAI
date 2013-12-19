package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import interfaces.GameStateInterface;
import interfaces.PlayerInterface;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ApplicationView extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1892187361371589841L;
//	private Image background;

	public ApplicationView(){
		setTitle("Nine Mens Morris");
		setSize(800, 625);
		setBackground(Color.WHITE);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		try {
//			background = ImageIO.read(new File("src/NineMensMorris.jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	public void addPane(JPanel panel){
		this.getContentPane().add(panel);
	}
	
//	public void paint(Graphics g){
////		g.drawImage(background, 0, 30, null);
////		super.paint(g);
////		System.out.println("Height: " + this.getSize().height + ", Width:" + this.getSize().width);
//	}

}
