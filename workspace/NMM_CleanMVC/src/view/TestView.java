package view;

import interfaces.BoardViewInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TestView extends JPanel implements Observer {

	
	@SuppressWarnings("unused")
	private Image board;
	private Font toolTipFont;
	
	private BoardViewInterface model;

	
	
	public TestView(BoardViewInterface model){
		super();
		setOpaque(false);
		setSize(800,650);
		setBackground(Color.white);
		toolTipFont = new Font("Arial", Font.BOLD, 20);
		setFont(toolTipFont);
		this.model = model;

	}

	public void paint(Graphics g){
		g.drawString("   Number of Games Played: " + model.getGamesPlayed() + " / " + model.getGamesToPlay(), 25, 200);
		g.drawString("          Player One Wins: " + model.getPlayerOneWins(), 25, 250);
		g.drawString("          Player Two Wins: " + model.getPlayerTwoWins(), 25, 300);
		g.drawString("                    Draws: " + model.getNumberOfDraws(), 25, 350);
		g.drawString("            Current State: " + model.getState(), 25, 400);
		g.drawString("      Player One To Place: " + ((BoardViewInterface) model).getPlayerOneToPlace(), 25, 450);
		g.drawString("      Player Two To Place: " + ((BoardViewInterface) model).getPlayerTwoToPlace(), 25, 500);
		g.drawString("     Player One Remaining: " + ((BoardViewInterface) model).getPlayerOneRemaining(), 25, 550);
		g.drawString("     Player Two Remaining: " + ((BoardViewInterface) model).getPlayerTwoRemaining(), 25, 600);
		
		
		super.paint(g);
	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

}
