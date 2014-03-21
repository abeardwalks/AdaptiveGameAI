import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;



public class GraphDataTranslation {
	
	private FileReader fr;
	private BufferedReader reader;
	private Scanner scan;
	private String fileToRead;
	private String fileToWrite;
	private ArrayList<String> newLines;
	
	public GraphDataTranslation(){
		fileToRead = "";
		
		requestFilePath();
		
	}
	
	public void readFile(){
		newLines = new ArrayList<String>();
		try {
			fr = new FileReader(fileToRead);
			reader = new BufferedReader(fr);
			String line = reader.readLine();
			newLines.add(line);
			line = reader.readLine();
			while(line != null){
				String[] data = line.split(",");
				
				int count = 0;
				int value = 0;
				String plotPoints = "";
				if(data.length == 8){
					for (char c : data[7].toCharArray()) {
						if(c == 'R'){
							value++;
						}else{
							value--;
						}
						plotPoints += "," + value;
						count++;
					}
					while(count < 13){
						plotPoints += "," + value;
						count++;
					}
					line += plotPoints;
				}
				newLines.add(line);
				System.out.println("Line added: " + line);
				line = reader.readLine();
			}
			reader.close();
			fr.close();
			writeFile();
		} catch (FileNotFoundException e) {
			System.out.println("File Not found!!");
			requestFilePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeFile(){
		try {
			FileWriter w = new FileWriter(fileToRead);
			BufferedWriter bw = new BufferedWriter(w);
			for (String line : newLines) {
				bw.write(line);
				bw.newLine();
			}
			bw.close();
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		requestFilePath();
	}
	
	public void requestFilePath(){
		
		try {
			System.out.println("Please enter the full path of the file to read:");
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			fileToRead = bufferRead.readLine();
			if(fileToRead.equals("quit")){
				System.exit(0);
			}
			System.out.println("Please enter the full path of the file to write:");
			bufferRead = new BufferedReader(new InputStreamReader(System.in));
			fileToWrite = bufferRead.readLine();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		readFile();
		
	}

}
