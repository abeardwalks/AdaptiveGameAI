package view;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ApplicationView extends JFrame {
	
	private static final long serialVersionUID = -1892187361371589841L;

	public ApplicationView(){
		setTitle("Nine Mens Morris");
		setSize(800, 670);
		setBackground(Color.WHITE);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void addPane(JPanel panel){
		this.getContentPane().add(panel);
	}

}
