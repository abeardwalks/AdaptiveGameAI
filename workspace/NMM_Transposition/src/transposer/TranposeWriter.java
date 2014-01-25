package transposer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class TranposeWriter {
	
	public int fileNumber;
	
	public TranposeWriter(){
		fileNumber = 0;
	}
	
	public void writeFile(){
		try {
			
	        BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\Andrew\\Documents\\cs408-individual-project\\transposition-data\\"
	        									+ "trans-" + fileNumber + ".txt"));
	        PrintWriter pw = new PrintWriter(out);
	            for (int i = 0; i < 4; i++) {
	                pw.println("Test");
	            }
	            out.close();
	        } catch (IOException e) {}
		
		fileNumber++;
	}
	
	public int getNumberOfWrites(){
		return fileNumber;
	}

	public void writeFile(ArrayList<String> stateStrings) {
		try {
			
	        BufferedWriter out = new BufferedWriter(new FileWriter("C:\\Users\\Andrew\\Documents\\cs408-individual-project\\transposition-data\\"
	        									+ "trans-" + fileNumber + ".txt"));
	        PrintWriter pw = new PrintWriter(out);
	            for (String string : stateStrings) {
					pw.println(string);
				}
	            out.close();
	        } catch (IOException e) {}
		
		fileNumber++;
	}

}
