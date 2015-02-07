package knnandkfoldcrossval;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Controller class for the Interface. All underlying functionality are acessed from within this class
 * 
 * @author Prashanth Govindaraj
 */
public class InterfaceController implements Initializable{
	
	@FXML
	private Button runButton;
	@FXML
	private TextField kTextField;
	@FXML
	private TextArea input1;
	@FXML
	private TextArea input2;
	@FXML
	private TextArea output;
	@FXML
	private Text promptText;
	@FXML
	private Button refresh1;
	@FXML
	private Button refresh2;
	
	TextFileHandler textFileHandler;
	KNearestNeighbors nearestNeighbors;
	DataProcessor dataProcessor;
	ArrayList<String[]> fileData;
	ArrayList<TheDataModel> allData;
	ArrayList<TheDataModel> classifiedData;
	ArrayList<TheDataModel> unclassifiedData;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		// TODO Auto-generated method stub
		allowOnlyNumbers(kTextField);
		URL dataFilePath = getClass().getResource("data.txt");
		TextFileHandler textFileHandler=new TextFileHandler(dataFilePath.getPath());
		try {
			populateInputData();
			fileData=textFileHandler.readData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataProcessor=new DataProcessor();
				
		allData=dataProcessor.getDataTabulated(fileData);
		classifiedData=dataProcessor.getClassifiedData(allData);
		unclassifiedData=dataProcessor.getUnclassifiedData(allData);
		
	}
	
	/**
	 * Reads data from the input file and displays it in the text areas.
	 */
	void populateInputData() throws IOException{
		//input textArea 1
		FileReader fileReader=new FileReader(getClass().getResource("crossValInfo.txt").getPath());
		BufferedReader bufferedReader=new BufferedReader(fileReader);
		StringBuilder stringBuilder = new StringBuilder();
		String text1;
		while ((text1 = bufferedReader.readLine()) != null) {
		    stringBuilder.append(text1).append("\n");
		}
		input1.setText(stringBuilder.toString());
		//input textArea 2
		fileReader=new FileReader(getClass().getResource("data.txt").getPath());
		bufferedReader=new BufferedReader(fileReader);
		stringBuilder = new StringBuilder();
		String text2;
		while ((text2 = bufferedReader.readLine()) != null) {
		    stringBuilder.append(text2).append("\n");
		}
		input2.setText(stringBuilder.toString());
	}
	
	/**
	 * Reads data from the output file and displays it in the text areas.
	 */
	void populateOutputData() throws IOException{
		//input textArea 1
		FileReader fileReader=new FileReader(getClass().getResource("output.txt").getPath());
		BufferedReader bufferedReader=new BufferedReader(fileReader);
		StringBuilder stringBuilder = new StringBuilder();
		String text1;
		while ((text1 = bufferedReader.readLine()) != null) {
		    stringBuilder.append(text1).append("\n");
		}
		output.setText(stringBuilder.toString());
	}
	
	/**
	 * Click listener for run button.
	 * @throws IOException
	 */
	public void runButtonClick() throws IOException{
		
		if(kTextField.getText().equals("")){
			promptText.setText("Enter value of 'k'");
			return;
		}		
		nearestNeighbors=new KNearestNeighbors(Integer.parseInt(kTextField.getText()));
		ArrayList<TheDataModel> newlyClassified=classifyData(unclassifiedData,classifiedData);
		mergeData(newlyClassified, allData, fileData);
		//writing to output file
		URL outputFilePath = getClass().getResource("output.txt");
		TextFileHandler outputFileHandler=new TextFileHandler(outputFilePath.getPath());
		outputFileHandler.writeOutput(allData, fileData);
		//k fold cross validation
		KFoldCrossValidation kfcrsval=new KFoldCrossValidation(nearestNeighbors, classifiedData);
		promptText.setText(kfcrsval.crossValidate());
		populateOutputData();
		
	}
	
	/**
	 * Click listener for Refresh button 1
	 * @throws IOException
	 */
	public void refresh1Click() throws IOException{
		populateInputData();
	}
		
	/**
	 * Takes a set of unclassifed data and classifies them based on the given training data
	 * 
	 * @param data ArrayList of unclassified data
	 * @param classifiedData ArrayList of training data
	 * @return classified version of "data"
	 */
	ArrayList<TheDataModel> classifyData(ArrayList<TheDataModel> data,ArrayList<TheDataModel> classifiedData){
		for(int i=0;i<data.size();i++){
			TheDataModel temp=data.get(i);
			data.get(i).setY(nearestNeighbors.classifyInstance(data.get(i), classifiedData));
		}
		return data;
	}
	
	/**
	 * merges the newly classified data into the ArrayList containing all data.
	 * 
	 * @param newlyClassified ArrayList of newly classified data
	 * @param allData ArrayList of all adata
	 * @para fileData required to get the number of rown and columns in the input file
	 */
    void mergeData(ArrayList<TheDataModel> newlyClassified,ArrayList<TheDataModel> allData,ArrayList<String[]> fileData){//file data is a param bcoz it is needed by getindexofcoordinate func
		
		for(int i=0;i<newlyClassified.size();i++){
			allData.set(dataProcessor.getIndexOfCoordinate(newlyClassified.get(i), fileData), newlyClassified.get(i));
		}
						
	}
    
    /**
     * adds a changeListener that constrains the type of data that could be entered.
     * 
     * @param constrainedTextField the text field to which the change listener is to be added
     */
    void allowOnlyNumbers(final TextField constrainedTextField){
		
		constrainedTextField.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable,String oldValue,String newValue) {
	         if(!constrainedTextField.getText().matches("[1-5]*")){
	        	 String temp=constrainedTextField.getText();
	        	 constrainedTextField.setText(temp.substring(0,(temp.length()-1)));
	         }
	      }
	});
		
	}
}
