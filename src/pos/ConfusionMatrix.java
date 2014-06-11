package pos;

import java.util.ArrayList;

public class ConfusionMatrix {

	ArrayList<String> docCategories;
	//see comment on incrementPrediction for how the cooridantes map to the matrix
	int[][] matrix;
	double total;
	double correctlyClassified;


	public ConfusionMatrix(ContextModel cm) {
		total=0;
		correctlyClassified=0;
		
		docCategories = new ArrayList<String>();
		for(String key: cm.getWordTypes()){
			docCategories.add(key);
		}
		//the plus one is for "unknown" (values that appear do not appear in the training set but do in test set )
		matrix = new int[docCategories.size()+1][docCategories.size()];
		for(int i =0; i < docCategories.size()+1; i++){
			for(int j =0; j < docCategories.size(); j++){
				matrix[i][j] = 0;
			}
		}
	}


	/* I tired to do this by row column form. [3][1] is the 3rd row down (the 3rd true category)
	 * while [1] is the 1st column in the predicted category 
	 * 
	 * 
	 * */
	public void increment( String actual, String predicted ){
		total++;
		if(actual.equals(predicted)){
			correctlyClassified++;
		}
		int truthIndex = docCategories.indexOf(actual);
		if(truthIndex == -1){
			truthIndex = docCategories.size();
		}
		int predictionIndex = docCategories.indexOf(predicted);
		matrix[truthIndex][predictionIndex]++;
	}

	/* The format in which it prints out makes it so you can copy from the output striaght into a spreadsheet 
	 */
	public String toString(){
		StringBuilder sb =  new StringBuilder();
		sb.append("\t");
		for(String curCat:docCategories){
			sb.append(curCat+"\t");
		}
		sb.append("\n");
		for(int i =0; i < docCategories.size()+1; i++){
			if(i < docCategories.size()){
				sb.append(docCategories.get(i) + "\t");
			}
			else{
				sb.append("unknown" + "\t");
			}
			for(int j =0; j < docCategories.size(); j++){
				sb.append(matrix[i][j]+"\t");
			}
			sb.append("\n");
		}
		sb.append("\n\nClassification Accruracy: "+ correctlyClassified/total);
		return sb.toString();
	}


	/** Generates the matrix from the correct and predicted lists
	 * @param correct
	 * @param resultsObj
	 */
	public void generate(ArrayList<String> correct, ArrayList<Object> resultsObj) {
		ArrayList<String> predicted = objListToStringList(resultsObj);
		
		assert(correct.size() ==  resultsObj.size());
		for(int i =0; i< correct.size(); i++){
			increment(correct.get(i), predicted.get(i));
		}
	}


	public static ArrayList<String> objListToStringList(ArrayList<Object> resultsObj) {
		ArrayList<String> temp = new ArrayList<String>();
		for(Object obj:resultsObj){
			temp.add((String) obj);
		}
		return temp;
	}

	
}
