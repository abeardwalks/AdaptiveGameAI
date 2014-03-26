package view.test;

import interfaces.Player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class SetupTestView extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 5359371173459816852L;
	
	private List<Player> players1;
	private List<Player> players2;
	
	private JLabel playerOneLabel, playerTwoLabel, path;
	private JComboBox<String> playerOneSelect, playerTwoSelect;
	private JSpinner numberOfGames;
	private SpinnerNumberModel gamesRange;
	private JTextField filePath;
	private Player playerOne, playerTwo;
	private JButton start;
	
	public SetupTestView(ActionListener a, List<Player> players1, List<Player> players2){
		super();
		setOpaque(false);
		setSize(500, 500);
		setBackground(Color.white);
		
		playerOneLabel = new JLabel("Player  1");
		playerTwoLabel = new JLabel("Player  2");
		path = new JLabel("File Path");
		path.setToolTipText("Path to save results to.");
		
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
		
		gamesRange = new SpinnerNumberModel(100, 1, 500, 5);
		numberOfGames = new JSpinner(gamesRange);
		filePath = new JTextField();
		
		start = new JButton("Start Test");
		start.addActionListener(a);
		
		fillView();
		this.players1 = players1;
		this.players2 = players2;
		
	}
	
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.white);
		g2.fillRect(0, 0, 500, 500);
		
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
		playerOneLabel.setBounds(100 + insets.left, 100 + insets.top, size.width, size.height);
		playerOneSelect.setBounds(110 + size.width + insets.left, 100 + insets.top, size.width + 30, size.height + 5);
		playerTwoLabel.setBounds(260 + insets.left, 100 + insets.top, size.width, size.height);
		playerTwoSelect.setBounds(270 + size.width + insets.left, 100 + insets.top, size.width + 30, size.height + 5);
		
		size = path.getPreferredSize();
		path.setBounds(100 + insets.left, 160 + insets.top, size.width, size.height);
		this.add(path);
		
		size = filePath.getPreferredSize();
		filePath.setBounds(160 + insets.left, 160 + insets.top, size.width + 235, size.height);
		this.add(filePath);
		
		size = start.getPreferredSize();
		start.setBounds(215 + insets.left, 200 + insets.top, size.width, size.height);
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
	
	
	public Player getPlayerOne(){
		return playerOne;
	}
	
	public Player getPlayerTwo(){
		return playerTwo;
	}
	
	public String getPath(){
		return filePath.getText();
	}
	
	public int getNumberOfGames(){
		return (int) numberOfGames.getValue();
	}

}
