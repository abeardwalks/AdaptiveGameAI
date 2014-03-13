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

public class Writer implements Observer{
	
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
	
	public void writeline(){
		
		PrintWriter pw = new PrintWriter(bufferwriter);
		String newline = "" + lineNumber + "," + model.playerOneWin()
										 + "," + model.playerTwoWin()
										 + "," + model.getPlayerOneRemaining()
										 + "," + model.getPlayerTwoRemaining();
		System.err.println("new line: " + newline);
		pw.println(newline);
		lineNumber++;
		
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
	

	@Override
	public void update(Observable o, Object arg) {
	
				
//				writeline();
	
	}

}
