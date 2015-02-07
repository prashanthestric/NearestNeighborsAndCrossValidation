package knnandkfoldcrossval;
import java.util.ArrayList;

/**
 * Class that consists of methods to get information from the data fetched from the input files
 * 
 * @author Prashanth Govindaraj
 */
public class DataProcessor {
	
	/**
	 * Returns the data in a structured format for processing
	 * 
	 * @param records unstructured data fetched from the file
	 */
	public ArrayList<TheDataModel> getDataTabulated(ArrayList<String[]> records){
		
			ArrayList<TheDataModel> tabulatedData=new ArrayList<TheDataModel>();
			String[] firstLine=records.get(0);
			int noOfRows=Integer.parseInt(firstLine[0]);
			int noOfCols=Integer.parseInt(firstLine[1]);
		
			for(int i=1;i<(noOfRows+1);i++){
				String[] row=records.get(i);
				for(int j=0;j<noOfCols;j++){
				
					tabulatedData.add(new TheDataModel(j, i-1, row[j]));
				
				}
			}
		
		return tabulatedData;
		
	}
	
	/**
	 * returns the number of rows of examples in the raster ordered input file
	 */
	public int getNoOfRows(ArrayList<String[]> records){
		String[] firstLine=records.get(0);
		return Integer.parseInt(firstLine[0]);
	}
	
	/**
	 * returns the number of columns of examples in the raster ordered input file
	 */
	public int getNoOfCols(ArrayList<String[]> records){
		String[] firstLine=records.get(0);
		return Integer.parseInt(firstLine[1]);
	}
	
	/**
	 * calculates the index number of an instance (x1,x2)
	 */
	public int getIndexOfCoordinate(TheDataModel data,ArrayList<String[]> records){
		return ((data.getX2()*getNoOfCols(records))+data.getX1());
	}
	
	/**
	 * returns k value for cross validation
	 */
	public int getKOfCV(ArrayList<String[]> records){
		String[] firstLine=records.get(0);
		return Integer.parseInt(firstLine[0]);
	}
	
	/**
	 * returns number of examples for cross validation
	 */
	public int getNoOfExamplesCV(ArrayList<String[]> records){
		String[] firstLine=records.get(0);
		return Integer.parseInt(firstLine[1]);
	}
	
	/**
	 * returns number of permutations for cross validation
	 */
	public int getNoOfPermutationsCV(ArrayList<String[]> records){
		String[] firstLine=records.get(0);
		return Integer.parseInt(firstLine[2]);
	}
	
	/**
	 * returns an Arraylist that contains only classified data(training examples)
	 * 
	 * @param tabulatedData An arrayList of all data, both classified and unclassified
	 * @return classifiedData an ArrayList that contains only classified data
	 */
	public ArrayList<TheDataModel> getClassifiedData(ArrayList<TheDataModel> tabulatedData){
		
		ArrayList<TheDataModel> classifiedData=new ArrayList<TheDataModel>();
		for(int i=0;i<tabulatedData.size();i++){
			if(!tabulatedData.get(i).getY().equals(".")){
				classifiedData.add(tabulatedData.get(i));
			}			
		}
		return classifiedData;
		
	}
	
	/**
	 * returns an Arraylist that contains only unclassified data.
	 * 
	 * @param tabulatedData An arrayList of all data, both classified and unclassified
	 * @return classifiedData an ArrayList that contains only unclassified data
	 */
	public ArrayList<TheDataModel> getUnclassifiedData(ArrayList<TheDataModel> tabulatedData){
		
		ArrayList<TheDataModel> unclassifiedData=new ArrayList<TheDataModel>();
		for(int i=0;i<tabulatedData.size();i++){
			if(tabulatedData.get(i).getY().equals(".")){
				unclassifiedData.add(tabulatedData.get(i));
			}			
		}
		return unclassifiedData;
		
	}


}
