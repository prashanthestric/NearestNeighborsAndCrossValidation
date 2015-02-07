package knnandkfoldcrossval;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Consists of properties and methods required for performing k-fold cross validation
 * 
 * @author Prashanth Govindaraj
 */
public class KFoldCrossValidation {
	
	int kOfCV;
	KNearestNeighbors kNN;
	ArrayList<TheDataModel> dataSet;
	DataProcessor dataProcessor;
	int noOfPermutations;
	int noOfExamples;
	ArrayList<String[]> fileData;
	
	/**
	 * Constructor
	 * 
	 * @param kNN the kNearestNeighbors object. Cross validation is done using the k-nearest neighbors algorithm in "kNN"
	 * @param dataSet ArrayList of examples with which cross validation is to be done
	 */
	public KFoldCrossValidation(KNearestNeighbors kNN,ArrayList<TheDataModel> dataSet) throws IOException {
		// TODO Auto-generated constructor stub
		URL dataFilePath = getClass().getResource("crossValInfo.txt");
		TextFileHandler textFileHandler=new TextFileHandler(dataFilePath.getPath());
		dataProcessor=new DataProcessor();
		fileData=textFileHandler.readData();
		this.kOfCV=dataProcessor.getKOfCV(fileData);
		this.noOfExamples=dataProcessor.getNoOfExamplesCV(fileData);
		this.noOfPermutations=dataProcessor.getNoOfPermutationsCV(fileData);
		this.kNN=kNN;
		this.dataSet=dataSet;
	}
	
	/**
	 * Performs cross validation and calculates error, Variance and sigma and updates output file
	 */
	public String crossValidate() throws IOException{
		int noOfElementsInAChunk=noOfExamples/kOfCV;
		if(kNN.getNoOfNeighbors()>noOfElementsInAChunk){
			System.out.println("Error! Cannot perform cross validation. 'k' of k-NN:"+kNN.getNoOfNeighbors()+" is greater than number of instances in training chunk:"+noOfElementsInAChunk);
			return "Error! Cannot perform cross validation. 'k' of k-NN:"+kNN.getNoOfNeighbors()+" is greater than number of instances in training chunk:"+noOfElementsInAChunk;
		}
		ArrayList<Integer[]> permutations=getPermutations();
		ArrayList<Integer> noOfErrorsInPermutation=new ArrayList<Integer>(); //arraylist of number of errors in each permutation
		for(int i=0;i<noOfPermutations;i++){
			System.out.println("Permutation "+i);
			ArrayList<ArrayList<TheDataModel>> chunks=getChunks(permutations,i);
			Integer noOfErrors=new Integer(0);
			for(int j=0;j<kOfCV;j++){
				ArrayList<TheDataModel> testChunk=chunks.get(j);
				ArrayList<TheDataModel> trainingChunk=mergeChunksExcept(j,chunks);
				
				System.out.println("training chunk"); printdata(trainingChunk); System.out.println("\n");
				System.out.println("test chunk"); printdata(testChunk); System.out.println("\n");
				
				for(int ii=0;ii<testChunk.size();ii++){					
					if(!testChunk.get(ii).getY().equals(kNN.classifyInstance(testChunk.get(ii), trainingChunk))){
						
						noOfErrors++;
					}
				}				
			}
			noOfErrorsInPermutation.add(noOfErrors);
		}
		
		double e=(double)calculateError(noOfErrorsInPermutation);
		double V=(double)calculateVariance(e, noOfErrorsInPermutation);
		double sigma=(double)Math.sqrt(V);
		TextFileHandler fileWriter=new TextFileHandler(getClass().getResource("output.txt").getPath());
		fileWriter.writeCrossValidationResult(e, V, sigma,kOfCV,kNN.getNoOfNeighbors());
		return "Completed!";
	}
	
	/**
	 * calculates value of 'e' from the number of errors in each permutation
	 * 
	 * @param noOfErrorsInPermutation ArrayList containing number of errors in each permutation
	 * @return e overall error 'e'
	 */
	double calculateError(ArrayList<Integer> noOfErrorsInPermutation){
		double e=0.0;
		for(int j=0;j<noOfErrorsInPermutation.size();j++){
			System.out.println("no of errors in permutation"+j+"="+noOfErrorsInPermutation.get(j));
			e+=((double)noOfErrorsInPermutation.get(j)/(double)dataSet.size());			
		}		
		e/=(double)noOfPermutations;
		return e;
	}
	
	/**
	 * Calculates variance 
	 * 
	 * @param e overall error
	 * @param noOfErrorsInPermutation ArrayList containing number of errors in each permutation
	 * @return Variance 'V'
	 */
	double calculateVariance(double e,ArrayList<Integer> noOfErrorsInPermutation){
		double V=0.0;
		for(int i=0;i<noOfPermutations;i++){
			double EjMinuse=((double)noOfErrorsInPermutation.get(i)/(double)dataSet.size())-e;
			V+=(EjMinuse*EjMinuse);
		}
		V/=(double)(noOfPermutations-1);
		return V;
	}
	
	/**
	 * Prints an arrayList of data in console
	 */
	void printdata(ArrayList<TheDataModel> data){
		for(int i=0;i<data.size();i++){
			System.out.println(data.get(i).getX1()+"\t"+data.get(i).getX2()+"\t"+data.get(i).getY());
		}
	}
	
	/**
	 * <p>Merges two or more chunks of data except one specified chunk.</p><p>
	 * The merged chunks eventually becomes training set, and the discarded one, the test set for cross validation.</p>
	 * 
	 * @param discardedChunk Index of the chunk which shouldn't be merged. i.e., the test set
	 * @param chunks ArrayList of ArrayList of instances, i.e., the m/k chunks of instances used for cross validation
	 */
	ArrayList<TheDataModel> mergeChunksExcept(int discardedChunk,ArrayList<ArrayList<TheDataModel>> chunks){
		ArrayList<TheDataModel> mergedChunks=new ArrayList<TheDataModel>();
		for(int i=0;i<chunks.size();i++){
			if(i!=discardedChunk){
				ArrayList<TheDataModel> aChunk=chunks.get(i);
				for(int j=0;j<aChunk.size();j++){
					mergedChunks.add(aChunk.get(j));
				}
			}
		}
		return mergedChunks;
	}
	
	/**
	 * Reads the permutation order from the file
	 * 
	 * @return ArrayList of permuted indices
	 */
	ArrayList<Integer[]> getPermutations(){
		ArrayList<Integer[]> permutations=new ArrayList<Integer[]>();
		
		for(int i=0;i<noOfPermutations;i++){
			Integer[] permutation=new Integer[noOfExamples];
			for(int j=0;j<noOfExamples;j++){
				String[] temp=fileData.get(i+1);
				permutation[j]=Integer.parseInt(temp[j]);
			}
			permutations.add(permutation);
		}
		
		return permutations;
	}
	
	/**
	 * Divides the instances into chunks of data for cross validation, in the given permutation order
	 * 
	 * @param permutations ArrayList of permutation orders
	 * @param permutationNumber index of the permutation order that is required
	 * @return An ArrayList of ArrayList of instances ordered in the specified permutation order and divided into k chunks
	 */
	ArrayList<ArrayList<TheDataModel>> getChunks(ArrayList<Integer[]> permutations,int permutationNumber){
		ArrayList<ArrayList<TheDataModel>> chunks=new ArrayList<ArrayList<TheDataModel>>();
		int minInstancesPerChunk=noOfExamples/kOfCV;
		Integer[] permutationIndices=permutations.get(permutationNumber);
		int x=0;
		for(int i=0;i<(kOfCV-1);i++){
			ArrayList<TheDataModel> aChunk=new ArrayList<TheDataModel>();
			for(int j=0;j<minInstancesPerChunk;j++){				
				aChunk.add(dataSet.get(permutationIndices[x++]));
			}
			chunks.add(aChunk);
		}
		ArrayList<TheDataModel> aChunk=new ArrayList<TheDataModel>();
		for(int ix=x;ix<dataSet.size();ix++){			
			aChunk.add(dataSet.get(permutationIndices[x++]));
		}
		chunks.add(aChunk);
		return chunks;
	}
}
