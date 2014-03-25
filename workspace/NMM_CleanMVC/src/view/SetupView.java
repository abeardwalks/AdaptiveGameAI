package view;

import interfaces.Player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SetupView extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1229785926175762299L;
	
	private List<Player> players1;
	private List<Player> players2;
	
	private JLabel playerOneLabel, playerTwoLabel;
	private JComboBox<String> playerOneSelect, playerTwoSelect;
	private JButton start;
	private JCheckBox toolTip, testRig;
	private Player playerOne, playerTwo;
	private Image background;

	public SetupView(ActionListener a, List<Player> players1, List<Player> players2) {
		super();
		setOpaque(false);
		setSize(800,650);
		setBackground(Color.white);
		
		playerOneLabel = new JLabel("Player  1");
		playerTwoLabel = new JLabel("Player  2");
		playerOneSelect = new JComboBox<String>();
		playerTwoSelect = new JComboBox<String>();
		
		for (Player p : players1) {
			playerOneSelect.addItem(p.getName());
		}
		
		for (Player p : players2) {
			playerTwoSelect.addItem(p.getName());
		}
		playerOneSelect.setName("Player One");
		playerTwoSelect.setName("Player Two");
		playerOneSelect.addActionListener(this);
		playerTwoSelect.addActionListener(this);
		
		playerOne = players1.get(0);
		playerTwo = players2.get(0);
		
		start = new JButton("Start");
		start.addActionListener(a);
		
		toolTip = new JCheckBox("Tooltips");
		toolTip.setBackground(Color.white);
		testRig = new JCheckBox("Test Rig");
		testRig.setBackground(Color.white);
		
		
		fillView();
		this.players1 = players1;
		this.players2 = players2;
		
		try {
			background = ImageIO.read(new File("src/NineMensMorris.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, 800, 650);
		g2.drawImage(background, 95, 0, null);
		
		super.paint(g2);
	}


	private void fillView() {
		this.setLayout(null);
		
		
		
		this.add(playerOneLabel);
		this.add(playerOneSelect);
		this.add(playerTwoLabel);
		this.add(playerTwoSelect);
		
		Insets insets = this.getInsets();
		Dimension size = playerOneLabel.getPreferredSize();
		playerOneLabel.setBounds(235 + insets.left, 155 + insets.top, size.width, size.height);
		playerOneSelect.setBounds(245 + size.width + insets.left, 155 + insets.top, size.width + 30, size.height + 5);
		playerTwoLabel.setBounds(415 + insets.left, 155 + insets.top, size.width, size.height);
		playerTwoSelect.setBounds(427 + size.width + insets.left, 155 + insets.top, size.width + 30, size.height + 5);
		
		size = start.getPreferredSize();
		start.setBounds(365 + insets.left, 280 + insets.top, size.width, size.height);
		
		this.add(start);
		
		size = toolTip.getPreferredSize();
		toolTip.setBounds(360 + insets.left, 340 + insets.top, size.width, size.height);
		this.add(toolTip);
		
//		size = testRig.getPreferredSize();
//		testRig.setBounds(360 + insets.left, 370 + insets.top, size.width, size.height);
//		this.add(testRig);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<String> selectie = (JComboBox<String>)e.getSource();
		
		if(selectie.getName().equals("Player One")){
			playerOne = players1.get(selectie.getSelectedIndex());
		} else{
			playerTwo = players2.get(selectie.getSelectedIndex());
		}
	}
	
	public Player getPlayerOne(){
		return playerOne;
	}
	
	public Player getPlayerTwo(){
		return playerTwo;
	}
	
	public boolean getShowToolTip(){
		return toolTip.isSelected();
	}
	
	public boolean getTestRig(){
		return testRig.isSelected();
	}


}
