package knnandkfoldcrossval;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Consists of methods and properties for accessing and modifying input/output files
 * 
 * @author Prashanth Govindaraj
 */
public class TextFileHandler {
	private String fileLocation;
	
	/**
	 * Constructor for TextFileReader receives the path of the text file to be used.
	 * 
	 * @param file_location path of the text file to be used
	 */
	public TextFileHandler(String file_location){
		fileLocation=file_location;
	}
	
	public ArrayList<String[]> readData() throws IOException{
		
		ArrayList<String[]> records=new ArrayList<String[]>();
		FileReader fileReader=new FileReader(fileLocation);
		BufferedReader bufferedReader=new BufferedReader(fileReader);
		int numOfLines=noOfLines();
		
		for(int i=0;i<numOfLines;i++){
			String temp_record=bufferedReader.readLine();
			String[] rec=temp_record.split(" ");
			String[] record=new String[rec.length];
			for(int j=0;j<rec.length;j++){
				record[j]=rec[j];
			}
			
			records.add(record);
		}
		
		bufferedReader.close();
		return records;		
	}
	
	/**
	 * Writes output of nearest neighbors to the output file.
	 * 
	 * @param allData ArrayList of all data
	 * @param records ArrayList of data read from file
	 */
	public void writeOutput(ArrayList<TheDataModel> allData,ArrayList<String[]> records) throws IOException{
		
		FileWriter fileWriter=new FileWriter(fileLocation,false);
		PrintWriter printWriter=new PrintWriter(fileWriter);
		DataProcessor dataProc=new DataProcessor();
		int noOfRows=dataProc.getNoOfRows(records);
		int noOfCols=dataProc.getNoOfCols(records);
		
		int x=0;
		for(int i=0;i<noOfRows;i++){
			StringBuilder temp=new StringBuilder();
			for(int j=0;j<noOfCols;j++){
				TheDataModel rec=allData.get(x++);
				temp.append(rec.getY()+" ");
				//System.out.println(rec.getY());
			}
			//write line to file
			System.out.println(temp.toString());
			printWriter.println(temp.toString());
		}
		printWriter.close();
	}
	
	/**
	 * Appends the results of cross validation to the output file.
	 * 
	 * @param e error of cross validation
	 * @param V variance of cross validation
	 * @param sigma sigma of cross validation
	 * @param kOfCV "k" value for cross validation
	 * @param kOfNN "k" value for nearest neighbors
	 */
	public void writeCrossValidationResult(double e,double V,double sigma,int kOfCV,int kOfNN) throws IOException{
		
		FileWriter fileWriter=new FileWriter(fileLocation,true);
		PrintWriter printWriter=new PrintWriter(fileWriter);
		printWriter.println();
		printWriter.println("Cross validation results("+kOfNN+"-Nearest neighbors, "+kOfCV+"-fold cross validation):");
		printWriter.println("-------------------------------------------------------------------------");
		printWriter.println("e = "+e);
		printWriter.println("V = "+V);
		printWriter.println("Sigma = "+sigma);
		printWriter.close();
	}
	
	
	/**
	 * This method is used to compute the total number of records stored in the text file.
	 * 
	 * @return number of lines in the input text file
	 */
	int noOfLines() throws IOException{
		FileReader fileReader=new FileReader(fileLocation);
		BufferedReader bufferedReader=new BufferedReader(fileReader);
		String input;
		int numOfLines=0;
		
		while ((input=bufferedReader.readLine())!=null){
			numOfLines++;
			}
		
		bufferedReader.close();
		return numOfLines;
	}

}
