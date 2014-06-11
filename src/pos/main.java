package pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class main {

	/**
	 * READ ME!
	 * A DocumentDictionary is a count of all the keys with the words that follow. 
	 * A ContextModel is an indication of what parts of speech follow what. 
	 */
	public static void main(String[] args) {
		
		main m = new main();
		m.runViterbi();
		//File f = new File("res" + File.separator + "frost_poems.txt");
		//m.runLanguageModel(f);
		
	}
	
	public void runLanguageModel(File f){
		int numOfWords = 15;
		
		OneWordContextModel cm = new OneWordContextModel(f);
		TwoWordContextModel cm2 = new TwoWordContextModel(f);
		
		DocumentDictionary dd = new DocumentDictionary(f);
		
		NGramLanguageModeler modeler = new NGramLanguageModeler(dd); 
		modeler.runOneWordContextModel(cm, numOfWords);
		modeler.runTwoWordContextModel(cm2, numOfWords);
	}
	
	public void runViterbi(){
		System.out.print("reading files...");
		Viterbi v = new Viterbi(new File("res"+File.separator+"frost_poems.txt"), false);
		String[][] obs =  main.readFile(new File("res"+File.separator+"frost_test.txt"));
		System.out.println("...done reading files");
		
		v.printData(obs[0]);
		//System.out.println("running viterbi single...");
		ArrayList<Object> results = v.run(obs[0]);
		ArrayList<String> correct = objArrayToStringArrayList(obs[1]);
		
		ConfusionMatrix confusionMatrix = new ConfusionMatrix(v.getContextModel());
		confusionMatrix.generate(correct, results);
		System.out.println("Best Guess (Single) :" + results);
		System.out.println("        Real Result :" + correct);
		System.out.println("\nPrinting bigram confusion matrix:\n\n"+ confusionMatrix+"\n\n\n");
		
		
		v = new Viterbi(new File("res"+File.separator+"frost_poems.txt"), true);
		v.printData(obs);
		ArrayList<Object> guess = twoWordKeyToFinalResult(v.run(obs[0]));
		ConfusionMatrix confusionMatrix2 = new ConfusionMatrix(v.getContextModel());
		confusionMatrix2.generate(correct, guess);
		System.out.println("Best Guess (Double) "+guess);
		System.out.println("Real Result " + objArrayToStringArrayList(obs[1]));
		System.out.println("\nPrinting trigram confusion matrix:\n\n"+ confusionMatrix2+"\n\n\n");
		
		
	}
	
	public static ArrayList<String> objArrayToStringArrayList(Object[] obs){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < obs.length; i++){
			list.add((String) obs[i]);
		}
		return list;
	}
	
	public static ArrayList<Object> twoWordKeyToFinalResult(ArrayList<Object> obs){
		ArrayList<Object> result = new ArrayList<Object>();
		for(int i = 0; i < obs.size(); i++){
			result.add(((TwoWordKey)(obs.get(i))).w2);
		}
		return result;
	}
	
	public static String[][] readFile(File file){
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error occured with: "+ file.getAbsolutePath());
			System.out.println("Program will exit");
			e.printStackTrace();
			System.exit(0);
		}

		
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> keys = new ArrayList<String>();
		
		while(scanner.hasNext()){
			String[] split = scanner.next().split("_");
			words.add(split[0]);
			keys.add(split[1]);
		}
		scanner.close();
		String[][] toReturn = new String[2][words.size()];
		String[] wordArray = new String[words.size()];
		String[] keyArray = new String[words.size()];
		
		wordArray =  words.toArray(wordArray);
		keyArray =  keys.toArray(keyArray);
		toReturn[0] = wordArray;
		toReturn[1] = keyArray;
		
		return toReturn;
	}
}
