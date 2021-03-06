package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PauseView extends JPanel{

	private static final long serialVersionUID = 4854484671060436688L;
	
	private JButton resume, reset, setup, quit;
	
	public PauseView(ActionListener a){
		super();
		setSize(800,650);
		setOpaque(true);
		setBackground(new Color(255, 255, 255, 70));
		
		resume = new JButton("resume");
		resume.setActionCommand("resume");
		resume.addActionListener(a);
		
		reset = new JButton("reset");
		reset.setActionCommand("reset");
		reset.addActionListener(a);
		
		setup = new JButton("setup");
		setup.setActionCommand("setup");
		setup.addActionListener(a);
		
		quit = new JButton("quit");
		quit.setActionCommand("quit");
		quit.addActionListener(a);
		
		fillView();
		
	}

	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(255, 255, 255, 200));
		g2.fillRect(310, 225, 170, 200);
		super.paint(g2);
	}
	
	private void fillView() {
		this.setLayout(null);
		Insets insets = this.getInsets();
		
		Dimension size = resume.getPreferredSize();
		resume.setBounds(355 + insets.left, 260 + insets.top, size.width, size.height);
		this.add(resume);
		
		size = resume.getPreferredSize();
		reset.setBounds(355 + insets.left, 290 + insets.top, size.width, size.height);
		this.add(reset);
		
		size = resume.getPreferredSize();
		setup.setBounds(355 + insets.left, 320 + insets.top, size.width, size.height);
		this.add(setup);
		
		size = resume.getPreferredSize();
		quit.setBounds(355 + insets.left, 350 + insets.top, size.width, size.height);
		this.add(quit);
		
	}
}
