package view;

import interfaces.GameStateInterface;
import interfaces.PlayerInterface;
import interfaces.PlayerInterface2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SetupView2 extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1229785926175762299L;
	
	private List<PlayerInterface2> players1;
	private List<PlayerInterface2> players2;
	
	private JLabel playerOneLabel, playerTwoLabel;
	private JComboBox<String> playerOneSelect, playerTwoSelect;
	private JButton start;
	private PlayerInterface2 playerOne, playerTwo;
	private Image background;

	public SetupView2(ActionListener a, List<PlayerInterface2> players12, List<PlayerInterface2> players22) {
		super();
		setOpaque(false);
		setSize(800,600);
		setBackground(Color.white);
		
		playerOneLabel = new JLabel("Player  1");
		playerTwoLabel = new JLabel("Player  2");
		playerOneSelect = new JComboBox<String>();
		playerTwoSelect = new JComboBox<String>();
		
		for (PlayerInterface2 p : players12) {
			playerOneSelect.addItem(p.getName());
		}
		
		for (PlayerInterface2 p : players22) {
			playerTwoSelect.addItem(p.getName());
		}
		playerOneSelect.setName("Player One");
		playerTwoSelect.setName("Player Two");
		playerOneSelect.addActionListener(this);
		playerTwoSelect.addActionListener(this);
		
		playerOne = players12.get(0);
		playerTwo = players22.get(0);
		
		start = new JButton("Start");
		start.addActionListener(a);
		
		fillView();
		this.players1 = players12;
		this.players2 = players22;
		
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
		g2.fillRect(0, 0, 800, 600);
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
	
	public PlayerInterface2 getPlayerOne(){
		return playerOne;
	}
	
	public PlayerInterface2 getPlayerTwo(){
		return playerTwo;
	}

}
