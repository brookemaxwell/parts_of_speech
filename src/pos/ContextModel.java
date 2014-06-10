package pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
/**
 * This is a class to map parts of speech to their followers. 
 * Right now it only works with 1-word pairings, but it should be able to do more than that.
 * 
 * To see its layout, run main.java.
 * @author Brooke
 *
 */
public abstract class ContextModel{

	HashMap<Object, ArrayList<WordNode>> model;
	Random rand;
	HashMap<Object, Integer> keyOccurrences;
	int totalKeyOccurrences = 0;
	
	public ContextModel(File file) {
		model = new HashMap<Object, ArrayList<WordNode>>();
		keyOccurrences = new HashMap<Object, Integer>();
		
	}	
	
	public Object[] getStates(){
		return model.keySet().toArray();
	}
	
	public Double getStateProbability(Object key){
		if(keyOccurrences.keySet().contains(key))
			return keyOccurrences.get(key) / (double) totalKeyOccurrences;
		return .00001;
	
	}
	
	public Double getTransProbability(Object key, Object nextKey){
		ArrayList<WordNode> words = model.get(key);
		for(WordNode wn : words){
			if(wn.word.equals(nextKey)){
				return (double)wn.count / (double)keyOccurrences.get(key);
			}
		}
		return .00001;
	
	}
	
	public LinkedList<String> getSeedWords(){
		LinkedList<String> context = new LinkedList<String>();

		context.add("");
		return context;
	}

	public static String trim(String key) {
		String temp = key;
		
		//makes the key lower case
		key = key.toLowerCase();
		
		//trim off the none letter characters
		while(!key.equals("") && !Character.isLetter(key.charAt(0))){
			key = key.substring(1);
		}
		while(!key.equals("") && !Character.isLetter(key.charAt(key.length()-1))){
			key = key.substring(0, key.length()-1);
		}
		
		return key;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("ContextModel:");
		for(Object key: model.keySet()){
			sb.append("\n\t"+ key + " ("+keyOccurrences.get(key) + ")");
			List<WordNode> wnList = model.get(key);
			for(WordNode wn : wnList){
				sb.append("\n\t\t"+ wn.word +"  "+ wn.count);
			}
			
		}
		return sb.toString();
	}

}
