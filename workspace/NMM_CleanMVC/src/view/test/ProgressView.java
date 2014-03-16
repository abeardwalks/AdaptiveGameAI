package view.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JPanel;

public class ProgressView extends JPanel {
	
	private static final long serialVersionUID = 4647989007797352133L;
	
	private GameProgressBar bar1, bar2, bar3, bar4, bar5;
	
	public ProgressView(GameProgressBar bar1, GameProgressBar bar2, GameProgressBar bar3, GameProgressBar bar4, GameProgressBar bar5){
		super();
		setOpaque(false);
		setSize(500,500);
		setBackground(Color.white);
		
		this.bar1 = bar1;
		this.bar2 = bar2;
		this.bar3 = bar3;
		this.bar4 = bar4;
		this.bar5 = bar5;
		
		fillView();
	}


	private void fillView() {
		this.setLayout(null);
	
		Insets insets = this.getInsets();
		
		bar1.setBounds(15 + insets.left, 15 + insets.top, 465, 50);
		bar1.setString("Test Set 1");
		bar1.setStringPainted(true);
		this.add(bar1);
		
		bar2.setBounds(15 + insets.left, 68 + insets.top, 465, 50);
		bar2.setString("Test Set 2");
		bar2.setStringPainted(true);
		this.add(bar2);
		
		bar3.setBounds(15 + insets.left, 121 + insets.top, 465, 50);
		bar3.setString("Test Set 3");
		bar3.setStringPainted(true);
		this.add(bar3);
		
		bar4.setBounds(15 + insets.left, 174 + insets.top, 465, 50);
		bar4.setString("Test Set 4");
		bar4.setStringPainted(true);
		this.add(bar4);
		
		bar5.setBounds(15 + insets.left, 227 + insets.top, 465, 50);
		bar5.setString("Test Set 5");
		bar5.setStringPainted(true);
		this.add(bar5);

	}
	
	

}
