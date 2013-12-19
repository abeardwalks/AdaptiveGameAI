package view;

import interfaces.GameStateInterface;
import interfaces.PlayerInterface;

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

public class SetupView extends JPanel implements ActionListener, MouseListener{
	
	private static final long serialVersionUID = 1229785926175762299L;
	
	private List<PlayerInterface> players;
	
	private JLabel playerOneLabel, playerTwoLabel;
	private JComboBox<String> playerOneSelect, playerTwoSelect;
	private JButton start;
	private PlayerInterface playerOne, playerTwo;
	private Image background;

	public SetupView(ActionListener a, List<PlayerInterface> players) {
		super();
		setOpaque(false);
		setSize(800,600);
		setBackground(Color.white);
		
		playerOneLabel = new JLabel("Player  1");
		playerTwoLabel = new JLabel("Player  2");
		playerOneSelect = new JComboBox<String>();
		playerTwoSelect = new JComboBox<String>();
		
		for (PlayerInterface p : players) {
			playerOneSelect.addItem(p.getName());
			playerTwoSelect.addItem(p.getName());
		}
		playerOneSelect.setName("Player One");
		playerTwoSelect.setName("Player Two");
		playerOneSelect.addActionListener(this);
		playerTwoSelect.addActionListener(this);
		
		playerOne = players.get(0);
		playerTwo = players.get(0);
		
		start = new JButton("Start");
		start.addActionListener(a);
		
		fillView();
		this.addMouseListener(this);
		this.players = players;
		
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
			playerOne = players.get(selectie.getSelectedIndex());
		} else{
			playerTwo = players.get(selectie.getSelectedIndex());
		}
	}
	
	public PlayerInterface getPlayerOne(){
		return playerOne;
	}
	
	public PlayerInterface getPlayerTwo(){
		return playerTwo;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getX());
		System.out.println(e.getY());
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
