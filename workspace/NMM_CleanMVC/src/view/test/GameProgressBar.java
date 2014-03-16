package view.test;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JProgressBar;

public class GameProgressBar extends JProgressBar implements Observer {

	private static final long serialVersionUID = 1616282876885391958L;
	
	public GameProgressBar(){
		super(0, 100);
		setValue(0);
		
	}


	@Override
	public void update(Observable o, Object arg) {
		setValue(getValue() + 1);
	}

}
