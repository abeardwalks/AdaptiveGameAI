package view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
		addMouseListener(new ml());
		System.out.println();
	}

	public void addPane(JPanel panel){
		this.getContentPane().add(panel);
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
