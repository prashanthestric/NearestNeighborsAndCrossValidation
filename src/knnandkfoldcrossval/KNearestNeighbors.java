package knnandkfoldcrossval;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Contains properties and methods required to perform k-Nearest neighbors
 * 
 * @author Prashanth Govindaraj
 */
public class KNearestNeighbors {
	
	int noOfNeighbors;
	
	/**
	 * Constructor assigns k value for nearest neighbors
	 */
	public KNearestNeighbors(int k){
		noOfNeighbors=k;
	}
	
	/**
	 * getter method for noOfNeighbors
	 */
	public int getNoOfNeighbors() {
		return noOfNeighbors;
	}
	
	/**
	 * Labels an instance of unclassified data 
	 * 
	 * @param unclassifiedInstance the instance that has to be classified
	 * @param classifiedData ArrayList of training data.
	 */
	public String classifyInstance(TheDataModel unclassifiedInstance, ArrayList<TheDataModel> classifiedData){
		
		ArrayList<Integer> distances=calculateDistances(unclassifiedInstance,classifiedData);
		ArrayList<TheDataModel> nearestNeighbors=getNearestNeighbors(classifiedData,distances,noOfNeighbors);
		
		return findMajority(nearestNeighbors);
		
	}
	
	/**
	 * Calculates the distance of a given instance from all the training examples
	 * 
	 * @param unclassifiedInstance the instance for which distance has to be calculated
	 * @param classifiedData ArrayList of training data
	 * @return An ArrayList consisting distances between "unclassifiedInstance" and each instance in "classifiedData"
	 */
	ArrayList<Integer> calculateDistances(TheDataModel unclassifiedInstance, ArrayList<TheDataModel> classifiedData){
		
		ArrayList<Integer> distances=new ArrayList<Integer>();
		for(int i=0;i<classifiedData.size();i++){
			int x1diff=unclassifiedInstance.getX1()-classifiedData.get(i).getX1();
			int x2diff=unclassifiedInstance.getX2()-classifiedData.get(i).getX2();
			int dist=(x1diff*x1diff)+(x2diff*x2diff);
			distances.add(dist);
		}
		return distances;
		
	}
	
	/**
	 * Returns the index at which minimum value is present in an Integer ArrayList
	 * 
	 * @param distances an Integer ArrayList
	 * @return index at which minimum value is present
	 */
	int getIndexOfMinimum(ArrayList<Integer> distances){
		
		return distances.indexOf(Collections.min(distances));
		
	}
	
	/**
	 * Determines whether the given set of instances contains majority of + or -. Here the set is usually the nearest neighbors to an instance.
	 * 
	 * @param nearestNeighbors ArrayList of classified instances
	 * @return '+' if number of pluses > number of minuses, '-' otherwise 
	 */
	String findMajority(ArrayList<TheDataModel> nearestNeighbors){
		
		int noOfPositives=0;
		int noOfNegatives=0;
		String y;
		for(int i=0;i<nearestNeighbors.size();i++){
			if(nearestNeighbors.get(i).getY().equals("+")){
				noOfPositives++;
			}
			else{
				noOfNegatives++;
			}
		}
		
			y=(noOfPositives > noOfNegatives)?"+":"-";
				
		return y;
		
	}
	
	/**
	 * Gives a set of nearest neighbors to an instance
	 * 
	 * @param classifiedData ArrayList of training examples
	 * @param distances ArrayList of distances between an unclassified instance and each instance in "classifiedData"
	 * @return ArrayList of nearest neighbors to an instance
	 */
	ArrayList<TheDataModel> getNearestNeighbors(ArrayList<TheDataModel> classifiedData,ArrayList<Integer> distances,int noOfNeighbors){
		
		ArrayList<Integer> distances1=new ArrayList<Integer>(distances);
		ArrayList<TheDataModel> nearestNeighbors=new ArrayList<TheDataModel>();
		for(int i=0;i<noOfNeighbors;i++){
			int minIndex=getIndexOfMinimum(distances1);
			distances1.set(minIndex,(Collections.max(distances1)+1));
			nearestNeighbors.add(classifiedData.get(minIndex));
		}
		return nearestNeighbors;
		
	}
	
}
