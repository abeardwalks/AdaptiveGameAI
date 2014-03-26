package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class PlayerViewMouseListener implements MouseListener, MouseMotionListener {

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("Mouse Pressed x/y: " + e.getX() + ", " + e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Mouse Released x/y: " + e.getX() + ", " + e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Mouse dragged x/y: " + e.getX() + ", " + e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {	
	}
}
