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
	 * Right now, it works for single-word context and nothing else (not even zero-word context) 
	 * but it should be easy to add in the flexibility.
	 * 
	 * I didn't quickly figure out how to get the next word probabilistically, so I'm just doing random selection right now.
	 * <Ian says> I did this
	 * 
	 * For speed purposes, we might need to have a "createModelAndDictionary" method somewhere that builds both at the same time
	 * since each parses the file separately, so large files would become an issue, but that's not crucial yet.
	 * 
	 * Feel free to adapt any and all of the existing stuff, and good luck!
	 */
	public static void main(String[] args) {
		
		main m = new main();
		m.runViterbi();
		
	}
	
	public void runLanguageModel(File f){
		int numOfWords = 20;
		
		OneWordContextModel cm = new OneWordContextModel(f);
		TwoWordContextModel cm2 = new TwoWordContextModel(f);
		
		DocumentDictionary dd = new DocumentDictionary(f);
		
		NGramLanguageModeler modeler = new NGramLanguageModeler(dd); 
		modeler.runOneWordContextModel(cm, numOfWords);
		modeler.runTwoWordContextModel(cm2, numOfWords);
	}
	
	public void runViterbi(){
		System.out.print("reading files...");
		Viterbi v = new Viterbi(new File("res"+File.separator+"allTraining.txt"), false);
		String[][] obs =  main.readFile(new File("res"+File.separator+"little_test.txt"));
		System.out.println("...done reading files");
	
		v.printData(obs[0]);
		System.out.println("running viterbi single...");
		ArrayList<Object> results = v.run(obs[0]);
		System.out.println("Best Guess (Single) " + results);
		System.out.println("Real Result " + arrayAsString(obs[1]));
		
		/*v = new Viterbi(new File("res"+File.separator+"little_train.txt"), true);
		//v.printData(obs);
		ArrayList<Object> guess = v.run(obs[0]);
		
		System.out.println("Best Guess (Double) "+twoWordKeyToFinalResult(guess));
		System.out.println("Real Result " + arrayAsString(obs[1]));*/
		
	}
	
	public static String arrayAsString(Object[] obs){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i = 0; i < obs.length-1; i++){
			sb.append(obs[i]);
			sb.append(", ");
		}
		sb.append(obs[obs.length-1]);
		sb.append("]");
		return sb.toString();
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
