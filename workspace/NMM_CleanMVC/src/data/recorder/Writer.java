package data.recorder;

import interfaces.BoardViewInterface;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import move.AbstractMove;

public class Writer extends Observable{
	
	private int lineNumber;
	private BoardViewInterface model;
	private BufferedWriter bufferwriter;
	
	private BufferedReader reader;
	
	private FileWriter filewriter;
	private String filenumber;
	private String FILELOCATION = "C:\\Users\\Andy\\Documents\\CS408\\test-data" + filenumber + "-" + ".csv";
	private String HEADER = "src/header.txt";
	
	
	public Writer(BoardViewInterface model){
		this.model = model;
		lineNumber = 0;
		
		
		try {
			reader = new BufferedReader(new FileReader(HEADER));
			filenumber = reader.readLine();
			
			reader.close();
			filewriter = new FileWriter(FILELOCATION);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bufferwriter = new BufferedWriter(filewriter);
		
		PrintWriter pw = new PrintWriter(bufferwriter);
		
		
		String newline = "Game Number,Player One Win,Player Two Win,Player One Remaining,Player Two Remaining";
		pw.println(newline);
		lineNumber++;
		
	}
	
	
//	Game Number,Player One Win,Player Two Win,Player One Remaining,Player Two Remaining
	
	public Writer(BoardViewInterface model, String path, int runNumber,
			String p1, String p2) {
		this.model = model;
		lineNumber = 0;
		
		
		try {
			reader = new BufferedReader(new FileReader(HEADER));
			filenumber = reader.readLine();
			
			reader.close();
			filewriter = new FileWriter(path + "\\" + p1 + "-vs-" + p2 + "-" + runNumber + ".csv");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bufferwriter = new BufferedWriter(filewriter);
		
		PrintWriter pw = new PrintWriter(bufferwriter);
		
		
		String newline = "Game Number,Player One Win,Player Two Win,Player One Remaining,Player Two Remaining";
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
			notifyObservers(new Boolean(true));
		}else{
			notifyObservers(new Boolean(false));
		}
		return true;
	}
	
	public void closeBuffer(){
		try {
			
			bufferwriter.close();
			filewriter = new FileWriter("header.txt");
			int filenum = Integer.parseInt(filenumber);
			filenum++;
			bufferwriter = new BufferedWriter(filewriter);
			PrintWriter pw = new PrintWriter(bufferwriter);
			String newline = "" + filenum;
			pw.println(newline);
			bufferwriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
