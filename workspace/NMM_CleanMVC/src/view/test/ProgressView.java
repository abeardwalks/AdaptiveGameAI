package view.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProgressView extends JPanel implements Observer{
	
	private static final long serialVersionUID = 4647989007797352133L;
	
	private GameProgressBar bar1, bar2, bar3, bar4, bar5;
	
	private JLabel player1, player2, vs, player1Wins, player2Wins;
	private int player1WinCount, player2WinCount;
	
	public ProgressView(GameProgressBar bar1, GameProgressBar bar2, GameProgressBar bar3, GameProgressBar bar4, GameProgressBar bar5, String player1, String player2){
		super();
		setOpaque(true);
		setSize(500,500);
		setBackground(Color.white);
		
		this.bar1 = bar1;
		this.bar2 = bar2;
		this.bar3 = bar3;
		this.bar4 = bar4;
		this.bar5 = bar5;
		
		this.player1 = new JLabel(player1);
		this.player2 = new JLabel(player2);
		vs = new JLabel(player1 + "  vs  " + player2);
		vs.setFont(new Font("Arial", Font.BOLD, 20));
		player1Wins = new JLabel("0");
		player1Wins.setFont(new Font("Arial", Font.BOLD, 20));
		player2Wins = new JLabel("0");
		player2Wins.setFont(new Font("Arial", Font.BOLD, 20));
		player1WinCount = 0;
		player2WinCount = 0;
		
		this.addMouseListener(new ml());
		
		fillView();
	}


	private void fillView() {
		this.setLayout(null);
	
		Insets insets = this.getInsets();
		
		bar1.setBounds(15 + insets.left, 15 + insets.top, 465, 50);
		bar1.setString("Test Set 1");
		bar1.setStringPainted(true);
		bar1.setBackground(Color.white);
		bar1.setForeground(Color.black);
		this.add(bar1);
		
		bar2.setBounds(15 + insets.left, 68 + insets.top, 465, 50);
		bar2.setString("Test Set 2");
		bar2.setStringPainted(true);
		bar2.setBackground(Color.white);
		bar2.setForeground(Color.black);
		this.add(bar2);
		
		bar3.setBounds(15 + insets.left, 121 + insets.top, 465, 50);
		bar3.setString("Test Set 3");
		bar3.setStringPainted(true);
		bar3.setBackground(Color.white);
		bar3.setForeground(Color.black);
		this.add(bar3);
		
		bar4.setBounds(15 + insets.left, 174 + insets.top, 465, 50);
		bar4.setString("Test Set 4");
		bar4.setStringPainted(true);
		bar4.setBackground(Color.white);
		bar4.setForeground(Color.black);
		this.add(bar4);
		
		bar5.setBounds(15 + insets.left, 227 + insets.top, 465, 50);
		bar5.setString("Test Set 5");
		bar5.setStringPainted(true);
		bar5.setBackground(Color.white);
		bar5.setForeground(Color.black);
		this.add(bar5);
		
		vs.setBounds(50 + insets.left, 315 + insets.top, 465,50);
		this.add(vs);
		
		player1Wins.setBounds(71 + insets.left, 360 + insets.top, 465,50);
		this.add(player1Wins);
		
		player2Wins.setBounds(177 + insets.left, 360 + insets.top, 465, 50);
		this.add(player2Wins);

	}


	@Override
	public void update(Observable arg0, Object arg1) {
		if((Boolean)arg1 == true){
			player1WinCount++;
		}else{
			player2WinCount++;
		}
		
		player1Wins.setText(String.valueOf(player1WinCount));
		player2Wins.setText(String.valueOf(player2WinCount));
	}
	
	private class ml implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			System.err.println("Mouse X:" + e.getX());
			System.err.println("Mouse Y:" + e.getY());
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
