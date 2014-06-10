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
		
		/*File f = new File("res"+File.separator+"little_test.txt");
		//File f = new File("res"+File.separator+"devtest.txt");
		
		DocumentDictionary dd = new DocumentDictionary(f);
		//System.out.println(dd.toString());
		OneWordContextModel ocm = new OneWordContextModel(f);
		//System.out.println(ocm.toString());
		
		TwoWordContextModel tcm = new TwoWordContextModel(f);
		//System.out.println(tcm.toString());

		NGramLanguageModeler ng = new NGramLanguageModeler(ocm, dd);
		ng.runOneWordContextModel(ocm, 20);//run(desiredLengthOfParagraph)
		ng.runTwoWordContextModel(tcm, 20);
		
		*/
		
		Viterbi v = new Viterbi(new File("res"+File.separator+"little_train.txt"));
		Object[] obs =  main.readFile(new File("res"+File.separator+"little_test.txt"));
		v.printData(obs);
		v.run(obs);
		
		/*main m = new main();
		m.runTest();*/
		
	}
	
	public void runTest(){
		int totalKeyOccurrences = 0;
		ContextModel cm = new OneWordContextModel(new File("res"+File.separator+"viterbi_train.txt"));
		
		HashMap<Object, Integer> cm_keyOccurrences = new HashMap<Object, Integer>();
		cm_keyOccurrences.put("healthy", 60);
		cm_keyOccurrences.put("fever", 40);
		
		HashMap<Object, ArrayList<WordNode>> cm_model = new HashMap<Object, ArrayList<WordNode>>();
		
		ArrayList<WordNode> hc_list = new ArrayList<WordNode>();
		hc_list.add(new WordNode("healthy", 42));
		hc_list.add(new WordNode("fever", 18));
		cm_model.put("healthy", hc_list);
		
		ArrayList<WordNode> fc_list = new ArrayList<WordNode>();
		fc_list.add(new WordNode("healthy", 16));
		fc_list.add(new WordNode("fever", 24));
		cm_model.put("fever", fc_list);
		
		totalKeyOccurrences = 100;
		cm.model = cm_model;
		cm.keyOccurrences = cm_keyOccurrences;
		cm.totalKeyOccurrences = totalKeyOccurrences;
		
		//{{"Healthy", 0.6}, {"Fever", 0.4}};
		
		HashMap<Object, ArrayList<WordNode>> dd_model = new HashMap<Object, ArrayList<WordNode>>();
		ArrayList<WordNode> h_list = new ArrayList<WordNode>();
		h_list.add(new WordNode("normal", 30));
		h_list.add(new WordNode("cold", 24));
		h_list.add(new WordNode("dizzy", 6));
		
		dd_model.put("healthy", h_list);
		
		ArrayList<WordNode> f_list = new ArrayList<WordNode>();
		f_list.add(new WordNode("normal", 4));
		f_list.add(new WordNode("cold", 12));
		f_list.add(new WordNode("dizzy", 24));
		
		dd_model.put("fever", f_list);
		
		DocumentDictionary dd = new DocumentDictionary(new File("res"+File.separator+"viterbi_train.txt"));
		dd.partsOfSpeech = dd_model;
		dd.posCounts = cm_keyOccurrences;
		
		File f = new File("res"+File.separator+"viterbi_little_2.txt");
		
		Viterbi v = new Viterbi(cm, dd);
		v.run(readFile(f));
	}
	
	public static Object[] readFile(File file){
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
		while(scanner.hasNext()){
			words.add(scanner.next());
		}	
		scanner.close();
		String[] toReturn = new String[words.size()];
		return words.toArray(toReturn);
		
		
	}
}
