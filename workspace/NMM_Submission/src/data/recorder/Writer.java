package data.recorder;

import interfaces.BoardViewInterface;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Stack;

import move.AbstractMove;

/**
 * This writer writes the following game data to a file at the end of a game:
 * 		- Game number,
 * 		- Player one win? (True/False)
 * 		- Player two win? (True/False)
 * 		- Player one tokens remaining,
 * 		- Player two tokens remaining,
 * 		- End Phase,
 * 		- Last Board State,
 * 		- Removal History,
 * 		- Removal values (for plotting). 
 * 
 * It writes this data to .csv files, to the passed in path. The filename given is made up of the 
 * player one name, player two name and the thread number (between 0-4, inclusive). 
 * 
 * @author Andrew White - BSc Software Engineering, 200939787
 *
 */
public class Writer extends Observable{
	
	private int lineNumber;
	private BoardViewInterface model;
	private BufferedWriter bufferwriter;	
	private FileWriter filewriter;
	
	public Writer(BoardViewInterface model, String path, int runNumber, String p1, String p2) {
		
		this.model = model;
		lineNumber = 0;
		
		try {
			filewriter = new FileWriter(path + "\\" + p1 + "-vs-" + p2 + "-" + runNumber + ".csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bufferwriter = new BufferedWriter(filewriter);
		PrintWriter pw = new PrintWriter(bufferwriter);
		
		
		String newline = "Game Number,Player One Win,Player Two Win,Player One Remaining,Player Two Remaining, End Phase, Last Board State, Removal History, r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13";
		pw.println(newline);
		lineNumber++;
		
	}


	public boolean writeline(){
		String line = "" + lineNumber + "," + model.playerOneWin() + "," + model.playerTwoWin() + "," + model.getPlayerOneRemaining() + "," + model.getPlayerTwoRemaining() +  ", " + model.getPhase() + ", " + model.getState();
		Stack<AbstractMove> history = model.getHistory();
		
		String plotPoints = "";
		String removeHistory = "";
		int count = 0;
		int value = 0;
		
		//Constructs the removal history.
		while(!history.isEmpty()){
			AbstractMove m = history.pop();
			if(m.getAction() == 'R'){
				removeHistory = m.getPlayerColour() + removeHistory;
				if(m.getPlayerColour() == 'R'){
					value++;
				}else{
					value--;
				}
				plotPoints += "," + value;
				count++;
			}
			
		}
		//pads removal history to the end.
		while(count < 13){
			plotPoints += "," + value;
			count++;
		}
		line = line + "," + removeHistory + plotPoints;
		PrintWriter pw = new PrintWriter(bufferwriter);
		
		pw.println(line);
		
		lineNumber++;
		
		setChanged();
		if(model.playerOneWin()){
			notifyObservers(new Boolean(true));		//notify observers, player one win.
		}else{
			notifyObservers(new Boolean(false));	//notify observers, player two win.
		}
		return true;
	}
	
	public void closeBuffer(){
		try {
			bufferwriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
