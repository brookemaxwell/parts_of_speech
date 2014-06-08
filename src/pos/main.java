package pos;

import java.io.File;
import java.io.IOException;
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
		
		File f = new File("res"+File.separator+"little_test.txt");
		//File f = new File("res"+File.separator+"devtest.txt");
		
		DocumentDictionary dd = new DocumentDictionary(f);
		System.out.println(dd.toString());
		OneWordContextModel ocm = new OneWordContextModel(f);
		System.out.println(ocm.toString());
		
		TwoWordContextModel tcm = new TwoWordContextModel(f);
		System.out.println(tcm.toString());

		NGramLanguageModeler ng = new NGramLanguageModeler(ocm, dd);
		ng.runOneWordContextModel(ocm, 20);//run(desiredLengthOfParagraph)
		ng.runTwoWordContextModel(tcm, 20);
		
	}
}
