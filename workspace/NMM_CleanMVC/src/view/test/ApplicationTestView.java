package view.test;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ApplicationTestView extends JFrame{

	private static final long serialVersionUID = 2206472383834051420L;
	
	public ApplicationTestView(){
		setTitle("Test Rig - Nine Mens Morris");
		setSize(500, 500);
		setBackground(Color.WHITE);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void addPane(JPanel panel){
		this.getContentPane().add(panel);
	}
	
}
