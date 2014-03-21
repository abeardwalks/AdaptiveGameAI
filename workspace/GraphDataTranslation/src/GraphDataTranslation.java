import java.io.BufferedReader;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


public class GraphDataTranslation {
	
	private FileReader fr;
	private BufferedReader reader;
	private Scanner scan;
	private String fileToRead;
	private String fileToWrite;
	
	public GraphDataTranslation(){
		fileToRead = "";
		requestFilePath();
	}
	
	public void readFile(){
		try {
			fr = new FileReader(fileToRead);
			reader = new BufferedReader(fr);
			String line = reader.readLine();
			line = reader.readLine();
			while(line != null){
				
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not found!!");
			requestFilePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeFile(){
		
	}
	
	public void requestFilePath(){
		
		try {
			System.out.println("Please enter the full path of the file to read:");
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			fileToRead = bufferRead.readLine();
			
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
